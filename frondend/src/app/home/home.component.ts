import { Component } from '@angular/core';
import { User, Role } from '../models/user';
import { map, Observable, Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
    title = 'recipe';
    user$: Observable<User> | undefined;
    user: User | undefined;
    roleEnum = Role;
    constructor(private authService: AuthService) {}

    ngOnInit(): void {
        this.user$ = this.authService.getUserDetail();
        this.authService.triggerRefreshUserInfo();
    }

    logout(): void {
        this.authService.logout();
        this.user$ = undefined;
    }
}
