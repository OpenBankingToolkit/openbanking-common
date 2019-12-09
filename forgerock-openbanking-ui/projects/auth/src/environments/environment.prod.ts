import { environment as defaultEnv } from './environment.default';

export const environment = {
  ...defaultEnv,
  production: true,
  cookieDomain: '.ob.forgerock.financial',
  authenticationServer: 'https://am.ob.forgerock.financial',
  directoryBackend: 'https://service.directory.ob.forgerock.financial/directory-services'
};
