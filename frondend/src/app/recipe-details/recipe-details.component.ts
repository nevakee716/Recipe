import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap, map } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User, Role } from '../models/user';
import { AuthService } from '../services/auth.service';
import { lastValueFrom } from 'rxjs';

@Component({
    selector: 'app-recipe-details',
    templateUrl: './recipe-details.component.html',
    styleUrls: ['./recipe-details.component.scss'],
})
export class RecipeDetailsComponent implements OnInit {
    recipes$: Observable<Recipe[]> | undefined;
    recipeId: number = 0;
    user: User | undefined;
    roleEnum: any = Role;
    constructor(private authService: AuthService, public route: ActivatedRoute, private recipeService: RecipeService) {}

    async ngOnInit(): Promise<void> {
        this.user = await lastValueFrom(this.authService.getUserDetail());
        const urlParam$ = this.route.paramMap;
        this.recipes$ = urlParam$.pipe(
            switchMap((params) => {
                this.recipeId = Number(params.get('id'));
                return this.recipeService.getRecipe(this.recipeId);
            })
        );
    }
}
