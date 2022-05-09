package com.tallymarks.engroffm.utils;

import android.content.Context;
import android.os.Build;


import androidx.annotation.RequiresApi;

import com.tallymarks.engroffm.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class HttpHandler {
    private Context mContext;
    FingerprintDialog dialog;

    public HttpHandler(Context c) {
        this.mContext = c;
        //HTTPSTrustManager.allowAllSSL();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String httpPost(String requestUrl, HashMap<String, String> headerParams, HashMap<String, String> bodyParams, String jsonData) throws Exception {
        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(trustCert().getSocketFactory());
            }

            conn.setRequestMethod(Constants.POST);

            for (HashMap.Entry<String, String> entry : headerParams.entrySet())
            {
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }
            if(jsonData==null) {
                if(bodyParams==null)
                {
                    conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
                }
                else {
                    conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_FORM_URL_ENCODED);
                    byte[] postData = Helpers.urlParamBuilders(bodyParams).getBytes(StandardCharsets.UTF_8);
                    DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                    dataOutputStream.write(postData);
                }
            }
            else
            {
                conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
                conn.setDoInput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(jsonData);
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(inputStream);
            }
            else if(conn.getResponseCode()== HttpURLConnection.HTTP_CREATED)
            {
                InputStream inputStream=new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(inputStream);
            }
            else
            {
                InputStream inputStream = new BufferedInputStream(conn.getErrorStream());
                return convertStreamToString(inputStream);
            }
        }
        catch (Exception e) {
            throw new Exception("Exception occured : " + e.getMessage());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String httpPut(String requestUrl, HashMap<String, String> headerParams, HashMap<String, String> bodyParams, String jsonData) throws Exception {
        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(trustCert().getSocketFactory());
            }

            conn.setRequestMethod(Constants.PUT);

            for (HashMap.Entry<String, String> entry : headerParams.entrySet())
            {
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }
            if(jsonData==null) {
                if(bodyParams==null)
                {
                    conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
                }
                else {
                    conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_FORM_URL_ENCODED);
                    byte[] postData = Helpers.urlParamBuilders(bodyParams).getBytes(StandardCharsets.UTF_8);
                    DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                    dataOutputStream.write(postData);
                }
            }
            else
            {
                conn.setRequestProperty(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
                conn.setDoInput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
                dataOutputStream.writeBytes(jsonData);
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream=new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(inputStream);
            }
            else if(conn.getResponseCode()== HttpURLConnection.HTTP_CREATED)
            {
                InputStream inputStream=new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(inputStream);
            }
            else
            {
                InputStream inputStream = new BufferedInputStream(conn.getErrorStream());
                return convertStreamToString(inputStream);
            }
        }
        catch (Exception e) {
            throw new Exception("Exception occured : " + e.getMessage());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String httpGet(String requestUrl, HashMap<String, String> headerParams) throws Exception {
        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(trustCert().getSocketFactory());
            }

            for (HashMap.Entry<String, String> entry : headerParams.entrySet())
            {
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }
            conn.setRequestMethod(Constants.GET);
            if(conn.getResponseCode()== HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(inputStream);
            }
            else
            {
                InputStream inputStream = new BufferedInputStream(conn.getErrorStream());
                return convertStreamToString(inputStream);
            }
        }
        catch (Exception e) {
            throw new Exception("Exception occured : " + e.getMessage());
        }
    }





    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    private SSLContext trustCert() throws CertificateException,IOException, KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException {
        //AssetManager assetManager = mContext.getAssets();
       CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        ca = cf.generateCertificate(mContext.getResources().openRawResource(R.raw.tallymarkscloud));
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }
}
