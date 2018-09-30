import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewpageComponent } from './newpage.component';

describe('NewpageComponent', () => {
  let component: NewpageComponent;
  let fixture: ComponentFixture<NewpageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewpageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewpageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
