import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap, map } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User, Role } from '../models/user';
import { AuthService } from '../services/auth.service';
import { lastValueFrom, Subscription } from 'rxjs';

@Component({
    selector: 'app-recipe-details',
    templateUrl: './recipe-details.component.html',
    styleUrls: ['./recipe-details.component.scss'],
})
export class RecipeDetailsComponent implements OnInit {
    private subscriptions: Subscription[] = [];
    recipes$: Observable<Recipe[]> | undefined;
    recipe: Recipe | undefined;
    recipeId: number = 0;
    user: User | undefined;
    user$: Observable<User> | undefined;
    roleEnum: any = Role;
    constructor(private authService: AuthService, public route: ActivatedRoute, private recipeService: RecipeService) {}

    ngOnDestroy(): void {
        // tslint:disable-next-line: deprecation
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    async ngOnInit(): Promise<void> {
        const urlParam$ = this.route.paramMap;
        this.subscriptions.push(
            urlParam$
                .pipe(
                    switchMap((params) => {
                        this.recipeId = Number(params.get('id'));
                        return this.recipeService.getRecipe(this.recipeId);
                    })
                )
                .subscribe((recipes) => {
                    this.recipe = recipes[0];
                })
        );

        this.subscriptions.push(
            this.authService.getUserDetail().subscribe((user) => {
                this.user = user;
            })
        );
    }
}
