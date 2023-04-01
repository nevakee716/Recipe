import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    constructor(private http: HttpClient, public jwtHelper: JwtHelperService) {}

    login(credentials: { email: string; password: string }) {
        return this.http.post<{ token: string }>(`${environment.authUrl}/authenticate`, credentials).pipe(
            tap((res) => {
                localStorage.setItem('token', res.token);
            })
        );
    }

    getUserDetail(): Observable<User> {
        return this.http.get<User>(`${environment.authUrl}/userinfo`);
    }

    async logout() {
        let r = await lastValueFrom(this.http.get<{ token: string }>(`${environment.authUrl}/logout`));
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');
        return !this.jwtHelper.isTokenExpired(token);
    }
}
