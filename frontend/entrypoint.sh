#!/bin/sh
echo " Replacing variables... ";
sed -i /usr/local/apache2/htdocs/static/js/* -e "s|API_URL_TO_REPLACE|${API_URL}|g";
echo " Start application... ";
httpd-foreground