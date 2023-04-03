import { Component, SimpleChanges, OnDestroy, OnInit, EventEmitter, Input, Output, ElementRef, ViewChild } from '@angular/core';
import { RecipeService } from '../services/recipe.service';
import { Recipe } from '../models/recipe';
import { Observable } from 'rxjs/internal/Observable';
import { combineLatest, map, startWith } from 'rxjs';
import { UntypedFormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { lastValueFrom, Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { User, Role, Access } from '../models/user';
import { Keyword } from '../models/keyword';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatChipInputEvent } from '@angular/material/chips';

@Component({
    selector: 'app-keyword-filter',
    templateUrl: './keyword-filter.component.html',
    styleUrls: ['./keyword-filter.component.scss'],
})
export class KeywordFilterComponent {
    private subscriptions: Subscription[] = [];
    keywords: Keyword[] = [];
    separatorKeysCodes: number[] = [ENTER, COMMA];
    keywordsCtrl = new FormControl('');
    filteredKeywords: Observable<Keyword[]> | undefined;

    @Input() allKeywords: Keyword[] = [];
    @Output() keywordSelected = new EventEmitter<Keyword[]>();

    @ViewChild('keywordInput') keywordInput: ElementRef<HTMLInputElement> | undefined;

    constructor() {}

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['allKeywords']) {
            this.filteredKeywords = this.keywordsCtrl.valueChanges.pipe(
                startWith(null),
                map((keyword: string | null) => (keyword ? this._filter(keyword) : this.allKeywords.slice()))
            );
        }
    }
    private _filter(keyword: string): Keyword[] {
        const filterValue = keyword.toLowerCase();
        return this.allKeywords.filter((keyword) => keyword.name.toLowerCase().includes(filterValue));
    }

    add(event: MatChipInputEvent): void {
        const value = (event.value || '').trim();

        if (value) {
            let foundKeyword = this.allKeywords.find((k) => k.name === value);
            if (foundKeyword) this.keywords.push(foundKeyword);
        }
        // Clear the input value
        event.chipInput!.clear();
        this.keywordsCtrl.setValue(null);
        this.keywordSelected.emit(this.keywords);
    }

    remove(keywordName: string): void {
        const index = this.keywords.findIndex((k) => k.name === keywordName);
        if (index >= 0) {
            this.keywords.splice(index, 1);
        }
        this.keywordSelected.emit(this.keywords);
    }

    selected(event: MatAutocompleteSelectedEvent): void {
        let foundKeyword = this.allKeywords.find((k) => k.name === event.option.viewValue);
        if (foundKeyword) this.keywords.push(foundKeyword);
        if (this.keywordInput) this.keywordInput.nativeElement.value = '';
        this.keywordsCtrl.setValue(null);
        this.keywordSelected.emit(this.keywords);
    }
}
