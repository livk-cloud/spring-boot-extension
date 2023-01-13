package com.livk.autoconfigure.qrcode.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.autoconfigure.qrcode.enums.PicType;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
}
