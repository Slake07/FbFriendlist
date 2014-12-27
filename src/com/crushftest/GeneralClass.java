package com.crushftest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class GeneralClass {

	public static String temp_iUserFaceBookBLOGIN = "temp_iUserFBLOGIN";
	private static String responseBody;
	private static JSONObject jObj;
	public static ArrayList<NameValuePair> perameters = new ArrayList<NameValuePair>();
	public static boolean isHaveInternet = true;
	// public static String DeviceId = "";

	protected static boolean isConnectionTimeOut = false;

	static int timeoutConnection = 3000;
	static int timeoutSocket = 10000;
	
	public static void showToast(String text, Context con) {
		Toast.makeText(con, "" + text, Toast.LENGTH_LONG).show();
	}
	
	public static JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		/**
		 * Time out code
		 * */
		// HttpParams httpParameters = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParameters,
		// timeoutConnection);
		// HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				responseBody = EntityUtils.toString(httpEntity);
				// is = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				// DefaultHttpClient httpClient = new DefaultHttpClient(
				// httpParameters);
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				Log.e("URL---->", "--->" + url);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				responseBody = EntityUtils.toString(httpEntity);
				// is = httpEntity.getContent();
			}

			// Log.e("responswetrewrterteBody", "-->" + responseBody);

		} catch (UnsupportedEncodingException e) {
			isConnectionTimeOut = true;

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			isConnectionTimeOut = true;

			e.printStackTrace();
		} catch (IOException e) {

			isConnectionTimeOut = true;
			e.printStackTrace();
		}

		/*
		 * try { BufferedReader reader = new BufferedReader(new
		 * InputStreamReader( is, "iso-8859-1"), 8); StringBuilder sb = new
		 * StringBuilder(); String line = null; while ((line =
		 * reader.readLine()) != null) { sb.append(line + "\n"); } is.close();
		 * json = sb.toString(); } catch (Exception e) { Log.e("Buffer Error",
		 * "Error converting result " + e.toString()); }
		 */

		// try parse the string to a JSON object
		if (!isConnectionTimeOut) {
			if (isJSONValid(responseBody)) {

				// Log.e("valid json", "not valid json");

				try {
					jObj = new JSONObject(responseBody);
				} catch (JSONException e) {
					// Log.e("JSON Parser", "Error parsing data " +
					// e.toString());
				}
			}
		} else {
			JSONObject parent = new JSONObject();
			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("DATA", "NOTFOUND");
				parent.put("k2", jsonObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jObj = parent;
			isConnectionTimeOut = false;
			return jObj;
		}

		// return JSON String
		return jObj;

	}
	
	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}
}
