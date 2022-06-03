package com.jumbo.customerpanel.model;

public interface EntityToDtoMapper<E, D> {
    D map(E entity);
}
