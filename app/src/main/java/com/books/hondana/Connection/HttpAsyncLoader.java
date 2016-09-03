package com.books.hondana.Connection;

import android.content.AsyncTaskLoader;
import android.content.Context;
import com.books.hondana.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpAsyncLoader extends AsyncTaskLoader<JSONObject> {

	private final static String TAG = HttpAsyncLoader.class.getSimpleName();

	private String strUrl = null; // WebAPIのURL
	private SearchAPI searchAPI;


	public HttpAsyncLoader(Context context, String url, SearchAPI searchAPI ) {
		super(context);
		this.strUrl = url;
		this.searchAPI = searchAPI;
	}

	@Override
	public JSONObject loadInBackground() {

		final StringBuffer result = new StringBuffer();

		HttpURLConnection conn = null;

		try {
			final URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (searchAPI == SearchAPI.GOOGLE) {
				conn.setRequestProperty("key", GoogleBookSearch.API_KEY);
			}
			conn.connect();

			final int status = conn.getResponseCode();

			if (status == HttpURLConnection.HTTP_OK) {
				final InputStream in = conn.getInputStream();
				//final String encoding = conn.getContentEncoding();
				final InputStreamReader inReader = new InputStreamReader(in);
				final BufferedReader bufReader = new BufferedReader(inReader);
				String line = null;

				while ((line = bufReader.readLine()) != null) {
					result.append(line);
				}
				bufReader.close();
				inReader.close();
				in.close();

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LogUtil.d(TAG, e.getMessage());
		} catch (ProtocolException e) {
			e.printStackTrace();
			LogUtil.d(TAG, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.d(TAG, e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(result.toString());
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return jsonObj;
	}
}