import { BrowserModule } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';
import { NgModule, InjectionToken, APP_INITIALIZER } from '@angular/core';
import { StoreModule, ActionReducerMap } from '@ngrx/store';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { ForgerockConfigService } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { ForgerockConfigModule } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { AppComponent } from 'swagger/src/app/app.component';
import { TranslateSharedModule } from 'swagger/src/app/translate-shared.module';
import { AppRoutingModule } from 'swagger/src/app/app-routing.module';
import { environment } from 'swagger/src/environments/environment';
import rootReducer from 'swagger/src/store';
import { SwaggerService } from './services/swagger.service';

export const REDUCER_TOKEN = new InjectionToken<ActionReducerMap<{}>>('Registered Reducers');

export function getReducers() {
  return rootReducer;
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function init_app(appConfig: ForgerockConfigService, swaggerService: SwaggerService) {
  return () => appConfig.fetchAndMerge(environment).then(() => swaggerService.init());
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    ForgerockSharedModule,
    ForgerockConfigModule.forRoot(),
    TranslateSharedModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),
    // Store
    StoreModule.forRoot(REDUCER_TOKEN)
  ],
  providers: [
    SwaggerService,
    {
      provide: REDUCER_TOKEN,
      deps: [],
      useFactory: getReducers
    },
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [ForgerockConfigService, SwaggerService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
