ALTER TABLE public.trip
    ADD flight_number varchar NULL;
ALTER TABLE public.trip
    ADD CONSTRAINT trip_flight_fk FOREIGN KEY (flight_number) REFERENCES public.flight (flight_number);
