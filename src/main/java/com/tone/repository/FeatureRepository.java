package com.tone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.FeatureEntity;
import com.tone.model.enumm.StatusEnum;

public interface FeatureRepository extends BaseRepository<FeatureEntity, Long> {

	@EntityGraph(attributePaths = { "features" })
	Optional<List<FeatureEntity>> findOptionalByNameContainingIgnoreCase(String name);

	@Query("select f from FeatureEntity f left join fetch f.features where f.root is null")
	Iterable<FeatureEntity> findAll();
}
