FROM node:10

WORKDIR /src
ADD package.json package-lock.json /src/

RUN NG_CLI_ANALYTICS=ci npm ci

ADD . /src