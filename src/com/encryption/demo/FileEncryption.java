package com.encryption.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileEncryption extends Encryption {
    public static void main(String[] args) {
        try {
            encryption(new File("D:\\加密前.mp4"), new File("D:\\加密后.mp4"), "Hello world");
            decryption(new File("D:\\加密后.mp4"), new File("D:\\解密.mp4"), "Hello world");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void encryption(File file, File encryption, String key) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024 * 1024];
        FileOutputStream outputStream = new FileOutputStream(encryption);
        int size = 0;
        while ((size = inputStream.read(bytes)) != -1) {
            outputStream.write(encryption(bytes, key), 0, size + 8);
        }
    }

    public static boolean decryption(File file, File decryption, String key) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Map<String, Integer> valueKey = getValueKey(file);
        byte[] bytes = new byte[valueKey.get("valueSize") + 8];
        FileOutputStream outputStream = new FileOutputStream(decryption);
        int size = 0;
        while ((size = inputStream.read(bytes)) != -1) {
            byte[] temp = decryption(bytes, key);
            if (temp == null) {
                return false;
            }
            outputStream.write(temp, 0, size - 8);
        }
        return true;
    }

    private static Map<String, Integer> getValueKey(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] value = new byte[8];
        inputStream.read(value);
        return getValueKey(value);
    }
}
