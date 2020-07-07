import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {Form, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable, Subject, Subscription} from "rxjs";
import {ResourceComponent} from "../resource/resource.component";
import {FileService} from "../../services/file.service";
import CrudService from "../../services/crud.service";
import {Lection} from "../../models/lection.model";


@Component({
  selector: 'app-lection',
  templateUrl: './lection.component.html',
  styleUrls: ['./lection.component.scss'],
  providers: [CrudService]
})
export class LectionComponent implements OnInit {

  @Input()sectionSubject: Subject<any>;
  @Input() index: any;
  @Input()parentFormGroup: FormGroup;
  resourceObservable: Observable<any>;
  resourcesSubject: Subject<any> = new Subject<any>();
  videoSubscription: Subscription;
  formGroup: FormGroup;
  resourceComponents: ResourceComponent[] = [];
  resources: any[] = [];
  lectionVideo: File;

  createdLection: Lection;
  createdLectionSubscription: Subscription;
  constructor(private formBuilder: FormBuilder, private viewRef: ChangeDetectorRef,
              private fileService: FileService, private lectionService: CrudService<Lection>) { }

  ngOnInit(): void {
    this.lectionService.path = 'lection/';
    this.createdLectionSubscription = this.lectionService.getCreatedObjectObservable().subscribe(( x) => {
      this.createdLection = x;
    });
    this.formGroup = this.formBuilder.group(
      {
        title: ['Lection Title', Validators.required],
        video: this.formBuilder.group({
          type: ['ResourceType.Video', Validators.required],
          path: ['', Validators.required]
        }, Validators.required),
        attachments: this.formBuilder.array([])
      }
    );
    if(this.parentFormGroup){
      const formArray: FormArray = this.parentFormGroup.get('lections') as FormArray;
      formArray.push(this.formGroup);
      this.resourceObservable = this.resourcesSubject.asObservable();
      this.resourceObservable.subscribe((x: ResourceComponent)=> {
        console.log('=====Lection Component====');
        console.log('===== NEW Resource ADDED ======');
        const formArray: FormArray = this.formGroup.get('attachments') as FormArray;
        this.resourceComponents[x.index] = x;
        formArray.controls[x.index] = x.formGroup;
      });

    }
    this.videoSubscription = this.fileService.onUploadVideo().subscribe(
      (x: {path: string}) => {
        console.log(' Lection Video Upload222ed');
        const form : FormGroup = this.formGroup.get('video') as FormGroup;
        const formC: FormControl = form.get('path') as FormControl;
        formC.setValue(x.path);
        console.log(formC)
      });
    this.formGroup.updateValueAndValidity();

    if(this.sectionSubject){
      this.sectionSubject.next(this)
    }

  }

  createLection() {
    const object: Lection = this.formGroup.value;
    this.createdLection = object;
    if(this.sectionSubject){ this.sectionSubject.next(this); return;}
    this.lectionService.createObject(this.createdLection);
    console.log(object)

  }

  addResource() {
    this.resources.push({})
    this.viewRef.detectChanges()

  }

  onChangeVideo($event: Event) {
    const videoFormGroup: FormGroup = this.formGroup.get('video') as FormGroup;
    const formControl: FormControl = videoFormGroup.get('path') as FormControl;
    formControl.setValue('');
    const filesList: FileList = event.target['files'];
    this.lectionVideo = filesList.item(0);
  }

  uploadLectionVideo() {
    if(!this.lectionVideo) {return;}
    const formData: FormData = new FormData();
    formData.append('video', this.lectionVideo);
    this.fileService.uploadVideo(formData);

  }
}
