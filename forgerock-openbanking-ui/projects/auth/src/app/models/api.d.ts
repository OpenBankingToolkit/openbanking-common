interface AuthFormCallback {
  type: string;
  output: { name: string; value: string }[];
  input: { name: string; value: string }[];
}

export module ApiReponses {
  interface AuthLoginResponse {
    authId: string;
    template: string;
    stage:
      | 'DataStore1'
      | 'AuthenticatorPush3'
      | 'AuthenticatorPush4'
      | 'AuthenticatorPush5'
      | 'AuthenticatorPushRegistration2'
      | 'AuthenticatorPushRegistration3'
      | 'AuthenticatorPushRegistration4'
      | 'AuthenticatorPushRegistration5';
    header: string;
    callbacks: AuthFormCallback[];
  }
}

export module ApiRequest {
  interface IUserUpdateBody {
    givenName: string;
    mail: string;
    sn: string;
    telephoneNumber: string;
  }
  interface IUserPasswordUpdateBody {
    currentpassword: string;
    username: string;
    userpassword: string;
  }
}
