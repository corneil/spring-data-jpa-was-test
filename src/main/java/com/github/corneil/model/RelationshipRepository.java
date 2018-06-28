package com.github.corneil.model;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RelationshipRepository extends PagingAndSortingRepository<Relationship, Long>,
	QueryDslPredicateExecutor<Relationship> {
}
