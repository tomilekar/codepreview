import {Component, Input, OnInit} from '@angular/core';
import {Lection} from "../../../models/lection.model";

@Component({
  selector: 'app-lection-section-item',
  templateUrl: './lection-section-item.component.html',
  styleUrls: ['./lection-section-item.component.scss']
})
export class LectionSectionItemComponent implements OnInit {

  @Input()lection: Lection;
  constructor() { }

  ngOnInit(): void {
  }

}
