import { Component, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Platform } from '@angular/cdk/platform';
import { TranslateService } from '@ngx-translate/core';

import { ForgerockSplashscreenService } from '@forgerock/openbanking-ngx-common/services/forgerock-splashscreen';
import { ForgerockGDPRService } from '@forgerock/openbanking-ngx-common/gdpr';

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  constructor(
    @Inject(DOCUMENT) private document: any,
    private splashscreenService: ForgerockSplashscreenService,
    private translateService: TranslateService,
    private platform: Platform,
    private gdprService: ForgerockGDPRService
  ) {
    this.splashscreenService.init();
    this.gdprService.init();

    // Add languages
    this.translateService.addLangs(['en', 'fr']);
    // this.translateService.setTranslation('en', engTranslation);
    this.translateService.setDefaultLang('en');

    // Use a language
    this.translateService.use(this.translateService.getBrowserLang() || 'en');

    // Add is-mobile class to the body if the platform is mobile
    if (this.platform.ANDROID || this.platform.IOS) {
      this.document.body.classList.add('is-mobile');
    }
  }
}
