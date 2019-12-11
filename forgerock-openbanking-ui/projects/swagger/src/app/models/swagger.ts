export interface ISwaggerNavigationItem {
  name: string;
  method: string;
  path: string;
}

export interface ISwaggerNavigation {
  [tag: string]: ISwaggerNavigationItem[];
}

export interface ISwaggerJSON {
  swagger: string;
  info: {};
  host: string;
  basePath: string;
  tags: {
    name: string;
    description: string;
  }[];
  paths: any;
  definition: any;
}

export interface ISwaggerEndpointItem {
  endpoint: string;
  method: string;
  tags: string[];
  summary: string;
  description: string;
  operationId: string;
  consumes: string[];
  produces: string[];
  parameters: any[];
  responses: {
    [status: string]: {
      description: string;
      schema: any;
    };
  };
  security: any[];
  deprecated: boolean;
}
