import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LectionSectionItemComponent } from './lection-section-item.component';

describe('LectionSectionItemComponent', () => {
  let component: LectionSectionItemComponent;
  let fixture: ComponentFixture<LectionSectionItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LectionSectionItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LectionSectionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
