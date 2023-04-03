import { NgZone, Component, OnDestroy, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { combineLatest, map } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { lastValueFrom, Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { User, Role, RecipeAccess } from '../models/user';

@Component({
    selector: 'app-recipes',
    templateUrl: './recipes.component.html',
    styleUrls: ['./recipes.component.scss'],
})
export class RecipesComponent implements OnInit, OnDestroy {
    private subscriptions: Subscription[] = [];
    recipes$: Observable<Recipe[]> | undefined;
    filteredSearch$: Observable<Recipe[]> | undefined;
    searchText$: Observable<string> | undefined;
    searchFormControl = new UntypedFormControl();
    user: User | undefined;
    roleEnum: any = Role;

    constructor(
        private _ngZone: NgZone,
        private recipeService: RecipeService,
        private authService: AuthService,
        private _snackBar: MatSnackBar
    ) {
        this.searchFormControl = new UntypedFormControl();
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnInit(): void {
        this.fetchRecipes();
        this.fetchUserInfo();
    }

    async deleteRecipe(id: number) {
        try {
            await lastValueFrom(this.recipeService.deleteRecipe(id));
            this._snackBar.open('Recipe Successfully Deleted', 'Close');
            this.recipeService.triggerGetRecipe();
        } catch (e: any) {
            this._snackBar.open(`Issue when deleting recipe : ${e?.error?.error}`, 'Close');
        }
    }

    private fetchUserInfo(): void {
        this.subscriptions.push(
            this.authService.getUserDetail().subscribe((user) => {
                this.user = user;
            })
        );
        this.authService.triggerRefreshUserInfo();
    }

    private async fetchRecipes(): Promise<void> {
        try {
            this.recipes$ = this.recipeService.getRecipes();

            this.searchText$ = this.searchFormControl.valueChanges;
            const combine$ = combineLatest([this.recipes$, this.searchText$]);
            this.filteredSearch$ = combine$.pipe(
                map(([recipes, searchText]) => {
                    return recipes.filter((recipe) => recipe.name.toLowerCase().includes(searchText.toLowerCase()));
                })
            );
            // triger the combinelatest
            setTimeout(() => {
                this.recipeService.triggerGetRecipe();
                this.searchFormControl.setValue('');
            });
        } catch (error) {
            console.error('Error fetching recipes:', error);
        }
    }

    canEditRecipe(recipe: Recipe): boolean {
        return RecipeAccess.EDIT === this.recipeService.checkRecipeAccessRight(recipe, this.user);
    }
}
