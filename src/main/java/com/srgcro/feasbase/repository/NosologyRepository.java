package com.srgcro.feasbase.repository;

import com.srgcro.feasbase.domain.Nosology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nosology entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NosologyRepository extends JpaRepository<Nosology, Long> {}
