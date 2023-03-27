import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl, FormArray } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { Recipe } from '../models/recipe';
import { RecipeService } from '../services/recipe.service';

@Component({
    selector: 'app-recipe-form',
    templateUrl: './recipe-form.component.html',
    styleUrls: ['./recipe-form.component.scss'],
})
export class RecipeFormComponent implements OnInit {
    recipes$: Observable<Recipe[]> | undefined;
    recipeId: number = 0;
    recipeForm: FormGroup = new FormGroup({
        name: new FormControl(null, Validators.required),
        description: new FormControl(null, Validators.required),
        imageUrl: new FormControl(null),
        instructions: new FormControl(null, Validators.required),
        ingredients: new FormArray([]),
    });

    constructor(public route: ActivatedRoute, private recipeService: RecipeService) {}

    ngOnInit(): void {
        const urlParam$ = this.route.paramMap;
        this.recipes$ = urlParam$.pipe(
            switchMap((params) => {
                this.recipeId = Number(params.get('id'));
                return this.recipeService.getRecipe(this.recipeId);
            })
        );

        this.recipes$.subscribe((recipes) => {
            let recipe = recipes[0];
            this.recipeForm = new FormGroup({
                name: new FormControl(recipe.name, Validators.required),
                description: new FormControl(recipe.description, Validators.required),
                imageUrl: new FormControl(recipe.imageUrl),
                instructions: new FormControl(recipe.instructions, Validators.required),
                ingredients: new FormArray([]),
            });
            console.log(recipe.ingredients);
            for (const [ingredient, quantity] of Object.entries(recipe.ingredients)) {
                const ingredientGroup = new FormGroup({
                    ingredient: new FormControl(ingredient, Validators.required),
                    quantity: new FormControl(quantity, Validators.required),
                });
                (<FormArray>this.recipeForm?.get('ingredients')).push(ingredientGroup);
            }
        });
    }

    onSubmit() {
        console.log(this.recipeForm?.value);
    }

    onAddIngredient() {
        const ingredientGroup = new FormGroup({
            ingredient: new FormControl(null, Validators.required),
            quantity: new FormControl(null, [Validators.required]),
        });

        (<FormArray>this.recipeForm?.get('ingredients')).push(ingredientGroup);
    }

    onRemoveIngredient(index: number) {
        (<FormArray>this.recipeForm?.get('ingredients')).removeAt(index);
    }

    getControls() {
        return (this.recipeForm.get('ingredients') as FormArray).controls;
    }
}
