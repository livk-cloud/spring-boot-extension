package com.livk.util;

import lombok.experimental.UtilityClass;

import java.io.*;

/**
 * <p>
 * SerializerUtils
 * </p>
 *
 * @author livk
 * @date 2022/8/1
 */
@UtilityClass
public class SerializerUtils {
    public static void serializer(Serializable bean, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(bean);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deSerializer(String fileName, Class<T> targetClass) {
        File file = new File(fileName);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Object object = objectInputStream.readObject();
            if (targetClass.isInstance(object)) {
                return (T) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static <T> T deSerializerAndCopy(String fileName, Class<T> targetClass) {
        File file = new File(fileName);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Object object = objectInputStream.readObject();
            return BeanUtils.copy(object, targetClass);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
