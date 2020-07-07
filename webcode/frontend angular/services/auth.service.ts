import {Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import {User} from "../models/user.model";
import {Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService implements CanActivate, CanActivateChild{

  private _registerUrl = 'http://localhost:3000/users/register';
  private _loginUrl = 'http://localhost:3000/users/login';
  private JWT_AUTHORIZATION = "JWT_AUTHORIZATION";

  private login: Subject<any> = new Subject<any>();
  private register: Subject<any> = new Subject<any>();
  constructor(private http: HttpClient, private _router: Router) {
  }

  registerUser(user: User):void {
    if(!user){return;}
    this.http.post(this._registerUrl,user).subscribe((x)=> {
      console.log(x);
    })
  }

  loginUser(user) {
    return this.http.post<any>(this._loginUrl, user, {observe: "body"})
      .subscribe((response: {token: string, id: string}) => {
        localStorage.setItem(this.JWT_AUTHORIZATION,response.token);
        localStorage.setItem('id',response.id );
      });
  }

  loggedIn() {
    return !!localStorage.getItem('token');
  }

  logoutUser() {
    localStorage.removeItem('token');
    this._router.navigate(['/']);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return undefined;
  }

  getUserLoginObserv = () => {
    return this.login.asObservable();
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return undefined;
  }




}
