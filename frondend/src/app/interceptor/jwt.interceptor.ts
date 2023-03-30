import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(public jwtHelper: JwtHelperService) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        // Récupérez votre token JWT à partir du stockage local ou d'un autre service
        const token = localStorage.getItem('token');

        // Si le token existe, ajoutez-le à l'en-tête 'Authorization' de la requête
        if (token && !this.jwtHelper.isTokenExpired(token)) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`,
                },
            });
        }

        return next.handle(request);
    }
}
