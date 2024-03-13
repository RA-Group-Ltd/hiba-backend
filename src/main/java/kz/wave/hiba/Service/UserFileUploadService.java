package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserFileUploadService {

    User uploadImage(MultipartFile file, User user);
    byte[] getImage(Long id) throws IOException;

}
