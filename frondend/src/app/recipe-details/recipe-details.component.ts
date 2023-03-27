import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap, map } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-recipe-details',
    templateUrl: './recipe-details.component.html',
    styleUrls: ['./recipe-details.component.scss'],
})
export class RecipeDetailsComponent implements OnInit {
    recipes$: Observable<Recipe[]> | undefined;
    recipeId: number = 0;
    constructor(public route: ActivatedRoute, private recipeService: RecipeService) {}

    ngOnInit(): void {
        const urlParam$ = this.route.paramMap;
        this.recipes$ = urlParam$.pipe(
            switchMap((params) => {
                this.recipeId = Number(params.get('id'));
                return this.recipeService.getRecipe(this.recipeId);
            })
        );
    }
}
