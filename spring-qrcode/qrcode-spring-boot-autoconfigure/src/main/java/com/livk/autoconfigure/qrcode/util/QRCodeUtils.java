package com.livk.autoconfigure.qrcode.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.livk.autoconfigure.qrcode.enums.PicType;
import com.livk.autoconfigure.qrcode.exception.QRCodeException;
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
 * @date 2022/11/4
 */
@UtilityClass
public class QRCodeUtils {

    public byte[] getQRCodeImage(String text, int width, int height, PicType type) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, type.name(), stream);
            return stream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new QRCodeException("QR code generation failed!");
        }
    }

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
