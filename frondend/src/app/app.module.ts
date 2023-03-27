import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RecipesComponent } from './recipes/recipes.component';
import { RecipeDetailsComponent } from './recipe-details/recipe-details.component';
import { RecipeFormComponent } from './recipe-form/recipe-form.component';
import { CommentFormComponent } from './comment-form/comment-form.component';
import { DemoMaterialModule } from './material-module';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    declarations: [AppComponent, RecipesComponent, RecipeDetailsComponent, RecipeFormComponent, CommentFormComponent],
    imports: [BrowserModule, HttpClientModule, AppRoutingModule, BrowserAnimationsModule, DemoMaterialModule],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
