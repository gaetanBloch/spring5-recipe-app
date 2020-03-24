package guru.springframework.spring5recipeapp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gaetan Bloch
 * Created on 24/03/2020
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        log.debug("Saving an image for recipe with id = " + recipeId);
    }
}
