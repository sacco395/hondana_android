package com.books.hondana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.model.Book;
import com.books.hondana.model.BookInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<Book>  {

	private final static String TAG = BookListAdapter.class.getSimpleName();

	private LayoutInflater inflater;
	private Context context;
	private ImageView imageView;
	private List<Book> bookList;

	@Override
	public int getCount() {
		if ( bookList == null )
			return -1;
		return bookList.size();
	}

	public BookListAdapter(Context context, int resource,
			int textViewResourceId, List<Book> objects) {

		super(context, resource, textViewResourceId, objects);
		this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bookList = objects;
	}

	@Override
	public Book getItem(int position) {
		if ( bookList == null )
			return null;
		return bookList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.book_list_item,parent,false);

		// 表示項目の取得
		TextView titleText = (TextView)convertView.findViewById(R.id.txtTitle);
		TextView authorText = (TextView)convertView.findViewById(R.id.txtAuthor);
		TextView publisherText = (TextView)convertView.findViewById(R.id.txtPublisher);
		ImageView imgView = (ImageView)convertView.findViewById(R.id.imageView);

		// 表示するオブジェクトの取得
		Book book = getItem(position);

		// 表示データの設定
		BookInfo info = book.getInfo();
		titleText.setText(info.getTitle());
		publisherText.setText(info.getPublisher());
		authorText.setText(info.getAuthor());
		String imgUrl = info.getImageUrl();

		// 画像データのダウンロードと設定
		ImageLoader imageLoader = ImageLoader.getInstance();

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		imageLoader.displayImage(imgUrl,imgView);

		return convertView;
	}
}
