export interface UnauthorizedDetail {
  errorCode: string;
}

export class Unauthorized {
  code: number;
  reason: string;
  message: string;
  detail?: UnauthorizedDetail;

  constructor(val: any) {
    this.code = val.code;
    this.reason = val.reason;
    this.message = val.message;
    if (val.detail) {
      this.detail = val.detail;
    }
  }
}
