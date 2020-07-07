import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, tap} from "rxjs/operators";
@Injectable()
export  class TokenInterceptor implements HttpInterceptor{
  prefix: string = "Bearer ";
  TOKEN: string = "JWT_AUTHORIZATION";
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('==TOKEN INTERECPTOR===');
    let request: HttpRequest<any> = req.clone(
      {headers: req.headers.set('Authorization', this.createBearerToken())});
    console.log(request);
    return next.handle(request);


  }

  createBearerToken(): string {
    if(!localStorage.getItem(this.TOKEN)){
      return undefined;
    }
    return this.prefix + localStorage.getItem(this.TOKEN);
  }


}
