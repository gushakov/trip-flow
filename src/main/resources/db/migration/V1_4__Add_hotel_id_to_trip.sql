ALTER TABLE public.trip ADD hotel_id varchar NULL;
ALTER TABLE public.trip ADD CONSTRAINT trip_hotel_fk FOREIGN KEY (hotel_id) REFERENCES public.hotel(hotel_id);
