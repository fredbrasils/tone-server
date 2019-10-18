package com.tone.repository;

import com.tone.model.SocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,E> extends PagingAndSortingRepository<T, E> {

	Optional<List<T>> findOptionalByNameContainingIgnoreCase(String name);

	Optional<List<T>> findAllOptionalByStatus(StatusEnum status);
}
