import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { RouterTestingModule } from '@angular/router/testing';

import rootReducer from 'auth/src/store';
import { MatSharedModule } from 'auth/src/app/mat-shared.module';
import { TranslateSharedModule } from 'auth/src/app/translate-shared.module';
import { LogoutRoutingModule } from './logout-routing.module';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';

import { LogoutComponent } from './logout.component';

describe('app:auth LogoutComponent', () => {
  let component: LogoutComponent;
  let fixture: ComponentFixture<LogoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        LogoutRoutingModule,
        MatSharedModule,
        TranslateSharedModule,
        ForgerockCustomerLogoModule,
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        RouterTestingModule.withRoutes([])
      ],
      declarations: [LogoutComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
