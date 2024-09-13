package com.hermez.farrot.image.repository;

import com.hermez.farrot.image.entity.Image;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

  private final EntityManager em;
  public void save(Image image) {
    em.persist(image);
  }

  public List<Image> findByEntityIdAndEntityType(Integer entityId, String entityType) {
    String query = "SELECT i FROM Image i WHERE i.entityId = :entityId AND i.entityType = :entityType";
    return em.createQuery(query, Image.class)
            .setParameter("entityId", entityId)
            .setParameter("entityType", entityType)
            .getResultList();
  }
}