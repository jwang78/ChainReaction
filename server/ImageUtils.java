package server;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import java.util.Base64.Decoder;

/**
 * Image loading utilities to allow encoding of images in text format.
 */
public class ImageUtils {
    /**
     * Encodes an image in a base-64 string from a file with a maximum size. If the maximum size
     * specified is zero or less, then the encoding will perform no compression.
     * @param f The file to read the image from.
     * @param maxSize The maximum size of the encoding
     * @return The base-64 string representation of the image.
     */
    public static String encodeImage(File f, int maxSize) throws IOException {
        Encoder enc = Base64.getEncoder();
        byte[] data = Files.readAllBytes(f.toPath());
        String s = enc.encodeToString(data);
        if (maxSize <= 0) {
            return s;
        }
        BufferedImage im = ImageIO.read(f);
        ImageWriter wr = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam p = wr.getDefaultWriteParam();
        p.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        p.setCompressionQuality((float) (maxSize * 1.0 / s.length()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wr.setOutput(ImageIO.createImageOutputStream(baos));
        wr.write(null, new javax.imageio.IIOImage(im, null, null), p);
        return enc.encodeToString(baos.toByteArray());
    }
    
    /**
     * Encodes an image into a base-64 string.
     * @param img The image to encodes
     * @return The base-64 string representation of the image.
     * */
    public static String encodeImage(Image img) {
        Encoder enc = Base64.getEncoder();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] data = new byte[0];
        try {
            ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(img, null), "jpg", b);
            data = b.toByteArray();
            b.close();
        } catch (IOException e) {
            // This never happens (hopefully), since we're using a
            // ByteArrayOutputStream
            e.printStackTrace();
        }
        return enc.encodeToString(data);
    }
    
    /**
     * Decodes an image from a string.
     * @param imageString The base-64 representation of the image.
     * @return The image decoding of the string
     * */
    public static Image decodeImage(String imageString) {
        byte[] data = Base64.getDecoder().decode(imageString);
        ByteArrayInputStream b = new ByteArrayInputStream(data);
        try {
            return javafx.embed.swing.SwingFXUtils.toFXImage(ImageIO.read(b), null);
        } catch (IOException e) {
            // Never happens because we're reading from a ByteArrayInputStream
            e.printStackTrace();
        }
        return null;
    }
}
