[<img src="https://raw.githubusercontent.com/ForgeRock/forgerock-logo-dev/master/Logo-fr-dev.png" align="right" width="220px"/>](https://developer.forgerock.com/)

| |Current Status|
|---|---|
|Build|[![Build Status](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2FOpenBankingToolkit%2Fopenbanking-common%2Fbadge%3Fref%3Dmaster&style=flat)](https://actions-badge.atrox.dev/OpenBankingToolkit/openbanking-common/goto?ref=master)|
|Code coverage|[![codecov](https://codecov.io/gh/OpenBankingToolkit/openbanking-common/branch/master/graph/badge.svg)](https://codecov.io/gh/OpenBankingToolkit/openbanking-common)
|Bintray|[![Bintray](https://img.shields.io/bintray/v/openbanking-toolkit/OpenBankingToolKit/openbanking-commons.svg?maxAge=2592000)](https://bintray.com/openbanking-toolkit/OpenBankingToolKit/openbanking-commons)|
|License|![license](https://img.shields.io/github/license/ACRA/acra.svg)|

**_This repository is part of the Open Banking Tool kit. If you just landed to that repository looking for our tool kit,_
_we recommend having a first read to_ https://github.com/OpenBankingToolkit/openbanking-toolkit**

ForgeRock OpenBanking common
============================

ForgeRock OpenBanking Commons is a set of abstractions and common classes used in different ForgeRock OpenBanking projects.

ForgeRock OpenBanking Commons also contains the UI apps for auth and swagger.

## Forgerock openbanking UI Images
- Auth UI: ./forgerock-openbanking-ui/projects/auth
- Swagger UI: ./forgerock-openbanking-ui/projects/swagger

## Build the project
Maven build all artifacts included the below UI images:
- auth-ui
- swagger-ui
```bash
mvn clean install
```
```bash
mvn clean install -DskipTests -DdockerCompose.skip
```
