import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recipe } from '../models/recipe';
import { RecipeFormRequest } from '../models/recipe-form-request';
import { Ingredient } from '../models/ingredient';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class RecipeService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    getRecipes(): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(this.apiUrl);
    }

    getIngredients(): Observable<Ingredient[]> {
        return this.http.get<Ingredient[]>(`${this.apiUrl}/ingredients`);
    }

    getRecipe(id: number): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(`${this.apiUrl}/${id}`);
    }

    createRecipe(recipe: Recipe): Observable<Recipe> {
        return this.http.post<Recipe>(this.apiUrl, recipe);
    }

    updateRecipe(id: number, recipeFormRequest: RecipeFormRequest): Observable<Recipe> {
        return this.http.put<Recipe>(`${this.apiUrl}/${id}`, recipeFormRequest);
    }

    deleteRecipe(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
