package com.hermez.farrot.image.repository;

import static com.hermez.farrot.image.entity.QImage.image;

import com.hermez.farrot.image.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public ImageRepositoryCustom(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<Image> findByEntityIdAndEntityType(Integer entityId, String entityType) {
    return queryFactory
        .selectFrom(image)
        .where(image.entityId.eq(entityId), image.entityType.eq(entityType))
        .fetch();
  }
}
