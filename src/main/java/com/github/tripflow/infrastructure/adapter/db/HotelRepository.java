package com.github.tripflow.infrastructure.adapter.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelRepository extends CrudRepository<HotelEntity, String> {

    List<HotelEntity> findAllByCity(String city);

}
