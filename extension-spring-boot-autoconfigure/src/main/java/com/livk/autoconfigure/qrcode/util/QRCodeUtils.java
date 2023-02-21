package com.livk.autoconfigure.qrcode.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.autoconfigure.qrcode.enums.PicType;
import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * QRCodeUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class QRCodeUtils {

    /**
     * Get qr code image byte [ ].
     *
     * @param text   the text
     * @param width  the width
     * @param height the height
     * @param type   the type
     * @return the byte [ ]
     */
    public byte[] getQRCodeImage(String text, int width, int height, PicType type) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getQRCodeImage(text, width, height, stream, type);
        return stream.toByteArray();
    }

    /**
     * Gets qr code image.
     *
     * @param text   the text
     * @param width  the width
     * @param height the height
     * @param out    the out
     * @param type   the type
     */
    public void getQRCodeImage(String text, int width, int height, OutputStream out, PicType type) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(matrix, type.name(), out);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse qr code string.
     *
     * @param inputStream the input stream
     * @return the string
     */
    public static String parseQRCode(InputStream inputStream) {
        String content = null;
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            content = result.getText();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }
}
