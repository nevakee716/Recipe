// login.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormControl } from '@angular/forms';
import { lastValueFrom } from 'rxjs';
@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
    form: FormGroup = new FormGroup({
        email: new FormControl(''),
        password: new FormControl(''),
    });
    error: any = undefined;
    constructor(private authService: AuthService, private router: Router) {}

    async submit() {
        if (this.form.valid) {
            try {
                await lastValueFrom(this.authService.login({ email: this.form.value.email, password: this.form.value.password }));
                this.router.navigate(['/recipes']);
            } catch (error) {
                this.error = error;
                console.log('Error:', error);
            }
        }
    }
}
