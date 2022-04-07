FROM node:14-alpine as builder
ADD ./ /app
WORKDIR /app
RUN npm i && npm run build;

FROM httpd:alpine as runner

COPY --from=builder /app/build/ /usr/local/apache2/htdocs/
COPY ./httpd.conf /usr/local/apache2/conf/httpd.conf

RUN ls -la /usr/local/apache2/htdocs/

RUN sed -i '/LoadModule rewrite_module/s/^#//g' /usr/local/apache2/conf/httpd.conf

ADD entrypoint.sh /usr/local/apache2/entrypoint.sh

RUN chmod a+x entrypoint.sh

RUN { \
  echo 'IncludeOptional conf.d/*.conf'; \
} >> /usr/local/apache2/conf/httpd.conf \
  && mkdir /usr/local/apache2/conf.d

ENTRYPOINT  ["sh", "entrypoint.sh"]
EXPOSE 80
