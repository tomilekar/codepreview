import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/services/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup
  responseError = false
  invalidForm = false

  constructor(private _auth: AuthService, private _router: Router, private fb: FormBuilder) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['tomi.lekar@outlook.de', [Validators.required, Validators.email]] ,
      password: ['Testpassword', [Validators.required, Validators.minLength(6)]]

    })
  }

  onSubmit() {
    console.log(this.loginForm)
    const loginData = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }
    if (this.loginForm.valid) {
      this._auth.loginUser(loginData);

    }
  }

}
