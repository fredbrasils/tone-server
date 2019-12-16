package com.tone.repository;

import com.tone.model.*;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LuthierFeatureRepository extends PagingAndSortingRepository<LuthierFeatureEntity, Long> {

    Optional<List<LuthierFeatureEntity>> findAllOptionalByFeature(FeatureEntity feature);

    Optional<List<LuthierFeatureEntity>> findAllOptionalByLuthier(LuthierEntity luthier);
}
