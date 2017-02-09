package com.d2d.biztil.Webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WebServerCall {

    public static final int ANDROID_CONNECTION_TIMEOUT = 100000;
    static InputStream is = null;
    static JSONObject json = null;
    static String output = "";

    public static String putDataToServer(List<NameValuePair> nameValuePairs,
                                         String url) throws Throwable {

        HttpPost request = new HttpPost(url);
        StringBuilder sb = new StringBuilder();
        // Or like this...
        for (int i = 0; i < nameValuePairs.size(); i++) {
            Log.e(url, nameValuePairs.get(i).toString());
        }
        request.setHeader("Content-Type",
                "application/x-www-form-urlencoded;charset=UTF-8");

        request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

        HttpResponse response = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpConnectionParams.setSoTimeout(httpClient.getParams(),
                ANDROID_CONNECTION_TIMEOUT * 1000);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
                ANDROID_CONNECTION_TIMEOUT * 1000);
        try {

            response = httpClient.execute(request);
        } catch (SocketException se) {
            Log.e("SocketException", se + "");
            throw se;
        }

        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);

        }

        return sb.toString();
    }

    public static String getDataFromServer(String url) throws Throwable {

        String line, response = "";
        url = url.replace(" ","%20");
        Log.e(url, url);
        URL               _url;
        HttpURLConnection urlConnection;

        try {
            _url = new URL(url);
            urlConnection = (HttpURLConnection) _url.openConnection();

            urlConnection.setUseCaches(false);
        }
        catch (MalformedURLException e) {
            Log.e("JSON Parser", "Error due to a malformed URL " + e.toString());
            return null;
        }
        catch (IOException e) {
            Log.e("JSON Parser", "IO error " + e.toString());
            return null;
        }

        try {
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder(is.available());

            while ((line = reader.readLine()) != null) {
                total.append(line).append('\n');
            }
            output = total.toString();
        }
        catch (IOException e) {
            Log.e("JSON Parser", "IO error " + e.toString());
            return null;
        }
        finally{
            urlConnection.disconnect();
        }

        Log.e(url, output);
        return output;

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.e("InputStream",
                    e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    @SuppressWarnings("deprecation")
    public static String multiPart(List<NameValuePair> nameValuePairs,
                                   String url) throws ClientProtocolException, IOException {

        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("Connection", "Keep-Alive");
        client.getParams().setParameter("Content-Type", "multipart/form-data;");
        client.getParams()
                .setParameter(
                        "http.socket.timeout",
                        Integer.valueOf(ANDROID_CONNECTION_TIMEOUT * 1000));
        client.getParams()
                .setParameter(
                        "http.connection.timeout",
                        Integer.valueOf(ANDROID_CONNECTION_TIMEOUT * 1000));

        MultipartEntity multipartEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);

        for (NameValuePair nameValuePair : nameValuePairs) {
            if (nameValuePair.getName().equalsIgnoreCase("photo")) {
                File imgFile = new File(nameValuePair.getValue());
                // FileBody fileBody = new FileBody(imgFile, "image/png",
                // "UTF-8");
                FileBody fileBody = new FileBody(imgFile, "image124.png",
                        "image/*", "UTF-8");
                multipartEntity.addPart("photo", fileBody);
            } else {
                multipartEntity.addPart(nameValuePair.getName(),
                        new StringBody(nameValuePair.getValue()));
            }
        }

        HttpPost post = new HttpPost(url);
        post.setEntity(multipartEntity);
        HttpResponse response = client.execute(post);

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        String responsestr = convertStreamToString(is);
        is.close();
        Log.e("Response is ::", responsestr);
        return responsestr;

    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
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

    public static String putDataToServer(JSONObject returnedJObject,
                                         String webService) throws Throwable {
        String URL = webService;
        HttpPost request = new HttpPost(URL);
        JSONStringer json = new JSONStringer();
        StringBuilder sb = new StringBuilder();

        if (returnedJObject != null) {
            Iterator<String> itKeys = returnedJObject.keys();
            if (itKeys.hasNext())
                json.object();
            while (itKeys.hasNext()) {
                String k = itKeys.next();
                json.key(k).value(returnedJObject.get(k));
                Log.e("keys " + k, "value "
                        + returnedJObject.get(k).toString());
            }
        }
        json.endObject();

        StringEntity entity = new StringEntity(json.toString());
        // entity.setContentType("application/json;charset=UTF-8");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8"));
        // request.setHeader("Accept", "application/json");
        // request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        HttpResponse response = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpConnectionParams.setSoTimeout(httpClient.getParams(),
                ANDROID_CONNECTION_TIMEOUT * 1000);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
                ANDROID_CONNECTION_TIMEOUT * 1000);
        try {

            response = httpClient.execute(request);
        } catch (SocketException se) {
            Log.e("SocketException", se + "");
            throw se;
        }

        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);

        }

        return sb.toString();
    }

}
