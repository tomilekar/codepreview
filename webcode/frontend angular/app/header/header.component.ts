import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @ViewChild('menu') menu: ElementRef;
  constructor() { }

  ngOnInit(): void {


  }


  toggleDropDown($event: MouseEvent) {
    const htmlElement: HTMLHtmlElement = this.menu.nativeElement as HTMLHtmlElement;
    if(htmlElement.classList.contains('show')){
      htmlElement.classList.remove('show');
    return;
    }
    htmlElement.classList.add('show');
  }
}
