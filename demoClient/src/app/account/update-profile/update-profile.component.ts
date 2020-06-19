import { Component, OnInit, HostListener } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';

import { ErrorService, Globals } from 'projects/fast-code-core/src/public_api';

import { UserService, IUser } from 'src/app/admin/user-management/user/index';
import { first } from 'rxjs/operators';
import { Observable } from 'rxjs';

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
	) { }

	ngOnInit() {
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
			githubUsername: [''],
			gitlabUsername: [''],
		});
	}

	onItemFetched(item: IUser) {
		this.user = item;
		this.userForm.patchValue(item);
	}

	getItem() {
		this.userService.getProfile().subscribe(x => this.onItemFetched(x), (error) => {
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
		this.userService.updateProfile(this.userForm.getRawValue())
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

}
