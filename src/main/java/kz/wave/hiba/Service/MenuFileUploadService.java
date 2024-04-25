package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MenuFileUploadService {

    Menu uploadImage(MultipartFile file, Menu menu);
    byte[] getImage(Long id) throws IOException;

}
