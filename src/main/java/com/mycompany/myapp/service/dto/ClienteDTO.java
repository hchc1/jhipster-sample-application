package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Cliente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String cpf;

    @NotNull
    private String rg;

    @Lob
    private byte[] foto;

    private String fotoContentType;

    @NotNull
    private LocalDate dataNascimento;

    private Boolean beneficioAtivo;

    private ResponsavelLegalDTO responsavelLegal;

    private Set<TagDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Boolean getBeneficioAtivo() {
        return beneficioAtivo;
    }

    public void setBeneficioAtivo(Boolean beneficioAtivo) {
        this.beneficioAtivo = beneficioAtivo;
    }

    public ResponsavelLegalDTO getResponsavelLegal() {
        return responsavelLegal;
    }

    public void setResponsavelLegal(ResponsavelLegalDTO responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", rg='" + getRg() + "'" +
            ", foto='" + getFoto() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", beneficioAtivo='" + getBeneficioAtivo() + "'" +
            ", responsavelLegal=" + getResponsavelLegal() +
            ", tags=" + getTags() +
            "}";
    }
}
