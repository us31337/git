package com.srgcro.feasbase.service;

import com.srgcro.feasbase.domain.Document;
import com.srgcro.feasbase.repository.DocumentRepository;
import com.srgcro.feasbase.repository.UserRepository;
import com.srgcro.feasbase.service.dto.DocumentDTO;
import com.srgcro.feasbase.service.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final UserRepository userRepository;

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        Long userId = documentDTO.getUser().getId();
        userRepository.findById(userId).ifPresent(document::user);
        document = documentRepository.save(document);
        return documentMapper.toDto(document);
    }

    /**
     * Partially update a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        log.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(
                existingDocument -> {
                    documentMapper.partialUpdate(existingDocument, documentDTO);

                    return existingDocument;
                }
            )
            .map(documentRepository::save)
            .map(documentMapper::toDto);
    }

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findAll().stream().map(documentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    /**
     * Delete the document by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }
}
