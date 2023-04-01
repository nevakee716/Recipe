import { Component } from '@angular/core';
import { User } from '../models/user';
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
    private subscriptions: Subscription[] = [];
    constructor(private authService: AuthService) {}

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnInit(): void {
        this.user$ = this.authService.getUserDetail();
        setTimeout(() => {
            this.refresh();
        });
    }

    ngAfterContentChecked(): void {
        this.user$ ?? this.authService.getUserDetail();
    }

    logout(): void {
        this.authService.logout();
        this.user$ = undefined;
    }

    refresh(): void {
        this.authService.triggerRefreshUserInfo();
    }
}
