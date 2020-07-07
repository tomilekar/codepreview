import {Component, Input, OnInit} from '@angular/core';
import {Course} from "../../../models/course.model";

@Component({
  selector: 'app-cart-detail',
  templateUrl: './cart-detail.component.html',
  styleUrls: ['./cart-detail.component.scss']
})
export class CartDetailComponent implements OnInit {

  @Input()course: Course;
  constructor() { }

  ngOnInit(): void {
  }

}
