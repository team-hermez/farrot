package com.hermez.farrot.image.service;

import com.hermez.farrot.image.dto.request.ImageRequest;
import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.image.exception.ImageProcessionException;
import com.hermez.farrot.image.repository.ImageRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

  private final ImageRepository imageRepository;
  private final FirebaseService firebaseService;

  public <T> void save(ImageRequest<T> request) {
    saveImage result = getSaveImage(request);
    saveImageEntity(request, result);
  }

  public <T> void multiSave(List<ImageRequest<T>> requests) {
    requests.forEach(r -> saveImageEntity(r, getSaveImage(r)));
  }

  private <T> void saveImageEntity(ImageRequest<T> request, saveImage result) {
    Image image = Image.builder()
        .entityId(getEntityId(request.entity()))
        .entityType(request.getEntityType().getSimpleName())
        .originalName(result.fileName())
        .saveName(result.saveFileName())
        .path(result.filePath())
        .build();
    imageRepository.save(image);
  }

  private <T> saveImage getSaveImage(ImageRequest<T> request) {
    MultipartFile file = request.file();
    String fileName = file.getOriginalFilename();
    String saveFileName = System.currentTimeMillis() + "_" + fileName;
    String filePath = firebaseService.getPublicImageUrl(saveFileName);
    try {
      firebaseService.uploadImage(file,saveFileName);
    } catch (IOException e) {
      log.error("이미지 저장 중 오류 발생", e);
      throw new ImageProcessionException("이미지 저장 중 오류 발생", e);
    }
    return new saveImage(fileName, saveFileName, filePath);
  }

  private record saveImage(String fileName, String saveFileName, String filePath) {}

  private <T> Integer getEntityId(T entity) {
    try {
      return (Integer) entity.getClass().getMethod("getId").invoke(entity);
    } catch (Exception e) {
      log.error("엔티티 ID를 가져오는 중 오류 발생", e);
      throw new RuntimeException("엔티티 ID를 가져오는 중 오류 발생", e);
    }
  }
}
