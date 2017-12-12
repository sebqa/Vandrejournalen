package com.example.sebastian.vandrejournalen.authentication;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Sebastian on 08-11-2017.
 */

public class SecureUtil {

    Context context;
    private static final int AUTHENTICATION_DURATION_SECONDS = 30;
    private static final String KEY_NAME = "key";
    private static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String CHARSET_NAME = "UTF-8";
    private static final String STORAGE_FILE_NAME = "credentials";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private KeyguardManager keyguardManager;
    private String encryptedPassword;
    private String decryptedPassword;
    private String password;


    public SecureUtil(Context context){
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        }


    }

    public void checkLockScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(context,
                        "Secure lock screen hasn't set up. Go to 'Settings -> Security -> Screenlock' to set up a lock screen",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void showAuthenticationScreen(int requestCode) {
        //Open user authentication screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
            if (intent != null) {

                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }


    public String encrypt(String passwordString) {
        this.password = passwordString;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                try {
                    //Create key
                    SecretKey secretKey = createKey();
                    //Setup cipher with algo settings
                    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                    //Generate IV
                    byte[] encryptionIv = cipher.getIV();
                    //Convert plaintext to byte array
                    byte[] passwordBytes = passwordString.getBytes(CHARSET_NAME);
                    //Encrypt
                    byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
                    //Convert to string
                    encryptedPassword = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT);

                    //Save IV in shared preferences
                    SharedPreferences.Editor editor = context.getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE).edit();
                    editor.putString("encryptionIv", Base64.encodeToString(encryptionIv, Base64.DEFAULT));
                    editor.apply();


                } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                        | BadPaddingException | UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

        return encryptedPassword;
    }


    public String decrypt(String passwordString) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           /* if (!keyguardManager.isKeyguardSecure()) {
                // Show a message that the user hasn't set up a lock screen.
                Toast.makeText(context, "Secure lock screen isn't set up.\n" +
                        "Go to 'Settings -> Security -> Screen lock' to set up a lock screen", Toast.LENGTH_SHORT).show();*//*
            } else {*/
                try {
                    //Init sharedpreferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE);
                    //Retrieve IV from SP
                    String base64EncryptionIv = sharedPreferences.getString("encryptionIv", null);
                    //Convert into byte array
                    byte[] encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT);
                    //init keystore
                    KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
                    keyStore.load(null);
                    //Retrieve key of name KEY_NAME
                    SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
                    //Convert ciphered text into byte array
                    byte[] passwordStringBytes = Base64.decode(passwordString, Base64.DEFAULT);
                    //Setup cipher with settings
                    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptionIv));
                    //Decrypt
                    byte[] passwordBytes = cipher.doFinal(passwordStringBytes);
                    //Convert back to string
                    decryptedPassword = new String(passwordBytes, CHARSET_NAME);
                } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                        | BadPaddingException | InvalidAlgorithmParameterException
                        | UnrecoverableKeyException | KeyStoreException | CertificateException | IOException e) {
                    throw new RuntimeException(e);
                }
            }


        return decryptedPassword;
    }

    private SecretKey createKey() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                //Setup keygenerator
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                return keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
                throw new RuntimeException("Failed to create a symmetric key", e);
            }
        }
        return null;
    }

}
