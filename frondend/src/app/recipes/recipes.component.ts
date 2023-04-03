import { Component, OnDestroy, OnInit, ElementRef, ViewChild } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { combineLatestWith, map, fromEvent, startWith } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { lastValueFrom, Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { User, Role, Access } from '../models/user';
import { Keyword } from '../models/keyword';

@Component({
    selector: 'app-recipes',
    templateUrl: './recipes.component.html',
    styleUrls: ['./recipes.component.scss'],
})
export class RecipesComponent implements OnInit, OnDestroy {
    private subscriptions: Subscription[] = [];
    recipes$: Observable<Recipe[]> | undefined;
    recipes: Recipe[] = [];
    keywords$: any;
    filteredSearch$: Observable<Recipe[]> | undefined;
    searchText$: Observable<string> | undefined;
    searchText: string = '';
    searchFormControl = new UntypedFormControl();
    user: User | undefined;
    roleEnum: any = Role;
    allKeywords: Keyword[] = [];
    selectedKeywords: Keyword[] = [];
    @ViewChild('keywordFilter') keywordInput: ElementRef<HTMLInputElement> | undefined;

    constructor(private recipeService: RecipeService, private authService: AuthService, private _snackBar: MatSnackBar) {
        this.searchFormControl = new UntypedFormControl();
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnInit(): void {
        this.fetchUserInfo();
        this.fetchKeywords();
        this.fetchRecipes();
        this.fetchSearchText();
    }

    ngAfterViewInit(): void {
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

    keywordSelected(selectedKeywords: any): void {
        this.selectedKeywords = selectedKeywords;
        console.table(this.selectedKeywords);
    }

    private fetchKeywords(): void {
        this.subscriptions.push(
            this.recipeService.getKeywords().subscribe((keywords) => {
                this.allKeywords = keywords;
            })
        );
    }

    private fetchUserInfo(): void {
        this.subscriptions.push(
            this.authService.getUserDetail().subscribe((user) => {
                this.user = user;
            })
        );
        this.authService.triggerRefreshUserInfo();
    }

    private fetchRecipes(): void {
        try {
            this.recipes$ = this.recipeService.getRecipes();
            this.recipes$.subscribe((recipes: Recipe[]) => {
                this.recipes = recipes;
            });
            this.recipeService.triggerGetRecipe();
        } catch (error) {
            console.error('Error fetching recipes:', error);
        }
    }

    private fetchSearchText(): void {
        this.searchText$ = this.searchFormControl.valueChanges;
        this.searchText$.subscribe((searchText: string) => {
            this.searchText = searchText;
        });
    }

    public getFilteredRecipes(): Recipe[] {
        return this.recipes.filter((recipe: Recipe) => {
            //const keywordCheck = recipe.keywordList.find((k) => this.selectedKeywords.find((sk) => k.name == sk.name));
            const keywordCheck = this.selectedKeywords.every((sk) => recipe.keywordList.find((k) => k.name == sk.name));
            return (
                (this.selectedKeywords.length == 0 || keywordCheck) &&
                (this.searchText == '' || recipe.name.toLowerCase().includes(this.searchText.toLowerCase()))
            );
        });
    }

    canEditRecipe(recipe: Recipe): boolean {
        return Access.EDIT === this.recipeService.checkRecipeAccessRight(recipe, this.user);
    }
}
