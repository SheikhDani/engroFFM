package com.tallymarks.ffmapp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import com.tallymarks.ffmapp.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;


public class Helpers {

    public static String urlParamBuilders(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void noConnectivityPopUp(final Context context) {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom_alert_dialouge, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                //ImageView ivClose = promptsView.findViewById(R.id.iv_close);
                TextView tvTitle = promptsView.findViewById(R.id.tv_option);
                TextView tvBody = promptsView.findViewById(R.id.tv_body);
                Button btnNo = promptsView.findViewById(R.id.btn_no);
                Button btnYes = promptsView.findViewById(R.id.btn_yes);
                //ivClose.setVisibility(View.GONE);
                btnNo.setVisibility(View.GONE);
                tvTitle.setText(R.string.internet_available_title);
                tvBody.setText(R.string.internet_available_msg);
                btnYes.setText(context.getResources().getString(R.string.dismiss));
                tvBody.setTextColor(context.getResources().getColor(R.color.black));
                tvBody.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                alertDialogBuilder.setCancelable(true);
                alertDialog.show();
            }
        });
    }


    public static String clean(String data) {
        String decodedData = "";
        try {
            decodedData = java.net.URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedData;
    }


    public static String urlEncode(String data) {
        return Uri.encode(data);
    }


    public static String getDeviceTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getID();
    }
    public static String getDatefromMilis(String date)
    {
        long num = Long.parseLong(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(num);
        return simpleDateFormat.format(calendar.getTime());

    }

    public static String utcToAnyTimeZone(String timeZone, String time, String timeFormat) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat(timeFormat);
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = null;
        try {
            parsed = sourceFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TimeZone tz = TimeZone.getTimeZone(timeZone);
        SimpleDateFormat destFormat = new SimpleDateFormat(timeFormat);
        destFormat.setTimeZone(tz);
        return destFormat.format(parsed);
    }


    public static String utcToAnyDateFormat(String dateString, String inputFormat, String outputFormat) {
        if (!dateString.equals("") || !(dateString == null)) {
            SimpleDateFormat input = new SimpleDateFormat(inputFormat);
            SimpleDateFormat output = new SimpleDateFormat(outputFormat);
            Date date = null;
            try {
                date = input.parse(dateString);
                return output.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        {
            return "";
        }
    }


    public static void displayMessage(final Context context, boolean isBackgroundMessage, final String message) {
        if (isBackgroundMessage) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }




    public static byte[] bimapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        return baos.toByteArray();
    }

    public static Bitmap byteToBitmap(byte[] baos) {
        return BitmapFactory.decodeByteArray(baos, 0, baos.length);
    }

    public static Bitmap inputStreamToBitmap(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }

    public static String bitmapToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public static boolean isEmpty(Context context, EditText editText)
    {
        String input=editText.getText().toString();
        input=input.trim();
        if(input.equals(""))
        {
            editText.setText("");
            editText.setError(context.getResources().getString(R.string.required));
            return true;
        }
        else
        {
            return false;
        }
    }
    public static String getCurrectDate(String format)
    {
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }




    public static String dateMask(String inputData, String inputFormat, String outputFormat)
    {
        SimpleDateFormat input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        Date date = null;
        try
        {
            date = input.parse(inputData);
            System.out.println(output.format(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return output.format(date);
    }





    public static Double numberMask(Double number)
    {
        return Math.round(number*100.0)/100.0;
    }





    public static void highlightMandatoryFields(final Context context, final EditText editText)
    {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(editText.getText().toString().equals(""))
                {
                    editText.setError(context.getResources().getString(R.string.required));
                }
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);

        } catch (Exception e) {
        }
    }


    public static void highlightMandatoryFieldsOnLoad(final Context context, final EditText editText)
    {
        if(editText.getText().toString().equals(""))
        {
            editText.setError(context.getResources().getString(R.string.required));
        }
    }



    public static void unHighlightMandatoryField(EditText editText)
    {
        editText.setError(null);
    }

//    public static void customTextView(TextView view) {
//        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
//                "By Clicking Sign up. you agree to cabpoint's");
//        spanTxt.append("Terms of Use");
//        spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#E63939")), 44, spanTxt.length(), 0);
//        spanTxt.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                //openTermOrPolicyFiles("file:///android_asset/terms.html",1);  // 1 for terms ;
//            }
//        }, spanTxt.length() - "Terms of Use".length(), spanTxt.length(), 0);
//        spanTxt.append(" and acknowledge you have read the");
//        //spanTxt.setSpan(new ForegroundColorSpan(Color.parseColor("#E63939")), 32, spanTxt.length(), 0);
//        spanTxt.append(" Privacy Policy");
//        spanTxt.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                //openTermOrPolicyFiles("file:///android_asset/private_policy.html",2);   // 2 for policy ;
//            }
//        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
//        view.setMovementMethod(LinkMovementMethod.getInstance());
//        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
//    }


}
