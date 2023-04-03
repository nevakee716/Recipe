import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { Subscription, lastValueFrom } from 'rxjs';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { Ingredient } from '../models/ingredient';
import { QuantityIngredient } from '../models/quantity-ingredient';
import { Recipe } from '../models/recipe';
import { RecipeFormRequest } from '../models/recipe-form-request';
import { Keyword } from '../models/keyword';
import { RecipeService } from '../services/recipe.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { KeywordFilterComponent } from '../keyword-filter/keyword-filter.component';

@Component({
    selector: 'app-recipe-form',
    templateUrl: './recipe-form.component.html',
    styleUrls: ['./recipe-form.component.scss'],
})
export class RecipeFormComponent implements OnInit {
    private subscriptions: Subscription[] = [];
    recipes$: Observable<Recipe[]> | undefined;
    keywords$: Observable<Keyword[]> | undefined;
    ingredient$: Observable<Ingredient[]> | undefined;

    recipeId: number = 0;
    recipeForm: FormGroup = new FormGroup({
        name: new FormControl(null, Validators.required),
        description: new FormControl(null, Validators.required),
        imageUrl: new FormControl(null),
        instructions: new FormControl(null, Validators.required),
        ingredients: new FormArray([]),
        keywords: new FormArray([]),
    });

    ingredients: Ingredient[] = [];
    keywords: Keyword[] = [];

    constructor(
        private router: Router,
        private _snackBar: MatSnackBar,
        public route: ActivatedRoute,
        private recipeService: RecipeService
    ) {}

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnInit(): void {
        const urlParam$ = this.route.paramMap;
        this.recipes$ = urlParam$.pipe(
            switchMap((params) => {
                this.recipeId = Number(params.get('id'));
                return this.recipeService.getRecipe(this.recipeId);
            })
        );
        this.ingredient$ = this.recipeService.getIngredients();

        this.keywords$ = this.recipeService.getKeywords();
        this.refreshKeywordAndIngredients();

        this.subscriptions.push(this.ingredient$.subscribe((ingredients) => (this.ingredients = ingredients)));
        this.subscriptions.push(this.keywords$.subscribe((keywords) => (this.keywords = keywords)));

        this.subscriptions.push(
            this.recipes$.subscribe((recipes) => {
                if (this.recipeId !== 0) {
                    this.initForm(recipes[0]);
                }
            })
        );
    }

    initForm(recipe: Recipe): void {
        this.recipeForm = new FormGroup({
            name: new FormControl(recipe.name ?? 'New Recipe', Validators.required),
            description: new FormControl(recipe.description, Validators.required),
            imageUrl: new FormControl(recipe.imageUrl),
            instructions: new FormControl(recipe.instructions, Validators.required),
            ingredients: new FormArray([]),
            keywords: new FormArray([]),
        });
        for (const keyword of recipe.keywordList || []) {
            const keywordGroup = new FormGroup({
                keyword: new FormControl(keyword.name, Validators.required),
            });
            (<FormArray>this.recipeForm?.get('keywords')).push(keywordGroup);
        }
        for (const ingredientQuantity of recipe.ingredientsQuantity || []) {
            const ingredientGroup = new FormGroup({
                ingredient: new FormControl(ingredientQuantity.ingredient.name, Validators.required),
                quantity: new FormControl(ingredientQuantity.quantity, Validators.required),
            });
            (<FormArray>this.recipeForm?.get('ingredients')).push(ingredientGroup);
        }
    }

    async refreshKeywordAndIngredients(): Promise<void> {
        if (this.ingredient$) this.ingredients = await lastValueFrom(this.ingredient$);
        if (this.keywords$) this.keywords = await lastValueFrom(this.keywords$);
    }

    async onSubmit() {
        const newRecipe = this.recipeForm?.value;

        const recipeForm: RecipeFormRequest = {
            recipe: newRecipe,
            quantityIngredients: this.recipeForm?.value.ingredients.map((i: any) => {
                let ingredient: Ingredient | undefined = this.ingredients.find(
                    (ingredient: Ingredient) => ingredient.name === i.ingredient
                );
                if (ingredient === undefined) ingredient = { id: 0, name: i.ingredient };

                let r: QuantityIngredient = {
                    ingredient: ingredient,
                    quantity: i.quantity,
                };
                return r;
            }),
            keywords: this.recipeForm?.value.keywords.map((i: any) => {
                let k: Keyword | undefined = this.keywords.find((k: Keyword) => k.name === i.keyword);
                if (k === undefined) k = { id: 0, name: i.keyword };
                return k;
            }),
        };
        let answered: Recipe;
        try {
            if (this.recipeId !== 0) {
                answered = await lastValueFrom(this.recipeService.updateRecipe(this.recipeId, recipeForm));
                this._snackBar.open('Recipe Successfully Edited', 'Close');
            } else {
                answered = await lastValueFrom(this.recipeService.createRecipe(recipeForm));
                this._snackBar.open('Recipe Successfully Created', 'Close');
                this.router.navigate(['/', 'app', 'recipes', 'edit', answered.id]);
            }
        } catch (e: any) {
            this._snackBar.open(`Issue when submitting the recipe : ${e?.error?.error}`, 'Close');
        }
        this.refreshKeywordAndIngredients();
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

    onAddKeyword() {
        const keywordGroup = new FormGroup({
            keyword: new FormControl(null, Validators.required),
        });

        (<FormArray>this.recipeForm?.get('keywords')).push(keywordGroup);
    }

    onRemoveKeyword(index: number) {
        (<FormArray>this.recipeForm?.get('keywords')).removeAt(index);
    }

    getControls(item: string) {
        return (this.recipeForm.get(item) as FormArray).controls;
    }

    async deleteRecipe() {
        try {
            if (this.recipeId != 0) {
                await lastValueFrom(this.recipeService.deleteRecipe(this.recipeId));
                this._snackBar.open('Recipe Successfully Deleted', 'Close');
            }
            this.router.navigate(['/app/recipes']);
        } catch (e: any) {
            this._snackBar.open(`Issue when deleting recipe : ${e?.error?.error}`, 'Close');
        }
    }
}
