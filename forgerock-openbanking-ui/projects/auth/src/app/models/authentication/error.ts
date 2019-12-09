export interface ErrorDetail {
  errorCode: string;
}

export class Error {
  code: number;
  reason: string;
  message: string;
  detail?: ErrorDetail;

  constructor(val: any) {
    this.code = val.code;
    this.reason = val.reason;
    this.message = val.message;
    if (val.detail) {
      this.detail = val.detail;
    }
  }
}
