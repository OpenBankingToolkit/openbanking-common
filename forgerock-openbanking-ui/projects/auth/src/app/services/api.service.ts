import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';

import { encodeQueryData } from '@utils/url';
import { ForgerockConfigService } from '@forgerock/openbanking-ngx-common/services/forgerock-config';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  get sessionInfoUrl() {
    return `${this.configService.get('authenticationServer')}/json/sessions?_action=getSessionInfo`;
  }

  constructor(private http: HttpClient, private configService: ForgerockConfigService) {}

  login(realm = this.configService.get('defaultRealm'), body, queries = {}) {
    return this.http.post(
      `${this.configService.get('authenticationServer')}/json/realms/root/realms${prefixRealm(
        realm
      )}/authenticate${encodeQueryData(queries)}`,
      body,
      getDefaultHeaders()
    );
  }

  logout() {
    return this.http.post(
      `${this.configService.get('authenticationServer')}/json/sessions/?_action=logout`,
      {},
      getDefaultHeaders({
        'Accept-API-Version': 'protocol=1.0,resource=2.0'
      })
    );
  }

  getUserProfile(realm = this.configService.get('defaultRealm'), username = '') {
    return this.http.get(
      `${this.configService.get('authenticationServer')}/json/realms/root/realms${prefixRealm(
        realm
      )}/users/${username}`,
      getDefaultHeaders({
        'Accept-API-Version': 'protocol=1.0,resource=2.0'
      })
    );
  }

  updateUserProfile(realm = this.configService.get('defaultRealm'), username = '', body: any = {}) {
    return this.http.put(
      `${this.configService.get('authenticationServer')}/json/realms/root/realms${prefixRealm(
        realm
      )}/users/${username}`,
      body,
      getDefaultHeaders({
        'Accept-API-Version': 'protocol=1.0,resource=2.0'
      })
    );
  }

  updateUserPassword(realm = this.configService.get('defaultRealm'), username = '', body: any = {}) {
    return this.http.post(
      `${this.configService.get('authenticationServer')}/json/realms/root/realms${prefixRealm(
        realm
      )}/users/${username}?_action=changePassword`,
      body,
      getDefaultHeaders({
        'Accept-API-Version': 'protocol=1.0,resource=2.0'
      })
    );
  }

  register(realm = this.configService.get('defaultRealm'), values: any) {
    const request = {
      input: {
        user: {
          username: values.username,
          givenName: values.firstname,
          sn: values.lastname,
          mail: values.email,
          userPassword: values.password,
          inetUserStatus: 'Active'
        }
      }
    };
    return this.http.post(
      `${this.configService.get('authenticationServer')}/json/realms/root/realms${prefixRealm(
        realm
      )}/selfservice/userRegistration?_action=submitRequirements`,
      request,
      getDefaultHeaders({
        'Accept-API-Version': 'protocol=1.0,resource=1.0'
      })
    );
  }

  getSession(): Observable<any> {
    return this.http.post(this.sessionInfoUrl, {}, getDefaultHeaders());
  }

  isConnected(): Observable<boolean> {
    return this.getSession().pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }
}

function prefixRealm(realm: string): string {
  return realm[0] === '/' ? realm : `/${realm}`;
}

function getDefaultHeaders(headers: { [key: string]: string } = {}) {
  return {
    withCredentials: true,
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      ...headers
    })
  };
}
