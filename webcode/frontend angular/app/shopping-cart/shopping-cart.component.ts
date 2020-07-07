import {ChangeDetectorRef, Component, Injectable, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import ShoppingCartService from "../../services/old/shopping-cart.service";
import {Course} from "../../models/course.model";

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
@Injectable()
export class ShoppingCartComponent implements OnInit {

  calculatedPrice: number = 0;
  cart: Course[] = [];
  observable: Subscription;
  constructor(private shoppingCartService: ShoppingCartService, private detectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    if(this.shoppingCartService.shoppingCart.length > 0){
      this.cart = [...this.shoppingCartService.shoppingCart]
    }
    this.observable = this.shoppingCartService.getObservable().subscribe((x)=> {
      console.log(' Added to Cart');
      this.cart.push(x);
      this.detectorRef.detectChanges();
      this.recalculate();
      console.log('this')
    })

    console.log('this')
  }
  recalculate(): void {

    this.calculatedPrice = 0;
    for(let course of this.cart){
      this.calculatedPrice += course.price;
    }
    this.detectorRef.detectChanges();
  }

}
