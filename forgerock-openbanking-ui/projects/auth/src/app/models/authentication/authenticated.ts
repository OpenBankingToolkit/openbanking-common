export interface IAuthenticated {
  realm: string;
  successUrl: string;
  tokenId: string;
}

export class Authenticated {
  authenticated: IAuthenticated;
  constructor(authenticated: IAuthenticated) {
    this.authenticated = authenticated;
  }
}
