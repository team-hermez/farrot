package com.hermez.farrot.image.repository;

import com.hermez.farrot.image.entity.Image;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    TypedQuery<Image> query = em.createQuery(
            "SELECT i FROM Image i WHERE i.entityId = :entityId AND i.entityType = :entityType", Image.class);
    query.setParameter("entityId", entityId);
    query.setParameter("entityType", entityType);
    return query.getResultList();
  }

  public void delete(Image image) {
    if (em.contains(image)) {
      em.remove(image);
    } else {
      em.remove(em.merge(image));
    }
  }
}