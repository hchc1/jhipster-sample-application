package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ResponsavelLegal;
import com.mycompany.myapp.repository.ResponsavelLegalRepository;
import com.mycompany.myapp.service.criteria.ResponsavelLegalCriteria;
import com.mycompany.myapp.service.dto.ResponsavelLegalDTO;
import com.mycompany.myapp.service.mapper.ResponsavelLegalMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ResponsavelLegalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResponsavelLegalResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsavel-legals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResponsavelLegalRepository responsavelLegalRepository;

    @Autowired
    private ResponsavelLegalMapper responsavelLegalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponsavelLegalMockMvc;

    private ResponsavelLegal responsavelLegal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsavelLegal createEntity(EntityManager em) {
        ResponsavelLegal responsavelLegal = new ResponsavelLegal().nome(DEFAULT_NOME).cpf(DEFAULT_CPF);
        return responsavelLegal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsavelLegal createUpdatedEntity(EntityManager em) {
        ResponsavelLegal responsavelLegal = new ResponsavelLegal().nome(UPDATED_NOME).cpf(UPDATED_CPF);
        return responsavelLegal;
    }

    @BeforeEach
    public void initTest() {
        responsavelLegal = createEntity(em);
    }

    @Test
    @Transactional
    void createResponsavelLegal() throws Exception {
        int databaseSizeBeforeCreate = responsavelLegalRepository.findAll().size();
        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);
        restResponsavelLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeCreate + 1);
        ResponsavelLegal testResponsavelLegal = responsavelLegalList.get(responsavelLegalList.size() - 1);
        assertThat(testResponsavelLegal.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testResponsavelLegal.getCpf()).isEqualTo(DEFAULT_CPF);
    }

    @Test
    @Transactional
    void createResponsavelLegalWithExistingId() throws Exception {
        // Create the ResponsavelLegal with an existing ID
        responsavelLegal.setId(1L);
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        int databaseSizeBeforeCreate = responsavelLegalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsavelLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsavelLegalRepository.findAll().size();
        // set the field null
        responsavelLegal.setNome(null);

        // Create the ResponsavelLegal, which fails.
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        restResponsavelLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsavelLegalRepository.findAll().size();
        // set the field null
        responsavelLegal.setCpf(null);

        // Create the ResponsavelLegal, which fails.
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        restResponsavelLegalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResponsavelLegals() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsavelLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)));
    }

    @Test
    @Transactional
    void getResponsavelLegal() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get the responsavelLegal
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL_ID, responsavelLegal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responsavelLegal.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF));
    }

    @Test
    @Transactional
    void getResponsavelLegalsByIdFiltering() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        Long id = responsavelLegal.getId();

        defaultResponsavelLegalShouldBeFound("id.equals=" + id);
        defaultResponsavelLegalShouldNotBeFound("id.notEquals=" + id);

        defaultResponsavelLegalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultResponsavelLegalShouldNotBeFound("id.greaterThan=" + id);

        defaultResponsavelLegalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultResponsavelLegalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where nome equals to DEFAULT_NOME
        defaultResponsavelLegalShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the responsavelLegalList where nome equals to UPDATED_NOME
        defaultResponsavelLegalShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultResponsavelLegalShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the responsavelLegalList where nome equals to UPDATED_NOME
        defaultResponsavelLegalShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where nome is not null
        defaultResponsavelLegalShouldBeFound("nome.specified=true");

        // Get all the responsavelLegalList where nome is null
        defaultResponsavelLegalShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByNomeContainsSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where nome contains DEFAULT_NOME
        defaultResponsavelLegalShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the responsavelLegalList where nome contains UPDATED_NOME
        defaultResponsavelLegalShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where nome does not contain DEFAULT_NOME
        defaultResponsavelLegalShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the responsavelLegalList where nome does not contain UPDATED_NOME
        defaultResponsavelLegalShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where cpf equals to DEFAULT_CPF
        defaultResponsavelLegalShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the responsavelLegalList where cpf equals to UPDATED_CPF
        defaultResponsavelLegalShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultResponsavelLegalShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the responsavelLegalList where cpf equals to UPDATED_CPF
        defaultResponsavelLegalShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where cpf is not null
        defaultResponsavelLegalShouldBeFound("cpf.specified=true");

        // Get all the responsavelLegalList where cpf is null
        defaultResponsavelLegalShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByCpfContainsSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where cpf contains DEFAULT_CPF
        defaultResponsavelLegalShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the responsavelLegalList where cpf contains UPDATED_CPF
        defaultResponsavelLegalShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllResponsavelLegalsByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        // Get all the responsavelLegalList where cpf does not contain DEFAULT_CPF
        defaultResponsavelLegalShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the responsavelLegalList where cpf does not contain UPDATED_CPF
        defaultResponsavelLegalShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultResponsavelLegalShouldBeFound(String filter) throws Exception {
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsavelLegal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)));

        // Check, that the count call also returns 1
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultResponsavelLegalShouldNotBeFound(String filter) throws Exception {
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restResponsavelLegalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingResponsavelLegal() throws Exception {
        // Get the responsavelLegal
        restResponsavelLegalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponsavelLegal() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();

        // Update the responsavelLegal
        ResponsavelLegal updatedResponsavelLegal = responsavelLegalRepository.findById(responsavelLegal.getId()).get();
        // Disconnect from session so that the updates on updatedResponsavelLegal are not directly saved in db
        em.detach(updatedResponsavelLegal);
        updatedResponsavelLegal.nome(UPDATED_NOME).cpf(UPDATED_CPF);
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(updatedResponsavelLegal);

        restResponsavelLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsavelLegalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
        ResponsavelLegal testResponsavelLegal = responsavelLegalList.get(responsavelLegalList.size() - 1);
        assertThat(testResponsavelLegal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testResponsavelLegal.getCpf()).isEqualTo(UPDATED_CPF);
    }

    @Test
    @Transactional
    void putNonExistingResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsavelLegalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponsavelLegalWithPatch() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();

        // Update the responsavelLegal using partial update
        ResponsavelLegal partialUpdatedResponsavelLegal = new ResponsavelLegal();
        partialUpdatedResponsavelLegal.setId(responsavelLegal.getId());

        partialUpdatedResponsavelLegal.nome(UPDATED_NOME).cpf(UPDATED_CPF);

        restResponsavelLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsavelLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsavelLegal))
            )
            .andExpect(status().isOk());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
        ResponsavelLegal testResponsavelLegal = responsavelLegalList.get(responsavelLegalList.size() - 1);
        assertThat(testResponsavelLegal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testResponsavelLegal.getCpf()).isEqualTo(UPDATED_CPF);
    }

    @Test
    @Transactional
    void fullUpdateResponsavelLegalWithPatch() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();

        // Update the responsavelLegal using partial update
        ResponsavelLegal partialUpdatedResponsavelLegal = new ResponsavelLegal();
        partialUpdatedResponsavelLegal.setId(responsavelLegal.getId());

        partialUpdatedResponsavelLegal.nome(UPDATED_NOME).cpf(UPDATED_CPF);

        restResponsavelLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsavelLegal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsavelLegal))
            )
            .andExpect(status().isOk());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
        ResponsavelLegal testResponsavelLegal = responsavelLegalList.get(responsavelLegalList.size() - 1);
        assertThat(testResponsavelLegal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testResponsavelLegal.getCpf()).isEqualTo(UPDATED_CPF);
    }

    @Test
    @Transactional
    void patchNonExistingResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responsavelLegalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponsavelLegal() throws Exception {
        int databaseSizeBeforeUpdate = responsavelLegalRepository.findAll().size();
        responsavelLegal.setId(count.incrementAndGet());

        // Create the ResponsavelLegal
        ResponsavelLegalDTO responsavelLegalDTO = responsavelLegalMapper.toDto(responsavelLegal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelLegalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsavelLegalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsavelLegal in the database
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponsavelLegal() throws Exception {
        // Initialize the database
        responsavelLegalRepository.saveAndFlush(responsavelLegal);

        int databaseSizeBeforeDelete = responsavelLegalRepository.findAll().size();

        // Delete the responsavelLegal
        restResponsavelLegalMockMvc
            .perform(delete(ENTITY_API_URL_ID, responsavelLegal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResponsavelLegal> responsavelLegalList = responsavelLegalRepository.findAll();
        assertThat(responsavelLegalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
