import { Component, OnInit, HostListener } from '@angular/core';
import { FormBuilder, Validators, FormGroup, FormControl } from '@angular/forms';

import { ErrorService, Globals, ITokenRole } from 'projects/fast-code-core/src/public_api';

import { UserService, IUser } from 'src/app/admin/user-management/user/index';
import { first } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { OwnersService } from 'src/app/owners';
import { VetsService } from 'src/app/vets';
import { AuthenticationService } from 'src/app/core/authentication.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-update-profile',
	templateUrl: './update-profile.component.html',
	styleUrls: ['./update-profile.component.scss']
})
export class UpdateProfileComponent implements OnInit {

	title: string = 'Update Profile';
	userForm: FormGroup;
	user: any;
	submitted: boolean = false;
	loading: boolean = false;
	isOwner: boolean = false;
	isVet: boolean = false;
	role: ITokenRole;

	/** 
   * Guard against browser refresh, close, etc.
   * Checks if user has some unsaved changes 
   * before leaving the page.
   */
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.userForm.dirty && !this.submitted) {
      return false
    }
    return true;
  }
	constructor(
		public formBuilder: FormBuilder,
		public userService: UserService,
		public errorService: ErrorService,
		public ownersService: OwnersService,
		public vetsService: VetsService,
		public authenticationService: AuthenticationService,
		public router: Router,
	) { }

	ngOnInit() {
		this.role = this.authenticationService.decodeToken().role;
		this.isOwner = this.role == ITokenRole.owner;
		this.isVet = this.role == ITokenRole.vet;
		this.setForm();
		this.getItem();
	}

	setForm() {
		this.userForm = this.formBuilder.group({
			emailAddress: ['', Validators.required],
			firstName: ['', Validators.required],
			lastName: ['', Validators.required],
			phoneNumber: [''],
			userName: ['', Validators.required],
			address: [''],
			city: [''],
		});
		if( this.role == ITokenRole.owner){
			this.userForm.addControl("address", new FormControl(''));
			this.userForm.addControl("city", new FormControl(''));
		}
	}

	onItemFetched(item: IUser) {
		this.user = item;
		this.userForm.patchValue(item);
	}

	getItem() {
		let getProfileObs: Observable<any>; 
		if(this.isOwner){
			getProfileObs = this.ownersService.getProfile();
		}
		else if(this.isVet){
			getProfileObs = this.vetsService.getProfile();
		}
		else{
			getProfileObs = this.userService.getProfile();
		}
		getProfileObs.subscribe(x => this.onItemFetched(x), (error) => {
			this.errorService.showError('An error occured while fetching details');
		});
	}

	/**
	 * Gets data from item form and calls
	 * service method to update the item.
	 */
	onSubmit() {
		if (this.userForm.invalid) {
			return;
		}

		this.submitted = true;
		this.loading = true;
		let updateProfileObs: any; 
		if(this.isOwner){
			updateProfileObs = this.ownersService.updateProfile(this.userForm.getRawValue());
		}
		else if(this.isVet){
			updateProfileObs = this.vetsService.updateProfile(this.userForm.getRawValue());
		}
		else{
			updateProfileObs = this.userService.updateProfile(this.userForm.getRawValue());
		}
		updateProfileObs
			.pipe(first())
			.subscribe(
				data => {
					this.loading = false;
					console.log("profile updated")
					// this.router.navigate([this.parentUrl], { relativeTo: this.route.parent });
				},
				error => {
					this.errorService.showError("Error Occured while updating");
					this.loading = false;
				});
	}

	onBack(){
		this.router.navigate(['/dashboard']);
	}

}
