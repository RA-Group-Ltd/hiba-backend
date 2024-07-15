package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Promotion;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PromotionUploadService {

    Promotion uploadImage(MultipartFile file, Promotion promotion);
    byte[] getImage(Long id) throws IOException;

}
