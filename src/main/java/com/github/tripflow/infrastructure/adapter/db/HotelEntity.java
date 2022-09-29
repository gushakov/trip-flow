package com.github.tripflow.infrastructure.adapter.db;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("hotel")
public class HotelEntity {

    @Column("hotel_id")
    String hotelId;

    @Column("hotel_name")
    String name;

    @Column("city")
    String city;

    @Column("price")
    int price;

    @Column("image_file")
    String imageFile;
}
