package com.encryption.demo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Encryption {

    public static void main(String[] args) {
        //加密
        String value = "阿瓦达啃大瓜";
        String key = "蔡徐坤打篮球";
        System.out.println("原文:" + value);
        value = new String(encryption(value.getBytes(StandardCharsets.UTF_8), key), StandardCharsets.UTF_8);
        System.out.println("密文:");
        System.out.println(value);

        //解密
        value = new String(decryption(value.getBytes(StandardCharsets.UTF_8), key));
        System.out.println("解密:" + value);
    }

    public static byte[] encryption(byte[] value, String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = new byte[value.length + 8];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) (value.length >> (i * 8));
        }
        for (int i = 4; i < 8; i++) {
            bytes[i] = (byte) ((keyBytes.length) >> (i * 8));
        }
        int temp = value.length / keyBytes.length + ((value.length % keyBytes.length == 0) ? 0 : 1);
        for (int i = 0; i < temp; i++) {
            int start = i * keyBytes.length;
            for (int j = 0; j < keyBytes.length; j++) {
                if (start + j >= value.length) {
                    return bytes;
                }
                bytes[start + j + 8] = (byte) (value[start + j] ^ keyBytes[j]);
            }
        }
        return bytes;
    }

    public static byte[] decryption(byte[] value, String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = new byte[value.length - 8];
        int valueSize = getValueKey(value).get("valueSize");
        int keySize = getValueKey(value).get("keySize");
        //校验密码是否符合逻辑
        if (valueSize != value.length - 8 || keySize != keyBytes.length) {
            return null;
        }
        int temp = valueSize / keySize + ((valueSize % keySize == 0) ? 0 : 1);
        for (int i = 0; i < temp; i++) {
            int start = keySize * i;
            for (int j = 0; j < keySize; j++) {
                if (start + j >= valueSize) {
                    return bytes;
                }
                bytes[start + j] = (byte) (value[start + j + 8] ^ keyBytes[j]);
            }
        }
        return bytes;
    }

    static Map<String, Integer> getValueKey(byte[] value) {
        Map<String, Integer> valueKey = new HashMap<>();
        int valueSize = 0;
        int keySize = 0;
        for (int i = 0; i < 4; i++) {
            valueSize <<= 8;
            valueSize |=value[3 - i] & 0xff;
        }
        for (int i = 4; i < 8; i++) {
            keySize <<= 8;
            keySize |=value[11 - i] & 0xff;
        }
        valueKey.put("valueSize", valueSize);
        valueKey.put("keySize", keySize);
        return valueKey;
    }
}
