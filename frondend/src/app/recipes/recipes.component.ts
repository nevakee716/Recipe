import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { combineLatest, map, BehaviorSubject } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { lastValueFrom } from 'rxjs';
@Component({
    selector: 'app-recipes',
    templateUrl: './recipes.component.html',
    styleUrls: ['./recipes.component.scss'],
})
export class RecipesComponent implements OnInit {
    private _recipesSubject = new BehaviorSubject<Recipe[]>([]);
    recipes$: Observable<Recipe[]> | undefined;
    filteredSearch$: Observable<Recipe[]> | undefined;
    searchText$: Observable<string> | undefined;
    searchFormControl = new UntypedFormControl();
    constructor(private recipeService: RecipeService, private _snackBar: MatSnackBar) {
        this.searchFormControl = new UntypedFormControl();
    }

    ngOnInit(): void {
        this.fetchRecipes();
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
    private async fetchRecipes(): Promise<void> {
        try {
            this.recipes$ = this.recipeService.getRecipes();

            this.searchText$ = this.searchFormControl.valueChanges;
            const combine$ = combineLatest([this.recipes$, this.searchText$]);
            this.filteredSearch$ = combine$.pipe(
                map(([recipes, searchText]) => {
                    console.log('combine');
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
}
