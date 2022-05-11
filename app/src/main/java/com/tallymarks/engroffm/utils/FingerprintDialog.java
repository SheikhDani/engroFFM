package com.tallymarks.engroffm.utils;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tallymarks.engroffm.R;
import com.tallymarks.engroffm.activities.LoginActivity;
import com.tallymarks.engroffm.activities.MainActivity;
import com.tallymarks.engroffm.database.DatabaseHandler;
import com.tallymarks.engroffm.database.ExtraHelper;
import com.tallymarks.engroffm.database.MyHelper;
import com.tallymarks.engroffm.database.SharedPrefferenceHelper;
import com.tallymarks.engroffm.models.loginoutput.LoginOutput;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintDialog extends DialogFragment
        implements FingerprintHelper.Callback {

    Button mCancelButton;
    public static final String DEFAULT_KEY_NAME = "default_key";
    FingerprintManager mFingerprintManager;
   MyHelper myHelper;
   ExtraHelper extraHelper;

    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintHelper mFingerprintHelper;

    KeyStore mKeyStore = null;
    KeyGenerator mKeyGenerator = null;
    KeyguardManager mKeyguardManager;
    SharedPrefferenceHelper sHelper;
    DatabaseHandler db;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        try {
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        Cipher defaultCipher;
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }

        mKeyguardManager = getContext().getSystemService(KeyguardManager.class);
        mFingerprintManager = getContext().getSystemService(FingerprintManager.class);

        mFingerprintHelper = new FingerprintHelper(mFingerprintManager, getContext(), this);

        if (!mKeyguardManager.isKeyguardSecure()) {
            Toast.makeText(getContext(),
                    "Lock screen not set up.\n"
                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                    Toast.LENGTH_LONG).show();
            return;
        }

        createKey(DEFAULT_KEY_NAME);

        if (initCipher(defaultCipher, DEFAULT_KEY_NAME)) {
            mCryptoObject = new FingerprintManager.CryptoObject(defaultCipher);
        }
    }

    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);


            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            Toast.makeText(mContext, "Keys are invalidated after created. Retry the purchase\n"
                            + e.getMessage(),
                    Toast.LENGTH_LONG).show();

            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            Toast.makeText(mContext, "Failed to init cipher", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fingerprint, container, false);
        mCancelButton = v.findViewById(R.id.btnCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCryptoObject != null) {
            mFingerprintHelper.startAuthentication(mFingerprintManager, mCryptoObject);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintHelper.stopListening();
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public void createKey(String keyName) {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAuthenticated(boolean b) {
        if (b) {
            extraHelper = new ExtraHelper(getActivity());
            myHelper = new MyHelper(getActivity());
            sHelper = new SharedPrefferenceHelper(getActivity());
            db = new DatabaseHandler(getActivity());
            if(myHelper!=null)
            {
                if(myHelper.getString(Constants.REFERSH_TOKEN)!=null && !myHelper.getString(Constants.REFERSH_TOKEN).equals("")) {
                    dismiss();
                    new FingerLoginData().execute();
                   // Toast.makeText(mContext.getApplicationContext(), "Fingerprint Recongnized Successfully!", Toast.LENGTH_LONG).show();

                   // Log.e("keyaccess", String.valueOf(extraHelper.getString(Constants.ACCESS_TOKEN)));

                }
                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(R.string.alert)
                            .setMessage("Please Login First with username and password")
                            .setCancelable(true)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dismiss();
                                    //new PostSyncOutlet().execute();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    //Toast.makeText(getActivity(), "Please Login First with username and password", Toast.LENGTH_SHORT).show();
                }
            }


        } else
            Toast.makeText(mContext.getApplicationContext(), "Fingerprint not recognized", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String s) {
        Toast.makeText(mContext.getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHelp(String s) {
        Toast.makeText(mContext.getApplicationContext(), "Auth help message:" + s, Toast.LENGTH_LONG).show();
    }
    private class FingerLoginData extends AsyncTask<String, Void, String> {

        private HttpHandler httpHandler;
        ProgressDialog pDialog;
        String message = "";
        String status = "";
        String error = "";
        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.loading));
            pDialog.setIndeterminate(false);
            pDialog.show();
            pDialog.setCancelable(false);
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... Url) {
            String response = "";
            String basicAuth= "";
            String auth = Constants.LOGIN_USERNAME + ":" + Constants.LOGIN_PASSWORD;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                basicAuth = "Basic " + new String(Base64.getEncoder().encode(auth.getBytes()));
            }
            String loginUrl = Constants.FFM_FINGER_LOGIN+ "?refresh_token=" + myHelper.getString(Constants.REFERSH_TOKEN)+ "&grant_type=" + Constants.REFERSH_GRANT_TYPE;
            try {
                httpHandler = new HttpHandler(mContext);
                HashMap<String, String> headerParams = new HashMap<>();
                headerParams.put(Constants.AUTHORIZATION,Constants.BASIC);
                HashMap<String, String> bodyParams = new HashMap<>();
                response = httpHandler.httpPost(loginUrl,headerParams,bodyParams,null);
                Log.e("lOGIN Url", loginUrl);
                Log.e("Response", response);
                LoginOutput logincode = new Gson().fromJson(response, LoginOutput.class);
                if (logincode != null) {
                    for(int i=0;i<logincode.getAuthorities().size();i++)
                    {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(db.KEY_ROLE_NAME, "" +  logincode.getAuthorities().get(i) == null ||  logincode.getAuthorities().get(i).equals("") ? getString(R.string.not_applicable):  logincode.getAuthorities().get(i));
                        db.addData(db.ROLES, map);
                        extraHelper.setString(Constants.ROLE,logincode.getAuthorities().get(i));
//                        logincode.getAuthorities().get(i);

                    }
                    sHelper.setString(Constants.ACCESS_TOKEN,logincode.getAccessToken());
                    sHelper.setString(Constants.REFERSH_TOKEN,logincode.getRefreshToken());
                    sHelper.setString(Constants.TOKEN_TYPE,logincode.getTokenType());
                    extraHelper.setString(Constants.ACCESS_TOKEN,logincode.getAccessToken());
                    extraHelper.setString(Constants.REFERSH_TOKEN,logincode.getRefreshToken());
                    extraHelper.setString(Constants.TOKEN_TYPE,logincode.getTokenType());
                    extraHelper.setString(Constants.USER_NAME,logincode.getUsername());
                    extraHelper.setString(Constants.NAME,logincode.getName());
                    myHelper.setString(Constants.REFERSH_TOKEN,logincode.getRefreshToken());

                    HashMap<String, String> map = new HashMap<>();
                    map.put(db.KEY_COMPANY_NAME, "" + logincode.getCompanyName() == null || logincode.getCompanyName().equals("") ? getString(R.string.not_applicable): logincode.getCompanyName());
                    map.put(db.KEY_USER_NAME, "" + logincode.getUsername() == null || logincode.getUsername().equals("") ? getString(R.string.not_applicable) : logincode.getUsername());
                    map.put(db.KEY_NAME, logincode.getName()== null || logincode.getName().equals("") ? getString(R.string.not_applicable) : logincode.getName());
                    map.put(db.KEY_USER_DESIGNATION, logincode.getDesignation() == null || logincode.getDesignation().equals("") ? getString(R.string.not_applicable) : logincode.getDesignation());
                    map.put(db.KEY_USER_EMAIL ,logincode.getEmail() == null || logincode.getEmail().equals("") ? getString(R.string.not_applicable) : logincode.getEmail());
                    map.put(db.KEY_IS_LOGGED_IN, "1");
                    db.addData(db.LOGIN, map);
                    Helpers.displayMessage(mContext, true, "Fingerprint Recongnized Successfully!");
                    sHelper.setString(Constants.CUSTOMER_ALL_PLAN_NOT_FOUND,"2");
                    sHelper.setString(Constants.FARMER_TODAY_PLAN_NOT_FOUND,"2");
                    sHelper.setString(Constants.CUSTOMER_TODAY_PLAN_NOT_FOUND,"2");
                    Intent main = new Intent(mContext, MainActivity.class);
                   // main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(main);
                    ((LoginActivity)getActivity()).finish();



                }
                else {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        status = String.valueOf(jsonObj.getString("success"));
                        message = String.valueOf(jsonObj.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //Helpers.displayMessage(LoginActivity.this, true, getResources().getString(R.string.invalid_credentials_message));


                //  return response.toString();
            } catch (Exception exception) {
                if (response.equals("")) {
                    Helpers.displayMessage(getActivity(), true, exception.getMessage());
                    //showResponseDialog( mContext.getResources().getString(R.string.alert),exception.getMessage());
                    //pDialog.dismiss();
                } else {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                        errorMessage = json.getString("error_description");
                        error = json.getString("error");


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    //Helpers.displayMessage(LoginActivity.this, true, exception.getMessage());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();

            if(error!=null && !error.equals(""))
            {

                DialougeManager.invalidCredentialsPopup(getActivity(),"",errorMessage);

            }


//            parseErrorResponse(result);
        }


    }
}
