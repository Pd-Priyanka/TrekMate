package com.trekmate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.trekmate.entity.Trek;

public interface TrekRepository extends JpaRepository<Trek, Long>, JpaSpecificationExecutor<Trek> {
    boolean existsBySlugIgnoreCase(String slug);

    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);
}
