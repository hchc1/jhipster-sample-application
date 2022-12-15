package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Cliente} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cpf;

    private StringFilter rg;

    private LocalDateFilter dataNascimento;

    private BooleanFilter beneficioAtivo;

    private LongFilter responsavelLegalId;

    private LongFilter tagsId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.rg = other.rg == null ? null : other.rg.copy();
        this.dataNascimento = other.dataNascimento == null ? null : other.dataNascimento.copy();
        this.beneficioAtivo = other.beneficioAtivo == null ? null : other.beneficioAtivo.copy();
        this.responsavelLegalId = other.responsavelLegalId == null ? null : other.responsavelLegalId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public StringFilter nome() {
        if (nome == null) {
            nome = new StringFilter();
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getRg() {
        return rg;
    }

    public StringFilter rg() {
        if (rg == null) {
            rg = new StringFilter();
        }
        return rg;
    }

    public void setRg(StringFilter rg) {
        this.rg = rg;
    }

    public LocalDateFilter getDataNascimento() {
        return dataNascimento;
    }

    public LocalDateFilter dataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new LocalDateFilter();
        }
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public BooleanFilter getBeneficioAtivo() {
        return beneficioAtivo;
    }

    public BooleanFilter beneficioAtivo() {
        if (beneficioAtivo == null) {
            beneficioAtivo = new BooleanFilter();
        }
        return beneficioAtivo;
    }

    public void setBeneficioAtivo(BooleanFilter beneficioAtivo) {
        this.beneficioAtivo = beneficioAtivo;
    }

    public LongFilter getResponsavelLegalId() {
        return responsavelLegalId;
    }

    public LongFilter responsavelLegalId() {
        if (responsavelLegalId == null) {
            responsavelLegalId = new LongFilter();
        }
        return responsavelLegalId;
    }

    public void setResponsavelLegalId(LongFilter responsavelLegalId) {
        this.responsavelLegalId = responsavelLegalId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            tagsId = new LongFilter();
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(rg, that.rg) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(beneficioAtivo, that.beneficioAtivo) &&
            Objects.equals(responsavelLegalId, that.responsavelLegalId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cpf, rg, dataNascimento, beneficioAtivo, responsavelLegalId, tagsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nome != null ? "nome=" + nome + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (rg != null ? "rg=" + rg + ", " : "") +
            (dataNascimento != null ? "dataNascimento=" + dataNascimento + ", " : "") +
            (beneficioAtivo != null ? "beneficioAtivo=" + beneficioAtivo + ", " : "") +
            (responsavelLegalId != null ? "responsavelLegalId=" + responsavelLegalId + ", " : "") +
            (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
