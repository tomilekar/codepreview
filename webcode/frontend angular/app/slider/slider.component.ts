import {ChangeDetectorRef, Component, ElementRef, Injectable, Input, OnInit, ViewChild, ViewRef} from '@angular/core';
import {CourseService} from "../../services/old/course.service";
import {Course} from "../../models/course.model";
import {Subscription} from "rxjs";
import {animate, animateChild, state, style, transition, trigger} from "@angular/animations";
import CrudService from "../../services/crud.service";

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.scss'],
  animations: [trigger('animate',[state("next", style({
    transform: 'translateX(90px)',
    backgroundColor: 'green'
  })), state("*", style({})),
  transition('* => next', [
    animate('2s'),
    animateChild({})

  ])])]
})
@Injectable()
export class SliderComponent implements OnInit {

  @ViewChild('container')ref: ElementRef;
  maxCourseCards = 3;
  lastIndex= 2;
  firstIndex = 0;
  prevFirstIndex = 0;
  prevLastIndex= 2;
  sliderCards: Course[] = [] ;
  coursesSubscription: Subscription;
  courses:  any = {};
  constructor(private courseService: CrudService<Course>, private detectChanges:ChangeDetectorRef) { }


  ngOnInit(): void {
    this.courseService.path = 'course/';
    this.coursesSubscription =  this.courseService.getRequestedObjectsObservable().subscribe((x: Course[])=> {
      console.log('Courses ' + x)
      this.courses = x;
      this.courses.map((value: Course, index: number) => {
        if(this.sliderCards.length == this.maxCourseCards) {return;}
        this.sliderCards.push(value);
        console.log(this.sliderCards)
      })
    });
    this.courseService.getObjects();


  }
   getNextCards(): void {
     const element: HTMLHtmlElement = this.ref.nativeElement as HTMLHtmlElement;
    element.classList.remove('moveRight');
    this.prevFirstIndex = this.firstIndex;
    this.prevLastIndex = this.lastIndex;
    this.firstIndex += this.maxCourseCards;
    this.lastIndex += this.maxCourseCards;
    let iteration= 0;
     for(let index = this.firstIndex;
        index <= this.lastIndex; index++){
      console.log('====ITERATION: ' + index);
      const course = this.courses[index];
      console.log(course)
      this.sliderCards[iteration] = course;
      iteration++;
    }

     element.classList.add('moveRight')

  }
  getPreviouseCard(): void {
    this.prevFirstIndex = this.firstIndex;
    this.prevLastIndex = this.lastIndex;
    this.firstIndex -= this.maxCourseCards;
    this.lastIndex -= this.maxCourseCards;
    let iteration = 0;
    for(let index = this.firstIndex; index <= this.lastIndex; index++){
      const course = this.courses[index];
      this.sliderCards[iteration] = course;
      iteration++;
    }

  }



}
