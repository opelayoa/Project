package com.tiendas3b.almacen.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by dfa on 29/02/2016.
 */
public class Encryption {

    private static String BASE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    private Cipher eCipher;
    private Cipher dCipher;

    public Encryption() {

        String passPhrase = "_k4rl43l1z4b3th_";

        byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3, (byte) 0x03};

        int iterationCount = 19;

        try {

            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            //			SecretKey key;
            //			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //				// Use compatibility key factory -- only uses lower 8-bits of passphrase chars
            //				key = SecretKeyFactory.getInstance("PBEWithMD5AndDESAnd8bit").generateSecret(keySpec);
            //			} else {
            //				// Traditional key factory. Will use lower 8-bits of passphrase chars on
            //				// older Android versions (API level 18 and lower) and all available bits
            //				// on KitKat and newer (API level 19 and higher).
            //				key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            //			}

            eCipher = Cipher.getInstance(key.getAlgorithm());
            dCipher = Cipher.getInstance(key.getAlgorithm());

            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
        } catch (InvalidKeySpecException e) {
            System.out.println("EXCEPTION: InvalidKeySpecException");
        } catch (NoSuchPaddingException e) {
            System.out.println("EXCEPTION: NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("EXCEPTION: NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            System.out.println("EXCEPTION: InvalidKeyException");
        }
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF-8");
            byte[] enc = eCipher.doFinal(utf8);
            return Base64.encodeToString(enc, Base64.DEFAULT);
//			return new String(Base64.encodeBase64(enc), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String str) {
        if (str == null || str.isEmpty()){
            return null;
        }else{
            try {
    // Receiving side
    //            byte[] data = Base64.decode(base64, Base64.DEFAULT);
    //            String text = new String(data, "UTF-8");

                byte[] dec = Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT);
                byte[] utf8 = dCipher.doFinal(dec);
                return new String(utf8, "UTF-8");
            } catch (BadPaddingException e) {
                System.out.println("1");
            } catch (IllegalBlockSizeException e) {
                System.out.println("2" + str +"|");
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                System.out.println("3");
            } catch (Exception e) {
                System.out.println("4");
            }
        }
        return null;
    }

    public String generateRandomString() {

        String randomString = "";

        try {
            for (int i = 0; i < 10; i++) {
                int position = (int) (Math.random() * BASE.length());
                String ch = BASE.substring(position, position + 1);
                randomString += ch;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return randomString;
    }

    public String md5(String s) {
        try {
            //esto está mal!!! recordar
//            // Create MD5 Hash
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//            digest.update(s.getBytes());
//            byte messageDigest[] = digest.digest();
//
//            // Create Hex String
//            StringBuffer hexString = new StringBuffer();
//            for (int i=0; i<messageDigest.length; i++)
//                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
//            return hexString.toString();

            //kitkat+
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(StandardCharsets.UTF_8.encode(s));
//            return String.format("%032x", new BigInteger(1, md5.digest()));

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            String md5 = new BigInteger(1, m.digest()).toString(16);
            return md5.length() == 32 ? md5 : fillZeros(md5);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String fillZeros(String md5) {
        for(int i = md5.length(); i < 32; i++){
            md5 = "0".concat(md5);
        }
        return md5;
    }

}
