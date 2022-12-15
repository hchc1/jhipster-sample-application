package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ResponsavelLegalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.ResponsavelLegal}.
 */
public interface ResponsavelLegalService {
    /**
     * Save a responsavelLegal.
     *
     * @param responsavelLegalDTO the entity to save.
     * @return the persisted entity.
     */
    ResponsavelLegalDTO save(ResponsavelLegalDTO responsavelLegalDTO);

    /**
     * Updates a responsavelLegal.
     *
     * @param responsavelLegalDTO the entity to update.
     * @return the persisted entity.
     */
    ResponsavelLegalDTO update(ResponsavelLegalDTO responsavelLegalDTO);

    /**
     * Partially updates a responsavelLegal.
     *
     * @param responsavelLegalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResponsavelLegalDTO> partialUpdate(ResponsavelLegalDTO responsavelLegalDTO);

    /**
     * Get all the responsavelLegals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResponsavelLegalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" responsavelLegal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResponsavelLegalDTO> findOne(Long id);

    /**
     * Delete the "id" responsavelLegal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
