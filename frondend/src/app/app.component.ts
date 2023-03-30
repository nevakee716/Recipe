import { Component } from '@angular/core';
import { User } from './models/user';

import { AuthService } from './services/auth.service';
import { lastValueFrom } from 'rxjs';
@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent {
    title = 'recipe';
    user: User | undefined;

    constructor(private authService: AuthService) {}

    async ngOnInit(): Promise<void> {
        this.user = await lastValueFrom(this.authService.getUserDetail());
        this.user.username = this.user.firstname ? this.user.firstname + ' ' + this.user.lastname : this.user.email;
    }
}
