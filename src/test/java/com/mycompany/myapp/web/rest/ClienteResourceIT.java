package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.domain.ResponsavelLegal;
import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.repository.ClienteRepository;
import com.mycompany.myapp.service.ClienteService;
import com.mycompany.myapp.service.criteria.ClienteCriteria;
import com.mycompany.myapp.service.dto.ClienteDTO;
import com.mycompany.myapp.service.mapper.ClienteMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ClienteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClienteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_RG = "AAAAAAAAAA";
    private static final String UPDATED_RG = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_BENEFICIO_ATIVO = false;
    private static final Boolean UPDATED_BENEFICIO_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/clientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteRepository clienteRepositoryMock;

    @Autowired
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteService clienteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .rg(DEFAULT_RG)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .beneficioAtivo(DEFAULT_BENEFICIO_ATIVO);
        return cliente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createUpdatedEntity(EntityManager em) {
        Cliente cliente = new Cliente()
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .beneficioAtivo(UPDATED_BENEFICIO_ATIVO);
        return cliente;
    }

    @BeforeEach
    public void initTest() {
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();
        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCliente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testCliente.getRg()).isEqualTo(DEFAULT_RG);
        assertThat(testCliente.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testCliente.getBeneficioAtivo()).isEqualTo(DEFAULT_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void createClienteWithExistingId() throws Exception {
        // Create the Cliente with an existing ID
        cliente.setId(1L);
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setNome(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setCpf(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRgIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setRg(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setDataNascimento(null);

        // Create the Cliente, which fails.
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        restClienteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isBadRequest());

        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].beneficioAtivo").value(hasItem(DEFAULT_BENEFICIO_ATIVO.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientesWithEagerRelationshipsIsEnabled() throws Exception {
        when(clienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clienteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clienteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClienteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clienteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc
            .perform(get(ENTITY_API_URL_ID, cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.rg").value(DEFAULT_RG))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.beneficioAtivo").value(DEFAULT_BENEFICIO_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getClientesByIdFiltering() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        Long id = cliente.getId();

        defaultClienteShouldBeFound("id.equals=" + id);
        defaultClienteShouldNotBeFound("id.notEquals=" + id);

        defaultClienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.greaterThan=" + id);

        defaultClienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientesByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome equals to DEFAULT_NOME
        defaultClienteShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the clienteList where nome equals to UPDATED_NOME
        defaultClienteShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClientesByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultClienteShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the clienteList where nome equals to UPDATED_NOME
        defaultClienteShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClientesByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome is not null
        defaultClienteShouldBeFound("nome.specified=true");

        // Get all the clienteList where nome is null
        defaultClienteShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByNomeContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome contains DEFAULT_NOME
        defaultClienteShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the clienteList where nome contains UPDATED_NOME
        defaultClienteShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClientesByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where nome does not contain DEFAULT_NOME
        defaultClienteShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the clienteList where nome does not contain UPDATED_NOME
        defaultClienteShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllClientesByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cpf equals to DEFAULT_CPF
        defaultClienteShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the clienteList where cpf equals to UPDATED_CPF
        defaultClienteShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllClientesByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultClienteShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the clienteList where cpf equals to UPDATED_CPF
        defaultClienteShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllClientesByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cpf is not null
        defaultClienteShouldBeFound("cpf.specified=true");

        // Get all the clienteList where cpf is null
        defaultClienteShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByCpfContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cpf contains DEFAULT_CPF
        defaultClienteShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the clienteList where cpf contains UPDATED_CPF
        defaultClienteShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllClientesByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where cpf does not contain DEFAULT_CPF
        defaultClienteShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the clienteList where cpf does not contain UPDATED_CPF
        defaultClienteShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllClientesByRgIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rg equals to DEFAULT_RG
        defaultClienteShouldBeFound("rg.equals=" + DEFAULT_RG);

        // Get all the clienteList where rg equals to UPDATED_RG
        defaultClienteShouldNotBeFound("rg.equals=" + UPDATED_RG);
    }

    @Test
    @Transactional
    void getAllClientesByRgIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rg in DEFAULT_RG or UPDATED_RG
        defaultClienteShouldBeFound("rg.in=" + DEFAULT_RG + "," + UPDATED_RG);

        // Get all the clienteList where rg equals to UPDATED_RG
        defaultClienteShouldNotBeFound("rg.in=" + UPDATED_RG);
    }

    @Test
    @Transactional
    void getAllClientesByRgIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rg is not null
        defaultClienteShouldBeFound("rg.specified=true");

        // Get all the clienteList where rg is null
        defaultClienteShouldNotBeFound("rg.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByRgContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rg contains DEFAULT_RG
        defaultClienteShouldBeFound("rg.contains=" + DEFAULT_RG);

        // Get all the clienteList where rg contains UPDATED_RG
        defaultClienteShouldNotBeFound("rg.contains=" + UPDATED_RG);
    }

    @Test
    @Transactional
    void getAllClientesByRgNotContainsSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where rg does not contain DEFAULT_RG
        defaultClienteShouldNotBeFound("rg.doesNotContain=" + DEFAULT_RG);

        // Get all the clienteList where rg does not contain UPDATED_RG
        defaultClienteShouldBeFound("rg.doesNotContain=" + UPDATED_RG);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento equals to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento in DEFAULT_DATA_NASCIMENTO or UPDATED_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento equals to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.in=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is not null
        defaultClienteShouldBeFound("dataNascimento.specified=true");

        // Get all the clienteList where dataNascimento is null
        defaultClienteShouldNotBeFound("dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is greater than or equal to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is greater than or equal to UPDATED_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is less than or equal to DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is less than or equal to SMALLER_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is less than DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is less than UPDATED_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where dataNascimento is greater than DEFAULT_DATA_NASCIMENTO
        defaultClienteShouldNotBeFound("dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO);

        // Get all the clienteList where dataNascimento is greater than SMALLER_DATA_NASCIMENTO
        defaultClienteShouldBeFound("dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllClientesByBeneficioAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where beneficioAtivo equals to DEFAULT_BENEFICIO_ATIVO
        defaultClienteShouldBeFound("beneficioAtivo.equals=" + DEFAULT_BENEFICIO_ATIVO);

        // Get all the clienteList where beneficioAtivo equals to UPDATED_BENEFICIO_ATIVO
        defaultClienteShouldNotBeFound("beneficioAtivo.equals=" + UPDATED_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void getAllClientesByBeneficioAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where beneficioAtivo in DEFAULT_BENEFICIO_ATIVO or UPDATED_BENEFICIO_ATIVO
        defaultClienteShouldBeFound("beneficioAtivo.in=" + DEFAULT_BENEFICIO_ATIVO + "," + UPDATED_BENEFICIO_ATIVO);

        // Get all the clienteList where beneficioAtivo equals to UPDATED_BENEFICIO_ATIVO
        defaultClienteShouldNotBeFound("beneficioAtivo.in=" + UPDATED_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void getAllClientesByBeneficioAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clienteList where beneficioAtivo is not null
        defaultClienteShouldBeFound("beneficioAtivo.specified=true");

        // Get all the clienteList where beneficioAtivo is null
        defaultClienteShouldNotBeFound("beneficioAtivo.specified=false");
    }

    @Test
    @Transactional
    void getAllClientesByResponsavelLegalIsEqualToSomething() throws Exception {
        ResponsavelLegal responsavelLegal;
        if (TestUtil.findAll(em, ResponsavelLegal.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            responsavelLegal = ResponsavelLegalResourceIT.createEntity(em);
        } else {
            responsavelLegal = TestUtil.findAll(em, ResponsavelLegal.class).get(0);
        }
        em.persist(responsavelLegal);
        em.flush();
        cliente.setResponsavelLegal(responsavelLegal);
        clienteRepository.saveAndFlush(cliente);
        Long responsavelLegalId = responsavelLegal.getId();

        // Get all the clienteList where responsavelLegal equals to responsavelLegalId
        defaultClienteShouldBeFound("responsavelLegalId.equals=" + responsavelLegalId);

        // Get all the clienteList where responsavelLegal equals to (responsavelLegalId + 1)
        defaultClienteShouldNotBeFound("responsavelLegalId.equals=" + (responsavelLegalId + 1));
    }

    @Test
    @Transactional
    void getAllClientesByTagsIsEqualToSomething() throws Exception {
        Tag tags;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            clienteRepository.saveAndFlush(cliente);
            tags = TagResourceIT.createEntity(em);
        } else {
            tags = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tags);
        em.flush();
        cliente.addTags(tags);
        clienteRepository.saveAndFlush(cliente);
        Long tagsId = tags.getId();

        // Get all the clienteList where tags equals to tagsId
        defaultClienteShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the clienteList where tags equals to (tagsId + 1)
        defaultClienteShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClienteShouldBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].beneficioAtivo").value(hasItem(DEFAULT_BENEFICIO_ATIVO.booleanValue())));

        // Check, that the count call also returns 1
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClienteShouldNotBeFound(String filter) throws Exception {
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findById(cliente.getId()).get();
        // Disconnect from session so that the updates on updatedCliente are not directly saved in db
        em.detach(updatedCliente);
        updatedCliente
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .beneficioAtivo(UPDATED_BENEFICIO_ATIVO);
        ClienteDTO clienteDTO = clienteMapper.toDto(updatedCliente);

        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCliente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testCliente.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testCliente.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCliente.getBeneficioAtivo()).isEqualTo(UPDATED_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void putNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente.rg(UPDATED_RG);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCliente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testCliente.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testCliente.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testCliente.getBeneficioAtivo()).isEqualTo(DEFAULT_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void fullUpdateClienteWithPatch() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente using partial update
        Cliente partialUpdatedCliente = new Cliente();
        partialUpdatedCliente.setId(cliente.getId());

        partialUpdatedCliente
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .beneficioAtivo(UPDATED_BENEFICIO_ATIVO);

        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCliente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCliente))
            )
            .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clienteList.get(clienteList.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCliente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testCliente.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testCliente.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testCliente.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testCliente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCliente.getBeneficioAtivo()).isEqualTo(UPDATED_BENEFICIO_ATIVO);
    }

    @Test
    @Transactional
    void patchNonExistingCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clienteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCliente() throws Exception {
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();
        cliente.setId(count.incrementAndGet());

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.toDto(cliente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClienteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clienteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cliente in the database
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Delete the cliente
        restClienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cliente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cliente> clienteList = clienteRepository.findAll();
        assertThat(clienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
