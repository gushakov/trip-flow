package com.github.tripflow.infrastructure.adapter.db.hotel;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelEntityRepository extends CrudRepository<HotelEntity, String> {

    List<HotelEntity> findAllByCity(String city);

}
