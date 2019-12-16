# Stage 1 - the build process
FROM node:10

WORKDIR /src
ADD package.json package-lock.json /src/

RUN NG_CLI_ANALYTICS=ci npm ci

ADD . /src

RUN npm run build.swagger.themes

# Stage 2 - the production environment
FROM nginx:1.13.9-alpine

EXPOSE 80

ARG BUILD_VERSION
ENV BUILD_VERSION ${BUILD_VERSION}
ENV DOMAIN ""
ENV TEMPLATE ""
ENV ENABLE_CUSTOMIZATION "false"

RUN rm -rf /etc/nginx/conf.d
RUN cp /src/dist /usr/share/nginx
COPY projects/swagger/docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY projects/swagger/docker/start.sh /opt/start.sh

CMD ["/opt/start.sh"]
