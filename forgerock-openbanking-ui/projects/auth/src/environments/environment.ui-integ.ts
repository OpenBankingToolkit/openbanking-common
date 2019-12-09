// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import { environment as defaultEnv } from './environment.dev.default';

export const environment = {
  ...defaultEnv,
  production: false,
  cookieDomain: '.ui-integ.forgerock.financial',
  authenticationServer: 'https://am.ui-integ.forgerock.financial',
  directoryBackend: 'https://service.directory.ui-integ.forgerock.financial/directory-services'
  // featureFlags: {
  //   disableProfileForm: false,
  //   disablePasswordForm: false
  // },
  // routeDenyList: ['profile/password']
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
