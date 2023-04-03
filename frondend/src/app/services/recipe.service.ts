import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Recipe } from '../models/recipe';
import { RecipeFormRequest } from '../models/recipe-form-request';
import { Ingredient } from '../models/ingredient';
import { environment } from '../../environments/environment';
import { switchMap, tap } from 'rxjs/operators';
import { User, Role, RecipeAccess } from '../models/user';

@Injectable({
    providedIn: 'root',
})
export class RecipeService {
    private apiUrl = environment.apiUrl;
    private readonly getRecipesSubject = new Subject<void>();
    constructor(private http: HttpClient) {}

    getRecipes(): Observable<Recipe[]> {
        return this.getRecipesSubject.pipe(
            switchMap(() => this.http.get<Recipe[]>(this.apiUrl)),
            tap(() => console.log('GET request triggered'))
        );
    }
    triggerGetRecipe(): void {
        this.getRecipesSubject.next();
    }

    getIngredients(): Observable<Ingredient[]> {
        return this.http.get<Ingredient[]>(`${this.apiUrl}/ingredients`);
    }

    getRecipe(id: number): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(`${this.apiUrl}/${id}`);
    }

    createRecipe(recipeFormRequest: RecipeFormRequest): Observable<Recipe> {
        return this.http.post<Recipe>(`${this.apiUrl}/create`, recipeFormRequest);
    }

    updateRecipe(id: number, recipeFormRequest: RecipeFormRequest): Observable<Recipe> {
        return this.http.put<Recipe>(`${this.apiUrl}/${id}`, recipeFormRequest);
    }

    deleteRecipe(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    checkRecipeAccessRight(recipe: Recipe, user: User | undefined): RecipeAccess {
        if (!user) return RecipeAccess.READ;
        if (user.role === Role.ADMIN) return RecipeAccess.EDIT;
        if (recipe?.creator?.id === user.id) return RecipeAccess.EDIT;
        return RecipeAccess.READ;
    }
}
