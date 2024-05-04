package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.ButcheryDocument;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Service.ButcheryFileUploadCertificate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ButcheryFileUploadCertificateServiceImpl implements ButcheryFileUploadCertificate {

    @Autowired
    private ButcheryRepository butcheryRepository;

    @Value("${uploadButcheryDocumentURL}")
    private String documentURL;

    @Value("${loadButcheryDocumentURL}")
    private String myLoadDocumentURL;

    @Override
    public void uploadDocuments(List<MultipartFile> files, Butchery butchery) {
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    byte[] imageBytes = convertImageToByteArray(image);
                    ButcheryDocument document = new ButcheryDocument();
                    document.setCertificateImage(imageBytes);
                    document.setButchery(butchery);
                    butchery.getDocuments().add(document);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public byte[] getDocuments(Long id) throws IOException {
        try {
            Butchery butchery = butcheryRepository.getById(id);

//            byte[] image = butchery.getDocuments();

            /*if (image != null && image.length > 0) {
                System.out.println(image.toString());
                return image;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream in;
        String picURL = myLoadDocumentURL + "noimage.png";
        ClassPathResource resource = new ClassPathResource(picURL);
        in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }
}
