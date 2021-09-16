package com.srgcro.feasbase.service.mapper;

import com.srgcro.feasbase.domain.Nosology;
import com.srgcro.feasbase.service.dto.NosologyDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Nosology} and its DTO {@link NosologyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NosologyMapper extends EntityMapper<NosologyDTO, Nosology> {

    @Mapping(source = "name", target = "name", qualifiedByName = "nameToLowerCaseAndStrip")
    public Nosology toEntity (NosologyDTO dto);

    @Named("nameToLowerCaseAndStrip")
    public static String nameToLowerCaseAndStrip(String name) {
        return StringUtils.strip(name).toLowerCase();
    }
}
