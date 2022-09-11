package com.github.tripflow.infrastructure.adapter.db.map;

import com.github.tripflow.infrastructure.map.CommonMapStructConverters;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonMapStructConverters.class})
public class MapStructTripDbMapper {



}
