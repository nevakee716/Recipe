import { NgModule } from '@angular/core';
import { CdkTableModule } from '@angular/cdk/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatMenuModule } from '@angular/material/menu';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
@NgModule({
    exports: [
        CdkTableModule,
        MatDividerModule,
        MatExpansionModule,
        MatIconModule,
        MatNativeDateModule,
        MatRippleModule,
        MatToolbarModule,
        FormsModule,
        MatCardModule,
        MatAutocompleteModule,
        MatInputModule,
        MatButtonModule,
        ReactiveFormsModule,
        MatListModule,
        MatSnackBarModule,
        MatMenuModule,
        MatTableModule,
        MatProgressSpinnerModule,
        MatFormFieldModule,
        MatSelectModule,
        MatTooltipModule,
        MatChipsModule,
    ],
})
export class DemoMaterialModule {}
// tslint:disable: jsdoc-format

/**  Copyright 2019 Google Inc. All Rights Reserved.
    Use of this source code is governed by an MIT-style license that
    can be found in the LICENSE file at http://angular.io/license */
