import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RecipesComponent } from './recipes/recipes.component';
import { RecipeDetailsComponent } from './recipe-details/recipe-details.component';
import { RecipeFormComponent } from './recipe-form/recipe-form.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'recipes', component: RecipesComponent, canActivate: [AuthGuard] },
    { path: 'recipes/edit/:id', component: RecipeFormComponent, canActivate: [AuthGuard] },
    { path: 'recipes/:id', component: RecipeDetailsComponent, canActivate: [AuthGuard] },
    { path: 'recipes/comment', component: RecipeFormComponent, canActivate: [AuthGuard] },
    { path: '', redirectTo: '/recipes/', pathMatch: 'prefix' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
