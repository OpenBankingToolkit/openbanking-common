export interface IConfigClient {
  name: string;
  adminContact: string;
}

export interface IConfig {
  givenName: string;
  mail: string;
  sn: string;
  telephoneNumber: string;
  production: boolean;
  amSsoTokenCookieName: string;
  cookieDomain: string;
  authenticationServer: string;
  directoryBackend: string;
  defaultRealm: string;
  realmRedirections: {
    [key: string]: string;
  };
  client: IConfigClient;
}
