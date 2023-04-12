FROM amazoncorretto:17-alpine3.17

COPY build/distributions/gameserver-shadow-0.0.1.tar /app/gameserver-shadow-0.0.1.tar
WORKDIR /app
RUN tar -xf gameserver-shadow-0.0.1.tar
RUN rm gameserver-shadow-0.0.1.tar

EXPOSE 50624

ENTRYPOINT ["/app/gameserver-shadow-0.0.1/bin/gameserver"]