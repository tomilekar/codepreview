import {ChangeDetectorRef, Component, Injectable, OnInit} from '@angular/core';
import {Subject, Subscription} from 'rxjs';
import {AbstractControl, FormArray, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {SectionComponent} from "../section/section.component";
import {FileService} from "../../services/file.service";
import {CourseService} from "../../services/old/course.service";
import {Course} from "../../models/course.model";
import CrudService from "../../services/crud.service";


@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.scss']
})
@Injectable()
export class CourseComponent implements OnInit {


  course:Course;
  imageObservable: Subscription;
  formGroup: FormGroup;
  sections: any[] = []; // WILL BE USED TO INSTANTIATE NEW COMPONENT
  sectionComponents: SectionComponent[] = [];
  subject: Subject<SectionComponent> = new Subject<SectionComponent>();
  sectionSubscription:Subscription;
  createdCourseSubs: Subscription;
  constructor(public formBuilder: FormBuilder, private courseService: CrudService<Course>,
              private changeDetectorRef: ChangeDetectorRef, private fileService: FileService) {

  }

  ngOnInit(): void {
    this.courseService.path = 'course/';
    this.createdCourseSubs = this.courseService.getCreatedObjectObservable().subscribe((obj: Course) => {
      console.log('==== CREATED OBJ ==== COurse Component')
      this.course = obj;
      console.log(this.course);
    });
    this.imageObservable = this.fileService.onUploadAttachments().subscribe((x)=> {
      if(x['message']){
        console.log('Subscription Recieved Message' + x['message'])
        return;
      }
      const {path} = x;
      console.log(x)
      console.log('CREATED PATH')
      this.formGroup.get('image').setValue(path);
    });
    this.formGroup = this.formBuilder.group(
      {
        title: ['Course Title', Validators.required],
        image: ['', Validators.required],
        category: ['Course Category', Validators.required],
        description: ['Course Description', Validators.required],
        rating: ['5', Validators.required],
        price: ['5', Validators.required],
        sections: this.formBuilder.array([],Validators.min(1))
      }
    );


    this.sectionSubscription = this.subject.asObservable().subscribe((x:SectionComponent)=> {
      console.log('=====Course Component====');
      console.log('===== NEW Section ADDED ======');

      const formArray: FormArray = this.formGroup.get('sections') as FormArray;
      this.sectionComponents[x.index] = x;
      formArray.controls[x.index] = x.formGroup;
      this.changeDetectorRef.detectChanges();
      console.log('ARRAY===')
      console.log(formArray)
    });
    this.formGroup.statusChanges.subscribe((x) => {
      console.log('FormGroup ==== Course Component')
      console.log(x);
      console.log(this.formGroup)
    })

  }

  changeFile(event: Event) {
    const files: FileList = event.target['files'];
    const file: File = files.item(0);
    const formData: FormData = new FormData();
    formData.append('image',file);
    this.fileService.uploadImage(formData);

  }

  createCourse() {
    const object: Course = this.formGroup.value;
    this.courseService.createObject(object);
  }

  addSection() {
    this.sections.push({});
  }
}
