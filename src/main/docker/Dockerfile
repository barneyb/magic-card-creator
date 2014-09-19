#
# docker build -t magic-card-creator src/main/docker/
# docker run --rm -i -p 8080:8080 magic-card-creator
# curl -F svg=@build/1.svg -o build/1.png http://localhost:8080/convert/png?w=400
# curl -F svg=@build/1.svg -o build/1.pdf http://localhost:8080/convert/pdf?r=east
# for i in `find build -maxdepth 1 -name "*.svg"`; do curl -F svg=@$i -o $i.png http://localhost:8080/convert/png; done
# for i in `find build -maxdepth 1 -name "*.svg"`; do curl -F svg=@$i -o $i.pdf http://localhost:8080/convert/pdf; done
#

FROM        centos:centos6
MAINTAINER  Barney Boisvert <bboisvert@gmail.com>

RUN         yum install -x systemd* --skip-broken -y librsvg2
RUN         rpm -Uvh http://download.fedoraproject.org/pub/epel/6/i386/epel-release-6-8.noarch.rpm
RUN         yum install -y npm

RUN         yum install -x systemd* --skip-broken -y libgcj
RUN         rpm -Uvh https://www.pdflabs.com/tools/pdftk-the-pdf-toolkit/pdftk-2.02-1.el6.i686.rpm

ADD         . /src
WORKDIR     /src
RUN         npm install

EXPOSE      8080
CMD         ["npm", "start"]
