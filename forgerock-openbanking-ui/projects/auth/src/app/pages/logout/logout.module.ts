import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatSharedModule } from 'auth/src/app/mat-shared.module';
import { TranslateSharedModule } from 'auth/src/app/translate-shared.module';
import { LogoutRoutingModule } from './logout-routing.module';
import { LogoutComponent } from './logout.component';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';

@NgModule({
  imports: [CommonModule, LogoutRoutingModule, MatSharedModule, TranslateSharedModule, ForgerockCustomerLogoModule],
  declarations: [LogoutComponent]
})
export class LogoutModule {}
