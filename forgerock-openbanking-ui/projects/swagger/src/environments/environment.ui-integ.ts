import { environment as devDefaultEnv } from './environment.dev.default';

export const environment = {
  ...devDefaultEnv,
  production: false,
  swaggerJSON: 'https://rs.aspsp.ui-integ.forgerock.financial/api-docs'
};
