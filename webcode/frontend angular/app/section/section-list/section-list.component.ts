import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {Section} from "../../../models/section.model";
import CrudService from "../../../services/crud.service";
import {Subject, Subscription} from "rxjs";
import {Lection} from "../../../models/lection.model";

@Component({
  selector: 'app-section-list',
  templateUrl: './section-list.component.html',
  styleUrls: ['./section-list.component.scss']
})
export class SectionListComponent implements OnInit {


  @Input()selectedLection: Subject<Lection>;
  @Input()section: Section;
  sectionsSubscription: Subscription;
  id: string = '5f01feafd6eb0f9173279e86';
  showLections: boolean = false;
  constructor(private sectionService: CrudService<Section>, private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    console.log(this.section)
    this.sectionService.path = 'section/';
    this.sectionsSubscription = this.sectionService.getRequestedObjectObservable()
      .subscribe((section:Section) => {
        this.section = section;
        this.changeDetectorRef.detectChanges();
      });
    if(!this.section){
      this.sectionService.getObject(this.id);
    }

  }


  onShowLections(): void {
    console.log(this.section);
    this.showLections = !this.showLections;
  }

  onSelectLection(index: number) {
    if(!this.selectedLection){
      return;
    }
    this.selectedLection.next(this.section.lections[index]);
  }
}
