import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  attachments: Subject<any> = new Subject<any>();
  video: Subject<any> = new Subject<any>();
  private image: Subject<any> = new Subject<any>();
  constructor(private httpClient: HttpClient) {
  }
  uploadVideo(formData):void {
    this.httpClient.post('http://localhost:3000/video/',formData, {observe: "body"})
      .subscribe((x)=> {
        console.log('LectionVideo Uploaded')
        console.log(x);
        this.video.next(x);
      })
  }
  uploadAttachment(formdata: FormData){
    if(!formdata) {return;}
    this.httpClient.post('http://localhost:3000/attachments/', formdata, { observe: 'body'},
    ).subscribe(( body ) => {
      console.log('Attachment Uploaded');
      console.log(body);
      this.attachments.next(body);

    });
  }
  uploadImage(formdata: FormData){
    if(!formdata) {return;}
    this.httpClient.post('http://localhost:3000/image/', formdata, { observe: 'body'},
    ).subscribe(( body ) => {
      console.log('Attachment Uploaded');
      console.log(body);
      this.attachments.next(body);

    });
  }

  onUploadAttachments(): Observable<any>{
    return this.attachments.asObservable();
  }
  onUploadVideo(): Observable<any>{
    return this.video.asObservable();
  }
  onUploadImage(): Observable<any>{
    return this.image.asObservable();
  }

}
