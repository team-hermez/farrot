package com.hermez.farrot.image.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ImageRequest<T>(T entity, MultipartFile file) {

  public Class<?> getEntityType() {return entity != null ? entity.getClass() : null;}
}