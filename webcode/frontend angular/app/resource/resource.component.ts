import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {Subject, Subscription} from "rxjs";
import {FileService} from "../../services/file.service";
import {AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import IResource from "../../models/resource.model";


@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.scss']
})
export class ResourceComponent implements OnInit {

   @Input('index')index: any;
   @Input()subject: Subject<any>;
   @Input()parentFormGroup: FormGroup;
   resourceTypes: string[] = ['ResourceType.Text', 'ResourceType.Document', 'ResourceType.URL'];
   formGroup: FormGroup;
   subscription: Subscription;
   file:File;
   @Input()resourceType: any;
  constructor(private formBuilder: FormBuilder, private fileService: FileService) { }

  ngOnInit(): void {
    this.subscription = this.fileService.onUploadVideo().subscribe((x: {path: string}) => {
      console.log(x);
      const formArray: FormArray = this.parentFormGroup.get('attachments') as FormArray;
      const formGroup: FormGroup = formArray.at(this.index) as FormGroup;
      const formControl: FormControl = formGroup.get('path') as FormControl;
      formControl.setValue(x.path);
    console.log(this.formGroup);
    this.subject.next(this);
    });
    this.formGroup = this.formBuilder.group(
      {
        type: [this.resourceType || '', Validators.required],
        text: ['Test text', Validators.required],
        path: ['', Validators.required]
      }
    );
     const formArray: FormArray = this.parentFormGroup.get('attachments') as FormArray;
      formArray.push(this.formGroup)
  }

  changeResourceFile($event: Event) {
    const files: FileList = event.target['files'];
    this.file = files.item(0);
    this.formGroup.get('path').setValue('');
    this.subject.next(this)
  }

  uploadResource() {
    const formData: FormData = new FormData();
    formData.append('attachment',   this.file)
    formData.append('message', 'attachment');
    this.fileService.uploadAttachment(formData);

  }

  onChangeType(event: Event) {
    this.resourceType = event.target['value'];
  }
}
