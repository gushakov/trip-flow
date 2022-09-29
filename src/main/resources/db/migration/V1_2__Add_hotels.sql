CREATE TABLE public.hotel
(
    hotel_id   varchar NOT NULL,
    name       varchar NOT NULL,
    city       varchar NOT NULL,
    price      int4    NOT NULL,
    image_file varchar NOT NULL,
    CONSTRAINT hotel_pk PRIMARY KEY (hotel_id)
);

INSERT INTO public.hotel
    (hotel_id, name, city, price, image_file)
VALUES ('MDR1', 'The Principal Madrid Hotel', 'Madrid', 325, 'ciudad-maderas-MXbM1NrRqtI-unsplash.jpg');
