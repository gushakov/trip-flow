package com.github.tripflow.infrastructure.adapter.db.hotel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("hotel")
public class HotelEntity {

    @Id
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
