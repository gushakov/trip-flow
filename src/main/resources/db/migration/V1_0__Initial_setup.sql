CREATE TABLE public.activated_jobs
(
    job_key         int8    NOT NULL,
    "name"          varchar NOT NULL,
    "type"          varchar NOT NULL,
    trip_id         int8    NOT NULL,
    trip_started_by varchar NOT NULL,
    "action"        varchar NOT NULL,
    CONSTRAINT activated_jobs_pk PRIMARY KEY (job_key)
);

CREATE TABLE public.flight
(
    flight_number         varchar NOT NULL,
    airline               varchar NOT NULL,
    origin_city           varchar NOT NULL,
    origin_iata_code      varchar NOT NULL,
    destination_city      varchar NOT NULL,
    destination_iata_code varchar NOT NULL,
    price                 int4    NOT NULL,
    CONSTRAINT flight_pk PRIMARY KEY (flight_number)
);

CREATE TABLE public.hotel
(
    hotel_id   varchar NOT NULL,
    hotel_name varchar NOT NULL,
    city       varchar NOT NULL,
    price      int4    NOT NULL,
    image_file varchar NOT NULL,
    CONSTRAINT hotel_pk PRIMARY KEY (hotel_id)
);

CREATE TABLE public.trip
(
    trip_id        int8    NOT NULL,
    started_by     varchar NOT NULL,
    flight_number  varchar NULL,
    hotel_id       varchar NULL,
    flight_booked  boolean NOT NULL DEFAULT false,
    hotel_reserved boolean NOT NULL DEFAULT false,
    trip_refused   boolean NOT NULL DEFAULT false,
    trip_confirmed boolean NOT NULL DEFAULT false,
    CONSTRAINT trip_pk PRIMARY KEY (trip_id)
);

ALTER TABLE public.trip
    ADD CONSTRAINT trip_flight_fk FOREIGN KEY (flight_number) REFERENCES public.flight (flight_number);
ALTER TABLE public.trip
    ADD CONSTRAINT trip_hotel_fk FOREIGN KEY (hotel_id) REFERENCES public.hotel (hotel_id);
