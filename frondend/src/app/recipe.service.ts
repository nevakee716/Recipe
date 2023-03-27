import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Recipe } from './models/recipe';
import { environment } from './../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class RecipeService {
    private apiUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    getRecipes(): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(this.apiUrl);
    }

    getRecipe(id: number): Observable<Recipe[]> {
        return this.http.get<Recipe[]>(`${this.apiUrl}/${id}`);
    }

    createRecipe(recipe: Recipe): Observable<Recipe> {
        return this.http.post<Recipe>(this.apiUrl, recipe);
    }

    updateRecipe(id: number, recipe: Recipe): Observable<Recipe> {
        return this.http.put<Recipe>(`${this.apiUrl}/${id}`, recipe);
    }

    deleteRecipe(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
