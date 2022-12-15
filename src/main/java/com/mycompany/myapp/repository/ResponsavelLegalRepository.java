package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ResponsavelLegal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResponsavelLegal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsavelLegalRepository extends JpaRepository<ResponsavelLegal, Long>, JpaSpecificationExecutor<ResponsavelLegal> {}
