package kz.wave.hiba.Service;

import kz.wave.hiba.Entities.Butchery;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ButcheryFileUploadService {

    Butchery uploadImage(MultipartFile file, Butchery butchery);

    void uploadDocuments(List<MultipartFile> files, Butchery butchery);
    byte[] getDocuments(Long id) throws IOException;

}
