import {Component, Injectable, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Course} from "../../../models/course.model";
import {CourseService} from "../../../services/old/course.service";

import {Subject, Subscription} from "rxjs";
import {animate, keyframes, state, style, transition, trigger} from "@angular/animations";
import ShoppingCartService from "../../../services/old/shopping-cart.service";
import CrudService from "../../../services/crud.service";

@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.scss'],
  providers: [],

})
@Injectable({providedIn:'root'})
export class CourseCardComponent implements OnInit {
  id: string;

  sliderSubj: Subject<any>;
  subscription: Subscription;
  @Input()course: Course;
  constructor(private activatedRoute: ActivatedRoute, private courseService: CrudService<Course>,
              private shoppingCartService: ShoppingCartService, private router: Router) {}


  ngOnInit(): void {

    this.courseService.path = 'course/';
    this.activatedRoute.params.subscribe(params => {
      this.id = params['id'];
      console.log(this.id)
    });
    this.subscription = this.courseService.getRequestedObjectObservable().subscribe((x)=>{
      this.course = x;
      console.log(x)
      if(this.sliderSubj && this.course){
        this.sliderSubj.next(this.course);
      }
    });
    this.courseService.getObject(this.id);
  }

  addToShoppingCart(): void {
    this.shoppingCartService.addToCart(this.course);
    this.shoppingCartService.addToUser(this.course);
  }
  openDetails(): void {
    console.log(this.course)
    console.log(this.activatedRoute.parent);
    this.router.navigateByUrl('course/' + this.course['_id']).then((x) => {
      console.log(x);
    }).catch((x)=> {
      console.log(x);

    })
  }
}
