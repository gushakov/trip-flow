#FROM postgres:14.5
FROM library/postgres@sha256:8d46fa657b46fb96a707b3dff90ff95014476874a96389f0370c1c2a2846f249

COPY create-multiple-postgresql-databases.sh /docker-entrypoint-initdb.d/

ENV POSTGRES_MULTIPLE_DATABASES=tripflow
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres