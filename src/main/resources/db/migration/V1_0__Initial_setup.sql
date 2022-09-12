CREATE TABLE public.flight
(
    fight_number          varchar NOT NULL,
    airline               varchar NOT NULL,
    origin_city           varchar NOT NULL,
    origin_iata_code      varchar NOT NULL,
    destination_city      varchar NOT NULL,
    destination_iata_code varchar NOT NULL,
    price                 int4    NOT NULL,
    CONSTRAINT flight_pk PRIMARY KEY (fight_number)
);

INSERT INTO public.flight
(fight_number, airline, origin_city, origin_iata_code, destination_city, destination_iata_code, price)
VALUES ('LX1712', 'SWISS', 'Zurich', 'ZRH', 'Naples', 'NAP', 301);

INSERT INTO public.flight
(fight_number, airline, origin_city, origin_iata_code, destination_city, destination_iata_code, price)
VALUES ('U21343', 'SWISS', 'Geneva', 'GVA', 'Rome', 'FCO', 133);

INSERT INTO public.flight
(fight_number, airline, origin_city, origin_iata_code, destination_city, destination_iata_code, price)
VALUES ('IB3403', 'Iberia', 'Paris', 'ORY', 'Madrid', 'MAD', 122);

INSERT INTO public.flight
(fight_number, airline, origin_city, origin_iata_code, destination_city, destination_iata_code, price)
VALUES ('LX1622', 'SWISS', 'Zurich', 'ZRH', 'Milan', 'MXP', 193);

CREATE TABLE public.trip
(
    trip_id    bigint  NOT NULL,
    started_by varchar NOT NULL,
    CONSTRAINT trip_pk PRIMARY KEY (trip_id)
);
