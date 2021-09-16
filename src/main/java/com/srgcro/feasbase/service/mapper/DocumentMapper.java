package com.srgcro.feasbase.service.mapper;

import com.srgcro.feasbase.domain.Document;
import com.srgcro.feasbase.service.dto.DocumentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {}
