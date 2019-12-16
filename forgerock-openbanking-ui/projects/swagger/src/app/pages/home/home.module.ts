import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { NavigationComponent } from './components/navigation/navigation.component';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { DetailsComponent } from './components/details/details.component';

@NgModule({
  declarations: [HomeComponent, NavigationComponent, DetailsComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatCardModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatListModule,
    MatChipsModule,
    MatExpansionModule,
    ForgerockSharedModule
  ]
})
export class HomeModule {}
