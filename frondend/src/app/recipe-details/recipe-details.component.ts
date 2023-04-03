import { Component, OnInit } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { User, Role, Access } from '../models/user';
import { AuthService } from '../services/auth.service';
import { Subscription, lastValueFrom } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Comment } from '../models/comment';

@Component({
    selector: 'app-recipe-details',
    templateUrl: './recipe-details.component.html',
    styleUrls: ['./recipe-details.component.scss'],
})
export class RecipeDetailsComponent implements OnInit {
    private subscriptions: Subscription[] = [];
    recipes$: Observable<Recipe[]> | undefined;
    recipe: Recipe | undefined;
    recipeId: number = 0;
    user: User | undefined;
    user$: Observable<User> | undefined;
    roleEnum: any = Role;

    commentForm = this.formBuilder.group({
        title: '',
        content: '',
    });

    constructor(
        private _snackBar: MatSnackBar,
        private authService: AuthService,
        public route: ActivatedRoute,
        private recipeService: RecipeService,
        private router: Router,
        private formBuilder: FormBuilder
    ) {}

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    async ngOnInit(): Promise<void> {
        const urlParam$ = this.route.paramMap;
        this.subscriptions.push(
            urlParam$
                .pipe(
                    switchMap((params) => {
                        this.recipeId = Number(params.get('id'));
                        return this.recipeService.getRecipe(this.recipeId);
                    })
                )
                .subscribe((recipes) => {
                    this.recipe = recipes[0];
                })
        );

        this.subscriptions.push(
            this.authService.getUserDetail().subscribe((user) => {
                this.user = user;
            })
        );
        this.authService.triggerRefreshUserInfo();
    }
    canEditRecipe(recipe: Recipe): boolean {
        return Access.EDIT === this.recipeService.checkRecipeAccessRight(recipe, this.user);
    }

    canEditComment(comment: Comment): boolean {
        return Access.EDIT === this.recipeService.checkCommentAccessRight(comment, this.user);
    }

    async deleteComment(id: number, index: number) {
        try {
            if (this.recipe) {
                await lastValueFrom(this.recipeService.deleteComment(id, this.recipe));

                this.recipe?.commentList.splice(index, 1);
                this._snackBar.open('Comment Successfully Deleted', 'Close');
            }
        } catch (e: any) {
            this._snackBar.open(`Issue when deleting recipe : ${e?.error?.error}`, 'Close');
        }
    }

    async deleteRecipe(id: number) {
        try {
            await lastValueFrom(this.recipeService.deleteRecipe(id));
            this._snackBar.open('Recipe Successfully Deleted', 'Close');
            this.router.navigate(['/app/recipes']);
        } catch (e: any) {
            this._snackBar.open(`Issue when deleting recipe : ${e?.error?.error}`, 'Close');
        }
    }

    async onSubmit() {
        if (this.commentForm.valid) {
            try {
                const newComment: Comment = await lastValueFrom(this.recipeService.addComment(this.recipeId, this.commentForm.value));
                this.recipe?.commentList.push(newComment);
                this.commentForm.reset();
                this._snackBar.open('Comment Successfully Posted', 'Close');
            } catch (e: any) {
                this._snackBar.open(`Issue when submitting comment : ${e?.error?.error}`, 'Close');
            }
        }
    }
}
