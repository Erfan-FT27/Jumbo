package com.jumbo.customerpanel.config;

public interface EntityToDtoMapper<E, D> {
    D map(E entity);
}
