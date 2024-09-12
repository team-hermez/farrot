package com.hermez.farrot.image.repository;

import com.hermez.farrot.image.entity.Image;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

  private final EntityManager em;
  public void save(Image image) {
    em.persist(image);
  }
}