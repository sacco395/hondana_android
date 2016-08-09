package com.books.hondana.Connection;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAsyncLoader extends AsyncTaskLoader<Bitmap>{

	private String imgUrl;

	public ImageAsyncLoader(Context context,String imgUrl) {
		super(context);
		this.imgUrl = imgUrl;
	}

	@Override
	/* 以下を参考しました。 */
	/* http://rei19.hatenablog.com/entry/2014/08/03/143520 */
	public Bitmap loadInBackground() {
	      try {
	            URL url = new URL(imgUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoInput(true);
	            connection.connect();
	            return BitmapFactory.decodeStream(connection.getInputStream());
	        } catch (IOException e) {
	            // 404はここでキャッチする
	            e.printStackTrace();
	        }
		return null;
	}//end method
}
