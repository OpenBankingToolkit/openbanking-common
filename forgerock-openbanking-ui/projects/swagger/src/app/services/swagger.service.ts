import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import _get from 'lodash-es/get';
import _set from 'lodash-es/set';
import _forEach from 'lodash-es/forEach';
import _kebabCase from 'lodash-es/kebabCase';

import { ForgerockConfigService } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { ISwaggerNavigation, ISwaggerJSON, ISwaggerEndpointItem } from '../models/swagger';

@Injectable({
  providedIn: 'root'
})
export class SwaggerService {
  private _init = false;
  private _nav: any = {};
  private _paths: any = {};

  constructor(private http: HttpClient, private configService: ForgerockConfigService) {}

  init(): Promise<any> {
    if (this._init) {
      throw new Error('Already init');
    }
    this._init = true;

    return this.http
      .get(this.configService.get('swaggerJSON'), {
        withCredentials: true,
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      })
      .toPromise()
      .then(
        (data: ISwaggerJSON) => {
          const { navigation, paths } = dataParser(data);
          this._nav = navigation;
          this._paths = paths;
        },
        () => true // even if we fail we boot the app
      );
  }

  get navigation() {
    return this._nav;
  }

  public getEndpointInfo(path: string) {
    return this._paths[path];
  }
}

function dataParser(data: ISwaggerJSON) {
  const pathPerUrl: {
    [endpoint: string]: ISwaggerEndpointItem;
  } = {};
  const navigation: ISwaggerNavigation = {};

  _forEach(data.paths, (path, endpoint) => {
    _forEach(path, (pathObject, method) => {
      const link = _kebabCase(`${endpoint}-${method}`);
      pathPerUrl[link] = {
        endpoint,
        method,
        ...pathObject
      };
      pathObject.tags.forEach(tag => {
        const newNavPath = {
          name: pathObject.summary,
          method,
          link: '/' + _kebabCase(`${endpoint}-${method}`),
          path: `paths.${endpoint}.${method}`
        };

        if (navigation[tag]) {
          navigation[tag].push(newNavPath);
        } else {
          navigation[tag] = [newNavPath];
        }
      });
    });
  });

  return {
    paths: pathPerUrl,
    navigation
  };
}
