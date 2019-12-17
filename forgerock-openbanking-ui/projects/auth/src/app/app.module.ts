import { NgModule, InjectionToken, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { StoreModule, ActionReducerMap } from '@ngrx/store';

import { MatSharedModule } from 'auth/src/app/mat-shared.module';
import { ForgerockSharedModule } from 'auth/src/app/forgerock-shared.module';
import { AppComponent } from 'auth/src/app/app.component';
import rootReducer from 'auth/src/store';
import { environment } from 'auth/src/environments/environment';
import { ForgerockConfigService } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { ForgerockConfigModule } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { ApiService } from 'auth/src/app/services/api.service';
import { ForgerockSharedComponentsModule } from '@forgerock/openbanking-ngx-common/shared';
import { ForgerockAuthenticationModule } from '@forgerock/openbanking-ngx-common/authentication';
import { AppRoutingModule } from 'auth/src/app/app-routing.module';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export const REDUCER_TOKEN = new InjectionToken<ActionReducerMap<{}>>('Registered Reducers');

export function getReducers() {
  return rootReducer;
}

export function init_app(appConfig: ForgerockConfigService) {
  return () => appConfig.fetchAndMerge(environment);
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),
    ForgerockSharedModule,
    ForgerockAuthenticationModule,
    // Store
    StoreModule.forRoot(REDUCER_TOKEN),
    // Material Design
    MatSharedModule,
    ForgerockConfigModule.forRoot(),
    ForgerockSharedComponentsModule,
    environment.devModules || []
  ],
  providers: [
    ApiService,
    {
      provide: REDUCER_TOKEN,
      deps: [],
      useFactory: getReducers
    },
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [ForgerockConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
