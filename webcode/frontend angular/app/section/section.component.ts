import {Component, Inject, Input, OnInit} from '@angular/core';
import { Subject, Subscription} from "rxjs";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";

import {LectionComponent} from "../lection/lection.component";
import {Section} from "../../models/section.model";
import CrudService from "../../services/crud.service";
import {Lection} from "../../models/lection.model";

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.scss'],
  providers: [CrudService]
})
export class SectionComponent implements OnInit {


  @Input()index: number;
  @Input()courseSubject: Subject<any>;
  @Input()parentFormGroup: FormGroup;
  lections: any[] = [] ;
  formGroup: FormGroup;
  sectionObj: Section ;
  createdSection: Subscription;
  lectionsSubject: Subject<any> = new Subject<any>();
  lectionSubs: Subscription;
  lectionComponents:  LectionComponent[] = [];
  constructor(private formBuilder: FormBuilder,
             private sectionService: CrudService<Section>) { }

  ngOnInit(): void {



    this.sectionService.path = 'section/';
      this.createdSection = this.sectionService.getCreatedObjectObservable().subscribe((section: Section) => {
        console.log(section);
        this.sectionObj = section;
        console.log(this.sectionObj);
      })
      this.lectionSubs = this.lectionsSubject.asObservable().subscribe((x: LectionComponent) => {
        console.log('=====Section Component====');
        console.log('===== NEW LECTION ADDED ======');
        const index: number = x.index;
        const formArray: FormArray = this.formGroup.get('lections') as FormArray;
        this.lectionComponents[index] = x;
        formArray.controls[x.index] = x.formGroup;
        if (this.courseSubject) {
          this.courseSubject.next(this);
        }

      });
      this.formGroup = this.formBuilder.group(
        {
          title: ['', Validators.required],
          lections: this.formBuilder.array([], Validators.min(1))
        }
      );
    this.formGroup.statusChanges.subscribe(x => console.log(x))
      if (this.parentFormGroup) {
        const formArray: FormArray = this.parentFormGroup.get('sections') as FormArray;
        formArray.push(this.formGroup);
      }
      this.formGroup.updateValueAndValidity()

  }


  addLection() {
    console.log('')
    this.lections.push({});
    this.lectionsSubject.next(this);
  }

  createSection() {
    console.log('section created');
    this.sectionObj = this.formGroup.value;
    if(this.courseSubject){ this.courseSubject.next(this.sectionObj); return;}
    this.sectionService.createObject(this.sectionObj);
    console.log(this.sectionService)

  }
}
