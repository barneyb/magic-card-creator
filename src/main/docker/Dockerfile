#
# docker build -t magic-card-creator src/main/docker/
# docker run -i -v /Users/bboisvert/Documents/IdeaProjects/card-creator/src/main/design:/src -v /Users/bboisvert/Documents/IdeaProjects/card-creator/build/:/target magic-card-creator
#

FROM        centos
MAINTAINER  Barney Boisvert <bboisvert@gmail.com>

RUN         yum install -x systemd* --skip-broken -y librsvg2-tools

COPY        convert.sh /
RUN         chmod o+x /convert.sh

VOLUME      ["/src", "/target"]

ENTRYPOINT  ["bash", "/convert.sh", "/src", "/target"]
CMD         ["png:400"]
