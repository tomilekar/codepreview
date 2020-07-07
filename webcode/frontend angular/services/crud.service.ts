import {Observable, Subject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Inject, Injectable} from "@angular/core";

@Injectable(
  {providedIn: 'root'}
)
 class CrudService<O> {

  private server: string = 'http://localhost:3000/';
  private _path: string = '';
  private server_path: string = "";
  private requestedObject: Subject<O> = new Subject<O>();
  private requestedObjects: Subject<O[]> = new Subject<O[]>();
  private createdObject: Subject<O> = new Subject<O>();

  constructor(private httpClient: HttpClient) {
  }
  getObject(id: string): void {
    if(this._path === ""){ console.log('CREATE PATH FIRST'); return;}
    this.httpClient.get(this.server_path +  id, {observe: 'body'})
      .subscribe((response: O) => {
        console.log('====' + this._path + ' =====');
        console.log(response);
        this.requestedObject.next(response);
      })
  }
  getObjects(): void {
    if(this._path === ""){ console.log('CREATE PATH FIRST'); return;}
    this.httpClient.get(this.server_path, {observe: 'body'})
      .subscribe((response: O[]) => {
        console.log('====' + this._path + ' =====');
        console.log(response);
        this.requestedObjects.next(response);
      })
  }
  createObject(input: O): void {
    if(this._path === ""){ console.log('CREATE PATH FIRST'); return;}
    this.httpClient.post(this.server_path, input,{observe: 'body'})
      .subscribe((response: O) => {
        console.log('====' + this._path + ' =====');
        console.log(response);
        this.createdObject.next(response);
      })
  }

  getRequestedObjectObservable(): Observable<O>{
    return this.requestedObject.asObservable();
  }

  getRequestedObjectsObservable(): Observable<O[]>{
    return this.requestedObjects.asObservable();
  }
  getCreatedObjectObservable(): Observable<O>{
    return this.createdObject.asObservable();
  }
  get path(): string {
    return this._path;
  }


  set path(value: string) {
    if(!value.match(".+\\/")){
      this._path = value + "/";
      this.server_path = this.server + this._path;
      return;
    }
    this._path = value;
    this.server_path = this.server + this._path;
  }
}

export default CrudService;
