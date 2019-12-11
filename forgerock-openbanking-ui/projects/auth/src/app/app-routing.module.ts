import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgerockSimpleLayoutModule } from 'ob-ui-libs/layouts/simple';
import { SimpleLayoutComponent } from 'ob-ui-libs/layouts/simple';
import { ForgerockGDPRService } from 'ob-ui-libs/gdpr';
import { ForegerockGDPRConsentGuard } from 'ob-ui-libs/gdpr';
import { ForgerockMainLayoutComponent } from 'ob-ui-libs/layouts/main-layout';
import { ForgerockMainLayoutModule } from 'ob-ui-libs/layouts/main-layout';
import { IForgerockMainLayoutConfig, IForgerockMainLayoutNavigations } from 'ob-ui-libs/layouts/main-layout';
import {
  ForgerockToolbarMenuComponentModule,
  ForgerockToolbarMenuContainer,
  IsConnectedPublicGuard,
  IsConnectedPrivateGuard
} from 'ob-ui-libs/authentication';
import { ForgerockCustomerCanAccessGuard } from 'ob-ui-libs/guards';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login'
  },
  {
    path: ForgerockGDPRService.gdprDeniedPage,
    component: SimpleLayoutComponent,
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/gdpr.module').then(m => m.OBUILibsLazyGDPRPage)
  },
  {
    path: '',
    component: SimpleLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: 'login',
        loadChildren: 'auth/src/app/pages/login/login.module#LoginModule',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPublicGuard],
        data: {
          private: true
        }
      },
      {
        path: 'register',
        loadChildren: 'auth/src/app/pages/register/register.module#RegisterModule',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPublicGuard]
      },
      {
        path: 'logged-out',
        canLoad: [ForgerockCustomerCanAccessGuard],
        loadChildren: 'auth/src/app/pages/logout/logout.module#LogoutModule'
      },
      {
        path: '404',
        pathMatch: 'full',
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/not-found.module').then(m => m.OBUILibsLazyNotFoundPage)
      },
      {
        path: 'dev/info',
        pathMatch: 'full',
        loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/dev-info.module').then(m => m.OBUILibsLazyDevInfoPage)
      }
    ]
  },
  {
    path: '',
    component: ForgerockMainLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: 'profile',
        pathMatch: 'full',
        redirectTo: 'profile/details'
      },
      {
        path: 'profile/details',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPrivateGuard],
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/authentication/details.module').then(
            m => m.OBUILibsLazyAuthProfileDetailsPage
          )
      },
      {
        path: 'profile/password',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPrivateGuard],
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/authentication/password.module').then(
            m => m.OBUILibsLazyAuthProfilePasswordPage
          )
      }
    ]
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: '404'
  }
];

const mainLayoutConfig: IForgerockMainLayoutConfig = {
  style: 'vertical-layout-1',
  navbar: {
    folded: false,
    hidden: false,
    position: 'left'
  },
  toolbar: {
    hidden: false
  },
  footer: {
    hidden: true,
    position: 'below-static'
  }
};

export const navigations: IForgerockMainLayoutNavigations = {
  main: [
    {
      id: 'profile',
      translate: 'NAV.PROFILE',
      type: 'item',
      icon: 'face',
      url: '/profile/details'
    },
    {
      id: 'security',
      translate: 'NAV.SECURITY',
      type: 'item',
      icon: 'security',
      url: '/profile/password'
    }
  ]
};

@NgModule({
  imports: [
    ForgerockSimpleLayoutModule,
    ForgerockToolbarMenuComponentModule,
    ForgerockMainLayoutModule.forRoot({
      layout: mainLayoutConfig,
      navigations,
      components: {
        toolbar: ForgerockToolbarMenuContainer
      }
    }),
    RouterModule.forRoot(routes)
  ],
  exports: [ForgerockSimpleLayoutModule, RouterModule]
})
export class AppRoutingModule {}
