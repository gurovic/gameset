FROM gcc:12 AS build

WORKDIR /build

RUN apt -y update && apt install -y

ADD ./example_game /build/example_game
ADD ./interactor_lib /build/interactor_lib
ADD ./Makefile /build/Makefile

#RUN apt-get update && \
#    apt-get install -y \
#      libboost-dev libboost-program-options-dev \
#      libgtest-dev \
#      cmake \
#    && \
#    cmake -DCMAKE_BUILD_TYPE=Release /usr/src/gtest && \
#    cmake --build . && \
#    mv lib*.a /usr/lib

#RUN g++ src/interactor.cpp -o main -std=c++20
#-O3 -Wall -Wextra -Werror -pedantic -Wno-unused-result -Wno-unused-function -Wno-unused-variable -Wno-unused-but-set-variable -Wno-sign-compare -Wno-maybe-uninitialized -Wno-unknown-pragmas -Wno-psabi -Wno-attributes -Wno-unknown-warning-option -Wno-ignored-attributes

#FROM debian:latest AS run
#WORKDIR /app
#COPY --from=build /build/main .
#RUN groupadd -r runner && useradd -r -g runner runner
#USER runner

RUN make

CMD ["make", "run"]

