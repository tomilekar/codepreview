import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ActivatedRoute, Router, RouterModule, Routes} from '@angular/router';
import {  ReactiveFormsModule, FormsModule} from '@angular/forms';
import { AppComponent } from './app.component';

import { SectionComponent } from './section/section.component';
import { HeaderComponent } from './header/header.component';
import { CourseComponent } from './course/course.component';
import { SliderComponent } from './slider/slider.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { NotificationsComponent } from './notifications/notifications.component';
import { AuthorComponent } from './author/author.component';
import { MessagesComponent } from './messages/messages.component';
import { SubHeaderComponent } from './sub-header/sub-header.component';
import { LoginComponent } from './user/login/login.component';
import { AuthService } from 'src/services/auth.service';
import { AuthGuard } from 'src/route-guard/auth.guard';
import { ResourceComponent } from './resource/resource.component';
import {LectionComponent} from "./lection/lection.component";
import {UserComponent} from "./user/user.component";
import {RegisterComponent} from "./user/register/register.component";
import { CourseCardComponent } from './course/course-card/course-card.component';
import {CommonModule} from "@angular/common";
import {AppRoutingModule} from "./app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { CartDetailComponent } from './course/cart-detail/cart-detail.component';
import ShoppingCartService from "../services/old/shopping-cart.service";
import { LectionSectionItemComponent } from './lection/lection-section-item/lection-section-item.component';
import { SectionListComponent } from './section/section-list/section-list.component';
import { CourseViewComponent } from './course/course-view/course-view.component';
import { TabbedViewComponent } from './tabbed-view/tabbed-view.component';
import {TokenInterceptor} from "../services/interceptors/token.interceptor";

@NgModule({
  declarations: [
    AppComponent,
    LectionComponent,
    SectionComponent,
    HeaderComponent,
    CourseComponent,
    UserComponent,
    SliderComponent,
    ShoppingCartComponent,
    NotificationsComponent,
    AuthorComponent,
    MessagesComponent,
    SubHeaderComponent,
    LoginComponent,
    RegisterComponent,
    ResourceComponent,
    CourseCardComponent,
    CartDetailComponent,
    LectionSectionItemComponent,
    SectionListComponent,
    CourseViewComponent,
    TabbedViewComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  exports: [],
  providers: [AuthGuard, AuthService, ShoppingCartService, {
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent],
})
export class AppModule {}
