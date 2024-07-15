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
import java.util.Base64;

@Service
public class UserFileUploadServiceImpl implements UserFileUploadService {
    @Autowired
    private UserRepository userRepository;

    @Value("${uploadImageURL}")
    private String imageURL;

    @Value("${loadImageURL}")
    private String myLoadURL;

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

    private String encodeImageToBase64(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Write the image to the ByteArrayOutputStream in PNG format (you can choose a different format)
            ImageIO.write(image, "png", baos);

            // Encode the image bytes to base64
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
    private byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        }
    }
}
