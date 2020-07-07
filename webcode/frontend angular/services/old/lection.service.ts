import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


import {Observable, Subject} from 'rxjs';
import {Lection} from "../../models/lection.model";
@Injectable({
  providedIn: 'root'
})
 export class LectionService  {

  lections: Lection[] = [];

  selectedLectionSubject: Subject<Lection> = new Subject<Lection>();
  createdSubject: Subject<Lection> = new Subject<Lection>();
  LectionsSubject: Subject<Lection> = new Subject<Lection>();

  constructor(private httpClient: HttpClient) {
  }

  getLections(): void {
    this.lections = [];

   this.httpClient
      .get('http://localhost:3000/lection',
        {responseType: 'json'}).subscribe(document => {
          console.log('document')
          console.log(document);
     Object.keys(document).forEach((key) => {
       const lection = document[key];
       this.lections.push(lection);
       this.LectionsSubject.next(lection);
     });
   });


  }

  createLection(lection: Lection): void {

    this.httpClient.post('http://localhost:3000/lection', lection,
      {observe: 'body'}).subscribe(
      object => {

        const lection: Lection = JSON.parse(JSON.stringify(object));
        console.log(lection);
        this.createdSubject.next(lection);
        console.log('CREATED');
        console.log(object);
      }
    );
  }
  deleteLections(): void {
    this.httpClient.delete('http://localhost:3000/lection',
      {observe: 'body'}).subscribe((next) => {
        console.log('next');
        console.log(next);
    }, (error => console.log(error)));
    console.log('deleted');

  }
  getLectionById(id: string): Lection {
    let ret = null;
   this.httpClient.get("http://localhost:3000/lection/" + id, {observe: 'response'})
     .subscribe((response) => {
       const lection =  response.body as Lection;

       this.selectedLectionSubject.next(lection);
       ret = lection;
     });

   return ret;

  }
  onGetCreatedLection(): Observable<Lection> {
    return this.createdSubject.asObservable();
  }
  onGetLections(): Observable<Lection> {
    return this.LectionsSubject.asObservable();
  }
  onSelectSubject(): Observable<Lection>{
    return this.selectedLectionSubject.asObservable();
  }


}

