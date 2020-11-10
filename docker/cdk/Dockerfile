FROM node:lts

RUN apt-get update && \
    apt-get install -y zip && \
    rm -rf /var/lib/apt/lists/*

ENV AWS_CDK_VERSION=1.71.0
RUN mkdir -p /nodejs && \
    npm config set prefix /nodejs/bin && \
    npm install -g aws-cdk@${AWS_CDK_VERSION}

RUN cd /nodejs/bin && \
    zip -r --symlinks /opt/aws-cdk.zip *
RUN cd /usr/local && \
    zip -r /opt/node.zip bin/node
