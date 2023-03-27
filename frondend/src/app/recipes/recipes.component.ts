import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';

@Component({
    selector: 'app-recipes',
    templateUrl: './recipes.component.html',
    styleUrls: ['./recipes.component.scss'],
})
export class RecipesComponent implements OnInit {
    recipes: Observable<Recipe[]> | undefined;

    constructor(private recipeService: RecipeService) {}

    ngOnInit(): void {
        this.getRecipes();
    }

    getRecipes(): void {
        this.recipes = this.recipeService.getRecipes();
    }
}
