import {RouterModule, Routes} from '@angular/router';
import {LectionComponent} from './lection/lection.component';
import {LoginComponent} from './user/login/login.component';
import {RegisterComponent} from './user/register/register.component';
import {UserComponent} from './user/user.component';
import {SectionComponent} from './section/section.component';
import {CourseComponent} from './course/course.component';
import {ResourceComponent} from "./resource/resource.component";
import {CourseCardComponent} from "./course/course-card/course-card.component";
import {NgModule} from "@angular/core";
import {SliderComponent} from "./slider/slider.component";
import {ShoppingCartComponent} from "./shopping-cart/shopping-cart.component";
import {LectionSectionItemComponent} from "./lection/lection-section-item/lection-section-item.component";
import {Lection} from "../models/lection.model";
import {SectionListComponent} from "./section/section-list/section-list.component";
import {CourseViewComponent} from "./course/course-view/course-view.component";

export const SERVER_URL: string  = "http://localhost:3000/";
const routes: Routes = [

  {
    path: 'lection/item/:id',
    component: LectionSectionItemComponent
  },
  {
    path: 'shoppingcart',
    component: ShoppingCartComponent
  },
  {
    path: 'slider',
    component: SliderComponent
  },
  {
    path: 'course-view',
    component:CourseViewComponent
  },
  {
    path: 'course',
    component: CourseComponent
  },
  {
    path: 'course/:id',
    component: CourseCardComponent
  },
  {
    path: 'section-list',
    component: SectionListComponent,
    pathMatch: 'full'
  },
  {
    path: 'section',
    component: SectionComponent,
    pathMatch: 'full'
  },
  {
    path: 'lection',
    component: LectionComponent,
    pathMatch: 'full'
  },
  {
    path: 'resource',
    component: ResourceComponent,
    pathMatch: 'full'
  },
  {
    path: 'user',
    component: UserComponent,
    pathMatch: 'full'
  },
  {
    path: 'user/login',
    component: LoginComponent,
    pathMatch: 'full'
  },
  {
    path: 'user/register',
    component: RegisterComponent,
    pathMatch: 'full'
  },
  {
    path: '**',
    component: LectionComponent
  },


];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule{}


