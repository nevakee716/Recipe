import { Component, OnInit, OnDestroy } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription, lastValueFrom } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { Role, User } from '../models/user';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
    selector: 'app-users-table',
    templateUrl: './users-table.component.html',
    styleUrls: ['./users-table.component.scss'],
})
export class UsersTableComponent implements OnInit, OnDestroy {
    private subscriptions: Subscription[] = [];
    displayedColumns = ['id', 'email', 'password', 'firstname', 'lastname', 'role', 'action'];
    users: User[] = [];
    dataSource = new MatTableDataSource<any>();
    isLoading = true;
    pageNumber: number = 1;
    VOForm: FormGroup | undefined;
    isEditableNew: boolean = true;
    roleEnum = Role;
    initialValues: any = [];

    constructor(
        private fb: FormBuilder,
        private _formBuilder: FormBuilder,
        private authService: AuthService,
        private _snackBar: MatSnackBar
    ) {}

    ngOnDestroy(): void {
        this.subscriptions.forEach((s) => s.unsubscribe);
    }

    ngOnInit(): void {
        this.subscriptions.push(
            this.authService.getUsers().subscribe((users) => {
                this.users = users;
                this.initForm();
            })
        );
    }

    getSnapShotForm() {
        let VOForminit = this.fb.group({
            VORows: this.fb.array(
                this.users.map((user) => {
                    return this.fb.group({
                        id: new FormControl(user.id),
                        email: new FormControl(user.email),
                        firstname: new FormControl(user.firstname),
                        lastname: new FormControl(user.lastname),
                        role: new FormControl(user.role),
                        action: new FormControl('existingRecord'),
                        password: new FormControl(user.password),
                        isEditable: new FormControl(true),
                    });
                })
            ), //end of fb array
        }); // end of form group cretation

        this.initialValues = VOForminit?.get('VORows') as FormArray;
    }
    initForm() {
        this.VOForm = this._formBuilder.group({
            VORows: this._formBuilder.array([]),
        });

        this.VOForm = this.fb.group({
            VORows: this.fb.array(
                this.users.map((user) => {
                    return this.fb.group({
                        id: new FormControl(user.id),
                        email: new FormControl(user.email),
                        firstname: new FormControl(user.firstname),
                        lastname: new FormControl(user.lastname),
                        role: new FormControl(user.role),
                        password: new FormControl(user.password),
                        action: new FormControl('existingRecord'),
                        isEditable: new FormControl(true),
                    });
                })
            ), //end of fb array
        }); // end of form group cretation

        this.isLoading = false;
        this.dataSource = new MatTableDataSource((this.VOForm.get('VORows') as FormArray).controls);

        const filterPredicate = this.dataSource.filterPredicate;
        this.dataSource.filterPredicate = (data: AbstractControl, filter) => {
            return filterPredicate.call(this.dataSource, data.value, filter);
        };

        this.getSnapShotForm();
    }

    userToForm(user: User) {
        return {
            id: new FormControl(user.id),
            email: new FormControl(user.email),
            firstname: new FormControl(user.firstname),
            lastname: new FormControl(user.lastname),
            role: new FormControl(user.role),
            password: new FormControl(undefined),
            action: new FormControl('existingRecord'),
            isEditable: new FormControl(true),
        };
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    // @ViewChild('table') table: MatTable<PeriodicElement>;
    AddNewRow() {
        this.users.push({
            id: 0,
            firstname: '',
            lastname: '',
            email: '',
            password: this.genPassword(),
            role: Role.USER,
            recipes: [],
            comments: [],
        });
        this.getSnapShotForm();
        const control = this.VOForm?.get('VORows') as FormArray;
        control.push(this.initiateVOForm());
        this.initialValues.push(this.initiateVOForm());

        this.dataSource = new MatTableDataSource(control.controls);
    }

    // On click of correct button in table (after click on edit) this method will call
    async SaveSVO(VOFormElement: any, i: any) {
        try {
            let editedUser: User = VOFormElement.get('VORows').at(i).value;
            const newUser = await lastValueFrom(this.authService.saveUser(editedUser));
            this.users[i] = newUser;
            VOFormElement.get('VORows').at(i).get('id').patchValue(newUser.id);
            VOFormElement.get('VORows').at(i).get('password').patchValue(undefined);
            this.getSnapShotForm();
            this.dataSource = new MatTableDataSource(VOFormElement.get('VORows').controls);
            this._snackBar.open('User was successfully saved', 'Close');
        } catch (e: any) {
            this._snackBar.open(`Issue when saving User : ${e?.error?.error}`, 'Close');
        }
    }

    async DeleteSVO(VOFormElement: any, i: any) {
        try {
            if (this.users[i].id !== 0) await lastValueFrom(this.authService.delete(this.users[i]));
            VOFormElement.get('VORows').removeAt(i);
            VOFormElement.updateValueAndValidity();
            this.initialValues.removeAt(i);
            this.users.splice(i, 1);
            this._snackBar.open(`User was successfully deleted`, 'Close');
            this.dataSource = new MatTableDataSource(VOFormElement.get('VORows').controls);
        } catch (e: any) {
            this._snackBar.open(`Issue when deleting User : ${e?.error?.error}`, 'Close');
        }
    }

    CancelSVO(VOFormElement: any, i: any) {
        VOFormElement.get('VORows').at(i).reset(this.initialValues.at(i).value);
    }

    genPassword(passwordLength = 12): string {
        var chars = '0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&*()ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var password = '';
        for (var i = 0; i <= passwordLength; i++) {
            var randomNumber = Math.floor(Math.random() * chars.length);
            password += chars.substring(randomNumber, randomNumber + 1);
        }
        return password;
    }

    initiateVOForm(): FormGroup {
        return this.fb.group({
            id: new FormControl(0),
            email: new FormControl(''),
            firstname: new FormControl(''),
            lastname: new FormControl(''),
            password: new FormControl(this.genPassword()),
            role: new FormControl(Role.USER),
            action: new FormControl('newRecord'),
            isNewRow: new FormControl(true),
        });
    }
}
