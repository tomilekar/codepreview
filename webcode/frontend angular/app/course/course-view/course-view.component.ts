import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Lection} from "../../../models/lection.model";
import {Subject, Subscription} from "rxjs";
import CrudService from "../../../services/crud.service";
import {Course} from "../../../models/course.model";

@Component({
  selector: 'app-course-view',
  templateUrl: './course-view.component.html',
  styleUrls: ['./course-view.component.scss']
})
export class CourseViewComponent implements OnInit {


  selectedTab: number = 0;
  id: string = '5f020b6a2af400944aacdddb';
  selectedLection: Lection;
  selectedSectionSubj: Subject<Lection> = new Subject<Lection>();
  requestedCourseSubscr: Subscription;
  selectedLectionSubscr: Subscription;
  course: Course;

  constructor(private crudService: CrudService<Course>, private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.crudService.path = 'course/';
    this.requestedCourseSubscr = this.crudService.getRequestedObjectObservable()
      .subscribe((course: Course) => {
        this.course = course;
        this.changeDetectorRef.detectChanges();
      });

    this.selectedLectionSubscr = this.selectedSectionSubj.asObservable().subscribe((lection: Lection) => {
      this.selectedLection = lection;
      console.log('Selected Lection')
      console.log(this.selectedLection)
      this.changeDetectorRef.detectChanges();
    });
    this.crudService.getObject(this.id);

  }

  videoClicked($event: MouseEvent) {
    console.log($event)
  }
  onSelectTab(number){
    this.selectedTab = number;
    console.log('view')
    console.log(this.selectedTab);
  }
}
