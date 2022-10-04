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
    private String hotelId;

    @Column("hotel_name")
    private String name;

    @Column("city")
    private String city;

    @Column("price")
    private int price;

    @Column("image_file")
    private String imageFile;
}
