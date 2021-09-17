package com.srgcro.feasbase.service.mapper;

import com.srgcro.feasbase.domain.Document;
import com.srgcro.feasbase.service.dto.DocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    DocumentDTO toDto(Document s);
}
