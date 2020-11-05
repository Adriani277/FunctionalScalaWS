FROM gitpod/workspace-full
USER gitpod
RUN brew update
RUN brew install scala sbt