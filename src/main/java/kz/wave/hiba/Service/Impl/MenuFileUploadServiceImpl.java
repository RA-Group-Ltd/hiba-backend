package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.Menu;
import kz.wave.hiba.Repository.MenuRepository;
import kz.wave.hiba.Service.MenuFileUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Implementation of the {@link MenuFileUploadService} interface.
 */
@Service
@RequiredArgsConstructor
public class MenuFileUploadServiceImpl implements MenuFileUploadService {

    private final MenuRepository menuRepository;

    @Value("${uploadMenuImageURL}")
    private String menuImageURL;

    @Value("${loadMenuImageURL}")
    private String myMenuLoadURL;

    /**
     * Uploads an image for a menu.
     *
     * @param file the image file to upload
     * @param menu the menu entity to associate with the image
     * @return the updated menu entity with the uploaded image, or null if an error occurs
     */
    @Override
    public Menu uploadImage(MultipartFile file, Menu menu) {
        try {
            BufferedImage resizedImage = resizeImage(file, 150, 150);
            byte[] imageBytes = convertImageToByteArray(resizedImage);
            menu.setImage(imageBytes);
            return menuRepository.save(menu);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the image for a menu by its ID.
     *
     * @param id the ID of the menu
     * @return the image bytes, or the default "no image" if the image is not found
     * @throws IOException if an I/O error occurs
     */
    @Override
    public byte[] getImage(Long id) throws IOException {
        try {
            Menu menu = menuRepository.getById(id);

            byte[] image = menu.getImage();

            if (image != null && image.length > 0) {
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream in;
        String picURL = myMenuLoadURL + "noimage.png";
        ClassPathResource resource = new ClassPathResource(picURL);
        in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    /**
     * Resizes an image to the specified target width and height.
     *
     * @param file the image file to resize
     * @param targetWidth the target width
     * @param targetHeight the target height
     * @return the resized image
     * @throws IOException if an I/O error occurs
     */
    private BufferedImage resizeImage(MultipartFile file, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // Calculate the center of the image
        int centerX = originalImage.getWidth() / 2;
        int centerY = originalImage.getHeight() / 2;

        // Calculate the cropping coordinates
        int cropSize = Math.min(originalImage.getWidth(), originalImage.getHeight());
        int cropX = centerX - cropSize / 2;
        int cropY = centerY - cropSize / 2;

        // Crop the image to a square around its center
        BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, cropSize, cropSize);

        // Resize the image to the target dimensions
        Image scaledImage = croppedImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * Encodes an image to a Base64 string.
     *
     * @param image the image to encode
     * @return the Base64-encoded string
     * @throws IOException if an I/O error occurs
     */
    private String encodeImageToBase64(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Write the image to the ByteArrayOutputStream in PNG format (you can choose a different format)
            ImageIO.write(image, "png", baos);

            // Encode the image bytes to base64
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    /**
     * Converts an image to a byte array.
     *
     * @param image the image to convert
     * @return the byte array representation of the image
     * @throws IOException if an I/O error occurs
     */
    private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }
}
