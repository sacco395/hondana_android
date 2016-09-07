package com.books.hondana;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.Model.KiiBook;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/07/13.
 */
public class HondanaBookAdapter extends BaseAdapter {

    private ArrayList<KiiBook> list;
    private Context context;
    private LayoutInflater layoutInflater = null;
    private BookItemClickListener listener;

    public HondanaBookAdapter(Context context, ArrayList<KiiBook> list, BookItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    public void clear(){
        list.clear();
    }

    public void add(KiiBook book){
        list.add(book);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        //  (Bug!)      return Integer.parseInt(list.get(position).get(KiiBook.BOOK_ID));
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.row,parent,false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KiiBook book = list.get(position);
                listener.onClick(book);
            }
        });

        // 画像データのダウンロードと設定
        ImageLoader imageLoader = ImageLoader.getInstance();

        String imgUrl = list.get(position).get(KiiBook.IMAGE_URL);
        ImageView imgView = ((ImageView)convertView.findViewById(R.id.imgTitle));
        imageLoader.displayImage(imgUrl,imgView);

        ((TextView) convertView.findViewById(R.id.rowTextTitle)).setText(list.get(position).get(KiiBook.TITLE));
        ((TextView)convertView.findViewById(R.id.rowTextAuthor)).setText(list.get(position).get(KiiBook.AUTHOR));

        return convertView;
    }

    public interface BookItemClickListener {
        void onClick(KiiBook book);
    }
}
