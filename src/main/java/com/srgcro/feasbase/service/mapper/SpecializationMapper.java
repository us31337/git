package com.srgcro.feasbase.service.mapper;

import com.srgcro.feasbase.domain.Specialization;
import com.srgcro.feasbase.service.dto.SpecializationDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Specialization} and its DTO {@link SpecializationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpecializationMapper extends EntityMapper<SpecializationDTO, Specialization> {
    @Mapping(source = "name", target = "name", qualifiedByName = "nameToLowerCase")
    public Specialization toEntity(SpecializationDTO dto);

    @Named("nameToLowerCase")
    public static String nameToLowerCase(String name) {
        return StringUtils.strip(name).toLowerCase();
    }
}
