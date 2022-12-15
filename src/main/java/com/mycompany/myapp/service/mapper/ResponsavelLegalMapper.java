package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ResponsavelLegal;
import com.mycompany.myapp.service.dto.ResponsavelLegalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponsavelLegal} and its DTO {@link ResponsavelLegalDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponsavelLegalMapper extends EntityMapper<ResponsavelLegalDTO, ResponsavelLegal> {}
