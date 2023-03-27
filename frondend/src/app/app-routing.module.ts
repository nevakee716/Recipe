import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RecipesComponent } from './recipes/recipes.component';
import { RecipeDetailsComponent } from './recipe-details/recipe-details.component';
import { RecipeFormComponent } from './recipe-form/recipe-form.component';

const routes: Routes = [
    { path: 'recipes', component: RecipesComponent },
    { path: 'recipes/:id', component: RecipeDetailsComponent },
    { path: 'recipes/edit/:id', component: RecipeFormComponent },
    { path: 'recipes/comment', component: RecipeFormComponent },
    { path: '', redirectTo: '/recipes/', pathMatch: 'prefix' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
