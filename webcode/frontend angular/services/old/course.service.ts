import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {Course} from "../../models/course.model";
import CrudService from "../crud.service";
import {Section} from "../../models/section.model";

@Injectable({providedIn:"root" })
export class CourseService {

  courses: Course[] = [];
  private _allCoursesSubject: Subject<Course[]> = new Subject<Course[]>();
  requestedCourseObject: Subject<any> = new Subject<any>();
  createdCourseSubject: Subject<unknown> = new Subject<unknown>();
  constructor(private httpClient: HttpClient) {
  }


  createLection(course: Course){
    this.httpClient.post('http://localhost:3000/course',course).subscribe((x) => {
      this.createdCourseSubject.next(x);
      }
    )
  }
  getCourses(): void{
    this.httpClient.get('http://localhost:3000/course', {observe:"response"}).subscribe((x) => {
      console.log('Course Service === getCourses()');
      console.log('====BODY=====');
      this.courses = x.body as Course[];
      console.log(this.courses);
      this._allCoursesSubject.next(this.courses);
    });
  }
  getCourseById(id: string): void {
    this.httpClient.get('http://localhost:3000/course/' + id).subscribe((x) => {
      const course: Course = x['course'][0] as unknown as Course;
      this.requestedCourseObject.next(course);
  });
  }
  getRequestedCourseObserv(): Observable<any>{
    return this.requestedCourseObject.asObservable();
  }
  getCreatedSubjectObserv(): Observable<unknown> {
    return this.createdCourseSubject.asObservable();
  }

   allCoursesSubject(): Observable<Course[]> {
    return this._allCoursesSubject.asObservable();
  }
}
