import {Injectable} from "@angular/core";
import {Observable, Subject} from "rxjs";
import {Section} from "../../models/section.model";
import {HttpClient} from "@angular/common/http";
import {Lection} from "../../models/lection.model";

@Injectable(
  {providedIn: 'root'}
)
class SectionService {

  SERVER: string = 'http://localhost:3000/';
  SECTION : string = 'section';
  createdSectionSubject: Subject<Section> = new Subject<Section>();
  requestedSection: Subject<Section>;
  requestedSections: Subject<Section[]> = new Subject<Section[]>();
  constructor(private httpClient: HttpClient) {
  }

  createSection(section: Section): void {
    if(!section){return;}
    console.log(section);
    this.httpClient.post('' + this.SECTION,section,
      {observe: "body"}).
    subscribe((section: Section)=> {
        console.log(section);
        this.createdSectionSubject.next(section);
    })
  }

   getSection(id: string): void {
     this.httpClient.get(this.SERVER + id, {observe: "response"})
      .subscribe((x) => {
        console.log('===GET SECTION==');
        console.log(x);
        this.requestedSection.next()
      })
  }
   getSections(id: string): void {
     this.httpClient.get(this.SERVER + id, {observe: "response"})
      .subscribe((x) => {
        console.log('===GET SECTIONS==');
        console.log(x);
        this.requestedSection.next()
      })
  }
  createdLectionObservable(): Observable<Section>{
    return this.createdSectionSubject.asObservable();
  }

}


export default SectionService;
