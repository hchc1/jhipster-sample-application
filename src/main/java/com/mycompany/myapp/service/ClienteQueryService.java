package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.service.criteria.ClienteCriteria;
import com.mycompany.myapp.service.dto.ClienteDTO;
import com.mycompany.myapp.service.mapper.ClienteMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cliente} entities in the database.
 * The main input is a {@link ClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClienteDTO} or a {@link Page} of {@link ClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private final Logger log = LoggerFactory.getLogger(ClienteQueryService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteQueryService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Return a {@link List} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByCriteria(ClienteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteMapper.toDto(clienteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByCriteria(ClienteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page).map(clienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClienteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.count(specification);
    }

    /**
     * Function to convert {@link ClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cliente> createSpecification(ClienteCriteria criteria) {
        Specification<Cliente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cliente_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Cliente_.nome));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Cliente_.cpf));
            }
            if (criteria.getRg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRg(), Cliente_.rg));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Cliente_.dataNascimento));
            }
            if (criteria.getBeneficioAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getBeneficioAtivo(), Cliente_.beneficioAtivo));
            }
            if (criteria.getResponsavelLegalId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getResponsavelLegalId(),
                            root -> root.join(Cliente_.responsavelLegal, JoinType.LEFT).get(ResponsavelLegal_.id)
                        )
                    );
            }
            if (criteria.getTagsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTagsId(), root -> root.join(Cliente_.tags, JoinType.LEFT).get(Tag_.id))
                    );
            }
        }
        return specification;
    }
}
