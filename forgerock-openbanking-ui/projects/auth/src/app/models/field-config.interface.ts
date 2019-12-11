export interface IOutput {
  name: string;
  value: string;
}

export interface IInput {
  name: string;
  value: string;
}

export interface ICallback {
  type: string;
  output: IOutput[];
  input: IInput[];
}

export interface ICallbackHandlers {
  authId?: string;
  template?: string;
  stage?: string;
  header?: string;
  callbacks?: ICallback[];
}
