import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { lastValueFrom, Observable, Subject } from 'rxjs';
import { switchMap, tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private readonly getUserDetailSubject = new Subject<void>();
    constructor(private http: HttpClient, public jwtHelper: JwtHelperService, private router: Router) {}

    getUserDetail(): Observable<User> {
        return this.getUserDetailSubject.pipe(
            switchMap(() => this.http.get<User>(`${environment.authUrl}/userinfo`)),
            map((u) => {
                u.username = u.firstname ? `${u.firstname} ${u.lastname}` : u.email;
                return u;
            }),
            tap(() => console.log('GET UserInfo request triggered'))
        );
    }

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(`${environment.authUrl}/users`).pipe(
            map((users) => {
                users.map((u) => {
                    u.username = u.firstname ? `${u.firstname} ${u.lastname}` : u.email;
                    return u;
                });
                return users;
            })
        );
    }

    delete(user: User): Observable<User> {
        return this.http.delete<User>(`${environment.authUrl}/user/${user.id}`);
    }

    saveUser(user: User): Observable<User> {
        if (user.id === 0) {
            return this.http.post<User>(`${environment.authUrl}/register`, user);
        } else {
            return this.http.put<User>(`${environment.authUrl}/user/${user.id}`, user);
        }
    }

    triggerRefreshUserInfo(): void {
        this.getUserDetailSubject.next();
    }

    login(credentials: { email: string; password: string }) {
        return this.http.post<{ token: string }>(`${environment.authUrl}/authenticate`, credentials).pipe(
            tap((res) => {
                localStorage.setItem('token', res.token);
            })
        );
    }

    async logout() {
        let r = await lastValueFrom(this.http.get<{ token: string }>(`${environment.authUrl}/logout`));
        localStorage.removeItem('token');
        this.router.navigate(['login']);
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');
        return !this.jwtHelper.isTokenExpired(token);
    }
}
