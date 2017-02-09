package com.d2d.biztil.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Methods {


    public static String getDeviceID(TelephonyManager phonyManager) {

        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return "None: " + id;

            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM: IMEI=" + id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA: MEID/ESN=" + id;

            default:
                return "UNKNOWN: ID=" + id;
        }
    }

    public static String getSQLDate(String dateReceivedFromUser)
            throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        DateFormat dateFormatNeeded = new SimpleDateFormat("dd MMM, hh:mm aa");
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    public static String gethourDate(String dateReceivedFromUser)
            throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    public static String getSQLDateTime(String dateReceivedFromUser)
            throws ParseException {
        // 09/16/2013 format mm/dd/yyyy
        // String dateReceivedFromUser = "12/13/2012";
        DateFormat userDateFormat = new SimpleDateFormat(
                "E, MMM dd yyyy HH:mm: a");
        DateFormat dateFormatNeeded = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss a");
        Date date = userDateFormat.parse(dateReceivedFromUser);
        String convertedDate = dateFormatNeeded.format(date);
        // System.out.println(convertedDate);
        return convertedDate;
    }

    @SuppressLint("NewApi")
    public static boolean isEmpty(EditText editText) {

        String text = editText.getText().toString();
        text = text.trim();
        return text.isEmpty();
    }
    public static boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }



    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    public static String replaceAll(String str, String[] patterns,
                                    String[] replaceStrings) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < patterns.length; i++) {
            while ((e = str.indexOf(patterns[i], s)) >= 0) {
                result.append(str.substring(s, e));
                result.append(replaceStrings[i]);
                s = e + patterns[i].length();
            }
        }

        result.append(str.substring(s));
        return result.toString();
    }


    private static boolean downloadFile(String url, File outputFile) {
        boolean isDownloaded = false;
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(
                    outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
            isDownloaded = true;
        } catch (FileNotFoundException e) {
            isDownloaded = false;

        } catch (IOException e) {
            isDownloaded = false;
        }
        return isDownloaded;
    }

    public static Drawable downloadDeviceBackground(String imageurl) {
        String filepath = null;
        try {
            URL url = new URL(imageurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory()
                    .getAbsoluteFile();
            String filename = "downloadedFile.png";

            File file = new File(SDCardRoot, filename);
            if (file.createNewFile()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;

            }
            fileOutput.close();
            if (downloadedSize == totalSize)
                filepath = file.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            filepath = null;
            e.printStackTrace();
        }


        Drawable d = null;
        if (filepath == null || filepath.length() == 0) {
            d = null;
        } else {
            d = Drawable.createFromPath(filepath);
        }
        return d;
    }

    public static void animRedTextMethod(Activity context, final String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }


    public static String convertDate(String date, String dtformat,
                                     String needFormat) {
        try {

            SimpleDateFormat format = new SimpleDateFormat(dtformat);
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat(needFormat);
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getLastBitFromUrl(final String url) {
        // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    public static boolean DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        return fileOrDirectory.delete();
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                                                            View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            int desiredHeight = View.MeasureSpec.makeMeasureSpec(
                    listItem.getHeight(), View.MeasureSpec.UNSPECIFIED);
            listItem.measure(desiredWidth, desiredHeight);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                        + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
