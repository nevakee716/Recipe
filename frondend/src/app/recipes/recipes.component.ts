import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { combineLatest, map } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
@Component({
    selector: 'app-recipes',
    templateUrl: './recipes.component.html',
    styleUrls: ['./recipes.component.scss'],
})
export class RecipesComponent implements OnInit {
    recipes$: Observable<Recipe[]> | undefined;
    filteredSearch$: Observable<Recipe[]> | undefined;
    searchText$: Observable<string> | undefined;
    searchText: string = '';
    searchFormControl = new UntypedFormControl();
    constructor(private recipeService: RecipeService) {
        this.searchFormControl = new UntypedFormControl();
    }

    ngOnInit(): void {
        this.recipes$ = this.recipeService.getRecipes();
        this.searchText$ = this.searchFormControl.valueChanges;

        this.filteredSearch$ = combineLatest([this.recipes$, this.searchText$]).pipe(
            map(([recipes, searchText]) => recipes.filter((recipe) => recipe.name.toLowerCase().includes(searchText.toLowerCase())))
        );
    }

    ngAfterViewInit(): void {
        // init form value
        this.searchFormControl.setValue('');
    }
}
