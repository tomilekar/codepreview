import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-tabbed-view',
  templateUrl: './tabbed-view.component.html',
  styleUrls: ['./tabbed-view.component.scss']
})
export class TabbedViewComponent implements OnInit {

  @Output()selectedTab: EventEmitter<number> = new EventEmitter<number>();
  tabs: any[] = ['Tab 1', 'Tab2', 'Übersicht', 'Kurs Inhalt'];
  constructor() { }

  ngOnInit(): void {

  }

  onSelectTab(index: number) {
    console.log('Selected Tab: ' + index)
    this.selectedTab.emit(index);
  }
}
