import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { Observable } from 'rxjs';

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

    logout() {
        localStorage.removeItem('token');
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');
        return !this.jwtHelper.isTokenExpired(token);
    }
}
