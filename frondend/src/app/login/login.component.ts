// login.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
    email: string = '';
    password: string = '';

    constructor(private authService: AuthService, private router: Router) {}

    onSubmit() {
        this.authService.login({ email: this.email, password: this.password }).subscribe(
            () => {
                this.router.navigate(['/recipes']);
            },
            (error: any) => {
                console.log('Error:', error);
            }
        );
    }
}
