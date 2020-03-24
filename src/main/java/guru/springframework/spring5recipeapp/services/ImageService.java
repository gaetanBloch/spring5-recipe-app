package guru.springframework.spring5recipeapp.services;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
public interface ImageService {
    void saveImageFile(Long recipeId, MultipartFile file);
}
