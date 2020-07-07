import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import {User} from "../../../models/user.model";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup
  responseErr = false
  invalidForm = false

  constructor(private fb: FormBuilder, private _auth: AuthService, private _router: Router) {
  }

  ngOnInit() {
    this.registerForm = this.fb.group({
      email: ['tomi.lekar@outlook.de', [Validators.required, Validators.email]],
      username: ['Testusername', [Validators.required]],
      password: ['Testpassword', [Validators.required]]
    })
  }

  // On register click event, function below is executed
  onSubmit() {
    console.log(this.registerForm)
    const user: User = this.registerForm.value;
    console.log(user);
    this._auth.registerUser(user);
    console.log('TEST')
    console.log(user);
  }


}
