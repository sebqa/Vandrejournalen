package com.example.sebastian.vandrejournalen.authentication;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.PLActivity;

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


public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, QRReader.OnFragmentInteractionListener{

    public static final int SAVE_CREDENTIALS_REQUEST_CODE = 1;
    private static final int LOGIN_WITH_CREDENTIALS_REQUEST_CODE = 2;

    public static final int AUTHENTICATION_DURATION_SECONDS = 30;

    public static final String KEY_NAME = "key";

    public static final String TRANSFORMATION = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/"
            + KeyProperties.ENCRYPTION_PADDING_PKCS7;
    public static final String CHARSET_NAME = "UTF-8";
    public static final String STORAGE_FILE_NAME = "credentials";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private KeyguardManager keyguardManager;
    String encryptedPassword;
    String decryptedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            LoginFragment fragment = LoginFragment.newInstance("","");

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public void login() {
        startActivity(new Intent(this, PLActivity.class));
        finish();

    }

    @Override
    public void startQR() {
        QRReader fragment = QRReader.newInstance("","");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }



    @Override
    public void notSuccessful() {
        Toast.makeText(this,"Unsuccessful",Toast.LENGTH_LONG);
    }


//Kode fra https://github.com/Zlate87/android-fingerprint-example/blob/master/app/src/main/java/com/example/zlatko/fingerprintexample/MainActivity.java

    private void showAuthenticationScreen(int requestCode) {
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
        if (intent != null) {
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public String encrypt(String passwordString) {

        try{
            SecretKey secretKey = createKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptionIv = cipher.getIV();
            byte[] passwordBytes = passwordString.getBytes(CHARSET_NAME);
            byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
            encryptedPassword = Base64.encodeToString(encryptedPasswordBytes,Base64.DEFAULT);


            SharedPreferences.Editor editor = getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE).edit();
            editor.putString("encryptionIv", Base64.encodeToString(encryptionIv, Base64.DEFAULT));
            editor.apply();


        } catch (UserNotAuthenticatedException e) {
            showAuthenticationScreen(LOGIN_WITH_CREDENTIALS_REQUEST_CODE);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                | BadPaddingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encryptedPassword;
    }

    @Override
    public String decrypt(String passwordString) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(STORAGE_FILE_NAME, Activity.MODE_PRIVATE);
            String base64EncryptionIv = sharedPreferences.getString("encryptionIv", null);
            byte[] encryptionIv = Base64.decode(base64EncryptionIv, Base64.DEFAULT);
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);

            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            byte[] passwordStringBytes = Base64.decode(passwordString, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptionIv));

            byte[] passwordBytes = cipher.doFinal(passwordStringBytes);
            decryptedPassword = new String(passwordBytes, CHARSET_NAME);
        } catch (UserNotAuthenticatedException e) {
            showAuthenticationScreen(LOGIN_WITH_CREDENTIALS_REQUEST_CODE);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException
                | BadPaddingException | InvalidAlgorithmParameterException
                | UnrecoverableKeyException | KeyStoreException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
        return decryptedPassword;
    }

    private SecretKey createKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)

                    .build());
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException("Failed to create a symmetric key", e);
        }
    }
    
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}