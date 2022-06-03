package com.jumbo.map.repository;

import com.jumbo.map.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
//TODO remove it
public interface StoreRepository extends MongoRepository<Store, String> {

    Page<Store> findByLocationNear(Point point, Pageable pageable);

}
