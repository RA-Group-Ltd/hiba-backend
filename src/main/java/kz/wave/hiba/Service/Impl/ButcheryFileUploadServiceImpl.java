package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.ButcheryDocument;
import kz.wave.hiba.Repository.ButcheryRepository;
import kz.wave.hiba.Service.ButcheryFileUploadService;
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

/**
 * Implementation of the {@link ButcheryFileUploadService} interface.
 */
@Service
public class ButcheryFileUploadServiceImpl implements ButcheryFileUploadService {

    @Autowired
    private ButcheryRepository butcheryRepository;

    @Value("${uploadButcheryDocumentURL}")
    private String documentURL;

    @Value("${loadButcheryDocumentURL}")
    private String myLoadDocumentURL;

    /**
     * Uploads an image to the butchery.
     *
     * @param file the image file to be uploaded
     * @param butchery the butchery entity to which the image belongs
     * @return the updated butchery entity
     */
    @Override
    public Butchery uploadImage(MultipartFile file, Butchery butchery) {
        try {
            if (file == null) {
                return butchery;
            }
            InputStream inputStream = file.getInputStream();
            BufferedImage img = ImageIO.read(inputStream);
            byte[] imageBytes = convertImageToByteArray(img);

            butchery.setImage(imageBytes);

            return butcheryRepository.save(butchery);

        } catch (Exception e) {
            return butchery;
        }
    }

    /**
     * Uploads documents to the butchery.
     *
     * @param files the list of document files to be uploaded
     * @param butchery the butchery entity to which the documents belong
     */
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

    /**
     * Retrieves the documents of a butchery by its ID.
     *
     * @param id the ID of the butchery
     * @return the byte array of the documents
     * @throws IOException if an I/O error occurs
     */
    @Override
    public byte[] getDocuments(Long id) throws IOException {
        try {
            Butchery butchery = butcheryRepository.getById(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream in;
        String picURL = myLoadDocumentURL + "noimage.png";
        ClassPathResource resource = new ClassPathResource(picURL);
        in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    /**
     * Converts a BufferedImage to a byte array.
     *
     * @param image the BufferedImage to be converted
     * @return the byte array of the image
     * @throws IOException if an I/O error occurs
     */
    private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }
}
