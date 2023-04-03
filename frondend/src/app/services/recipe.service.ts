import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Recipe } from '../models/recipe';
import { RecipeFormRequest } from '../models/recipe-form-request';
import { Ingredient } from '../models/ingredient';
import { environment } from '../../environments/environment';
import { switchMap, tap } from 'rxjs/operators';
import { User, Role, Access } from '../models/user';
import { Comment } from '../models/comment';
import { Keyword } from '../models/keyword';

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

    getKeywords(): Observable<Keyword[]> {
        return this.http.get<Keyword[]>(`${this.apiUrl}/keywords`);
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

    addComment(id: number, comment: any): Observable<Comment> {
        return this.http.post<Comment>(`${this.apiUrl}/${id}/comment`, comment);
    }
    deleteComment(id: number, recipe: Recipe): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/comment/${id}`, recipe);
    }
    checkRecipeAccessRight(recipe: Recipe, user: User | undefined): Access {
        if (!user) return Access.READ;
        if (user.role === Role.ADMIN) return Access.EDIT;
        if (recipe?.creator?.id === user.id) return Access.EDIT;
        return Access.READ;
    }

    checkCommentAccessRight(comment: Comment, user: User | undefined): Access {
        if (!user) return Access.READ;
        if (user.role === Role.ADMIN) return Access.EDIT;
        if (comment?.creator?.id === user.id) return Access.EDIT;
        return Access.READ;
    }
}
