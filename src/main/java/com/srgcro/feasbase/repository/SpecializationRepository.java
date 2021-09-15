package com.srgcro.feasbase.repository;

import com.srgcro.feasbase.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Specialization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {}
