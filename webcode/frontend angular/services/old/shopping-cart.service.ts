import {Observable, Subject} from "rxjs";
import {Course} from "../../models/course.model";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
@Injectable({
  providedIn: "root"
})
class ShoppingCartService {

  subject: Subject<any> = new Subject<any>();
  shoppingCart: any[] = [];
  constructor(private httpClient: HttpClient) {
  }

   addToCart(course: Course | Course[]){
    console.log(this.shoppingCart)
    this.shoppingCart.push(course);
    this.subject.next(course);
  }
  addToUser(course: Course){
    const userid = localStorage.getItem('id');
    const courseid = course['_id'];
    console.log(course['_id']);
    this.httpClient.patch('http://localhost:3000/users/' + userid, {id: userid, course: courseid},{observe: "body"})
      .subscribe((response) => {
        console.log(response);
      })
  }
  getObservable(): Observable<any>{
    return this.subject.asObservable();
  }

}

export default ShoppingCartService;
