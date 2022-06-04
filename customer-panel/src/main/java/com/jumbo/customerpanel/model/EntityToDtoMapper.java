package com.jumbo.customerpanel.model;

/**
 * Helps to increase code quality in transforming entity class to desired DTO
 * @param <E>
 * @param <D>
 */
public interface EntityToDtoMapper<E, D> {
    D map(E entity);
}
