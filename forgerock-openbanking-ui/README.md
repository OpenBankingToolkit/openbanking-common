## Build app locally
- Requirements: `node v10`, `npm v6`
```shell
cd forgerock-openbanking-ui
npm ci
npm run serve.auth
```
Review troubleshooting if you have any issue.

### Update dependency
```shell
npm install <package>
```

### troubleshooting
This project requires node 10 but the npm provided with node 10 raise an error:
```shell
npm does not support Node.js v10.24.1
You should probably upgrade to a newer version of node as we
can't make any promises that npm will work with this version.
You can find the latest version at https://nodejs.org/
/Users/jorge.sanchez-perez/.nvm/versions/node/v10.24.1/lib/node_modules/npm/lib/npm.js:32
  #unloaded = false
  ^

SyntaxError: Invalid or unexpected token
    at Module._compile (internal/modules/cjs/loader.js:723:23)
    at Object.Module._extensions..js (internal/modules/cjs/loader.js:789:10)
    at Module.load (internal/modules/cjs/loader.js:653:32)
    at tryModuleLoad (internal/modules/cjs/loader.js:593:12)
    at Function.Module._load (internal/modules/cjs/loader.js:585:3)
    at Module.require (internal/modules/cjs/loader.js:692:17)
    at require (internal/modules/cjs/helpers.js:25:18)
    at module.exports (/Users/jorge.sanchez-perez/.nvm/versions/node/v10.24.1/lib/node_modules/npm/lib/cli.js:22:15)
    at Object.<anonymous> (/Users/jorge.sanchez-perez/.nvm/versions/node/v10.24.1/lib/node_modules/npm/bin/npm-cli.js:2:25)
    at Module._compile (internal/modules/cjs/loader.js:778:30)
```
Using node 14 `npm` will raise an error when `npm install <package>` runs 
because the npm version of node 14 is more restrictive with the peer dependencies
and attempt to update the package-lock.json file, this will cause future issues.

To build the project properly:
```shell
cd forgerock-openbanking-ui
rm -rf node_modules
```
```shell
npx npm@v6 ci

Need to install the following packages:
  npm@6.14.18
Ok to proceed? (y)
```
```shell
npx npm@v6 run serve.auth
```

## Running docker image manually

**auth-ui** & **swagger-ui** are builds of the Auth & Swagger app with only the Forgerock theme.

It is convenient to start the app in no time.

- `<PORT>`: **REQUIRED** Port to use on your machine
- `<DOMAIN>`: **REQUIRED** Domain to use. Will replace `DOMAIN` in the frontend [config](./forgerock-openbanking-ui/projects/auth/docker/deployment-settings.js) e.g: `https://analytics.DOMAIN`
- `<TEMPLATE_NAME>`: Default value: `forgerock`.

```bash
docker run -it -p <PORT>:80 -e TEMPLATE=<TEMPLATE_NAME> -e DOMAIN=<DOMAIN> openbankingtoolkit/openbanking-auth-ui
docker run -it -p <PORT>:80 -e TEMPLATE=<TEMPLATE_NAME> -e DOMAIN=<DOMAIN> openbankingtoolkit/openbanking-swagger-ui
```

## Run Docker compose
The UI apps depend on services:
- Reference implementation services
    - AUTHENTICATION_URL: "https://am.DOMAIN"
    - DIRECTORY_BACKEND_URL: "https://service.directory.DOMAIN/directory-services"
    
> Run first the reference implementation spring boot apps

Working Directory: ./forgerock-openbanking-ui
```bash
docker-compose up
```
```bash
docker-compose -f [docker-compose-file] up
```
## Building the app with your theme

Create a new theme: <https://github.com/OpenBankingToolkit/openbanking-toolkit/wiki/Create-a-new-Theme>

Then build the docker image

## Building your own docker image
```bash
# Build
docker build -t <IMAGE_NAME> -f projects/auth/docker/Dockerfile .
docker build -t <IMAGE_NAME> -f projects/swagger/docker/Dockerfile .
```
