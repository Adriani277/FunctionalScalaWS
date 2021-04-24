FROM gitpod/workspace-full
USER gitpod
RUN sh -c "$(wget -O- https://github.com/deluan/zsh-in-docker/releases/download/v1.1.1/zsh-in-docker.sh)" -- \
    -p git
RUN brew update
RUN brew install scala sbt
