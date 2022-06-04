package com.jumbo.map.repository;

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.jumbo.map.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class RSQLStoreRepository {

    private final MongoTemplate mongoTemplate;

    public Page<Store> loadAllNearBy(String rsqlSearch, Point point, Pageable pageable) {
        Criteria rsqlCriteria = buildRSQLCriteria(rsqlSearch);
        Query realQuery = buildRealQuery(point, pageable, rsqlCriteria);
        Query countQuery = buildCountQuery(point, rsqlCriteria);

        return new PageImpl<>(mongoTemplate.find(realQuery, Store.class)
                , pageable, mongoTemplate.count(countQuery, Store.class));
    }

    private Query buildRealQuery(Point point, Pageable pageable, Criteria rsqlCriteria) {
        return Query.query(rsqlCriteria)
                .addCriteria(Criteria.where("location")
                        .near(new GeoJsonPoint(point)))
                .with(pageable);
    }

    private Query buildCountQuery(Point point, Criteria rsqlCriteria) {
        return Query.query(rsqlCriteria)
                .addCriteria(Criteria.where("location")
                        .near(point));
    }

    private Criteria buildRSQLCriteria(String rsqlSearch) {
        if (!StringUtils.hasText(rsqlSearch)) return new Criteria();
        QueryConversionPipeline pipeline = QueryConversionPipeline.defaultPipeline();
        Condition<GeneralQueryBuilder> condition = pipeline.apply(rsqlSearch, Store.class);
        return condition.query(new MongoVisitor());
    }
}
