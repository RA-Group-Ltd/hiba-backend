package kz.wave.hiba.Service.Impl;

import kz.wave.hiba.Entities.User;
import kz.wave.hiba.Repository.UserRepository;
import kz.wave.hiba.Service.UserFileUploadService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Implementation of the {@link UserFileUploadService} interface.
 */
@Service
public class UserFileUploadServiceImpl implements UserFileUploadService {
    @Autowired
    private UserRepository userRepository;

    @Value("${uploadImageURL}")
    private String imageURL;

    @Value("${loadImageURL}")
    private String myLoadURL;

    /**
     * Uploads an image for a user and resizes it to 150x150 pixels.
     *
     * @param file the image file to be uploaded
     * @param user the user to whom the image belongs
     * @return the updated user with the uploaded image
     */
    @Override
    public User uploadImage(MultipartFile file, User user) {
        try {
            BufferedImage resizedImage = resizeImage(file, 150, 150);
            byte[] imageBytes = convertImageToByteArray(resizedImage);
            user.setAvatar(imageBytes);
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the avatar image of a user.
     *
     * @param id the ID of the user
     * @return the byte array of the user's avatar image
     * @throws IOException if an I/O error occurs
     */
    @Override
    public byte[] getImage(Long id) throws IOException {
        try {
            User user = userRepository.getById(id);
            byte[] image = user.getAvatar();

            if (image != null && image.length > 0) {
                System.out.println(image.toString());
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream in;
        String picURL = myLoadURL + "noimage.png";
        ClassPathResource resource = new ClassPathResource(picURL);
        in = resource.getInputStream();
        return IOUtils.toByteArray(in);
    }

    /**
     * Resizes an image to the specified dimensions.
     *
     * @param file the image file to be resized
     * @param targetWidth the target width
     * @param targetHeight the target height
     * @return the resized image as a {@link BufferedImage}
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
     * Converts an image to a byte array.
     *
     * @param image the image to be converted
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
