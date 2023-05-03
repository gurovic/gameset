FROM gcc:latest AS build

WORKDIR /build

RUN apt -y update && apt install -y

ADD ./example_game /build/example_game
ADD ./interactor_lib /build/interactor_lib
ADD ./Makefile /build/Makefile

#FROM debian:latest AS run
#WORKDIR /app
#COPY --from=build /build/ .
#RUN groupadd -r runner && useradd -r -g runner runner
#USER runner

RUN make clean
RUN make game

CMD ["make", "run-game"]
