#
# docker build -t magic-card-creator src/main/docker/
# docker run --rm -i -p 8080:8080 magic-card-creator
# curl -F svg=@src/main/design/frame.svg -o build/frame.png http://192.168.59.103:8080/convert/png
# curl -F svg=@src/main/design/frame.svg -o build/frame.pdf http://192.168.59.103:8080/convert/pdf
#

FROM        centos:centos6
MAINTAINER  Barney Boisvert <bboisvert@gmail.com>

RUN         yum install -x systemd* --skip-broken -y librsvg2
RUN         rpm -Uvh http://download.fedoraproject.org/pub/epel/6/i386/epel-release-6-8.noarch.rpm
RUN         yum install -y npm

ADD         . /src
WORKDIR     /src
RUN         npm install

EXPOSE      8080
CMD         ["npm", "start"]
