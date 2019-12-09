import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgerockGDPRService } from 'ob-ui-libs/gdpr';
import { ForegerockGDPRConsentGuard } from 'ob-ui-libs/gdpr';
import { ForgerockSimpleLayoutModule } from 'ob-ui-libs/layouts/simple';
import { SimpleLayoutComponent } from 'ob-ui-libs/layouts/simple';

export const routes: Routes = [
  {
    path: ForgerockGDPRService.gdprDeniedPage,
    component: SimpleLayoutComponent,
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/gdpr.module').then(m => m.OBUILibsLazyGDPRPage)
  },
  {
    path: '404',
    pathMatch: 'full',
    component: SimpleLayoutComponent,
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/not-found.module').then(m => m.OBUILibsLazyNotFoundPage)
  },
  {
    path: 'dev/info',
    component: SimpleLayoutComponent,
    pathMatch: 'full',
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/dev-info.module').then(m => m.OBUILibsLazyDevInfoPage)
  },
  {
    path: '',
    canActivate: [ForegerockGDPRConsentGuard],
    loadChildren: 'swagger/src/app/pages/home/home.module#HomeModule'
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes), ForgerockSimpleLayoutModule],
  exports: [RouterModule]
})
export class AppRoutingModule {}
