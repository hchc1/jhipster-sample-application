package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ResponsavelLegal;
import com.mycompany.myapp.repository.ResponsavelLegalRepository;
import com.mycompany.myapp.service.ResponsavelLegalService;
import com.mycompany.myapp.service.dto.ResponsavelLegalDTO;
import com.mycompany.myapp.service.mapper.ResponsavelLegalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ResponsavelLegal}.
 */
@Service
@Transactional
public class ResponsavelLegalServiceImpl implements ResponsavelLegalService {

    private final Logger log = LoggerFactory.getLogger(ResponsavelLegalServiceImpl.class);

    private final ResponsavelLegalRepository responsavelLegalRepository;

    private final ResponsavelLegalMapper responsavelLegalMapper;

    public ResponsavelLegalServiceImpl(
        ResponsavelLegalRepository responsavelLegalRepository,
        ResponsavelLegalMapper responsavelLegalMapper
    ) {
        this.responsavelLegalRepository = responsavelLegalRepository;
        this.responsavelLegalMapper = responsavelLegalMapper;
    }

    @Override
    public ResponsavelLegalDTO save(ResponsavelLegalDTO responsavelLegalDTO) {
        log.debug("Request to save ResponsavelLegal : {}", responsavelLegalDTO);
        ResponsavelLegal responsavelLegal = responsavelLegalMapper.toEntity(responsavelLegalDTO);
        responsavelLegal = responsavelLegalRepository.save(responsavelLegal);
        return responsavelLegalMapper.toDto(responsavelLegal);
    }

    @Override
    public ResponsavelLegalDTO update(ResponsavelLegalDTO responsavelLegalDTO) {
        log.debug("Request to update ResponsavelLegal : {}", responsavelLegalDTO);
        ResponsavelLegal responsavelLegal = responsavelLegalMapper.toEntity(responsavelLegalDTO);
        responsavelLegal = responsavelLegalRepository.save(responsavelLegal);
        return responsavelLegalMapper.toDto(responsavelLegal);
    }

    @Override
    public Optional<ResponsavelLegalDTO> partialUpdate(ResponsavelLegalDTO responsavelLegalDTO) {
        log.debug("Request to partially update ResponsavelLegal : {}", responsavelLegalDTO);

        return responsavelLegalRepository
            .findById(responsavelLegalDTO.getId())
            .map(existingResponsavelLegal -> {
                responsavelLegalMapper.partialUpdate(existingResponsavelLegal, responsavelLegalDTO);

                return existingResponsavelLegal;
            })
            .map(responsavelLegalRepository::save)
            .map(responsavelLegalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResponsavelLegalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResponsavelLegals");
        return responsavelLegalRepository.findAll(pageable).map(responsavelLegalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResponsavelLegalDTO> findOne(Long id) {
        log.debug("Request to get ResponsavelLegal : {}", id);
        return responsavelLegalRepository.findById(id).map(responsavelLegalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ResponsavelLegal : {}", id);
        responsavelLegalRepository.deleteById(id);
    }
}
