package com.books.hondana;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Model.KiiBook;
import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.Model.Member;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/07/13.
 */
public class HondanaBookAdapter extends BaseAdapter {

    private static final String TAG = "HondanaBookAdapter";

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

        final KiiBook book = list.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(book);
            }
        });

        ImageView iv_bookCondition = ((ImageView) convertView.findViewById(R.id.MainBookConditionIcon));
        int resId = book.getConditionDrawableResId();
        if (resId != 0) {
            Context context = convertView.getContext();
            Drawable conditionDrawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            iv_bookCondition.setImageDrawable(conditionDrawable);
        }
        // 画像データのダウンロードと設定
        final ImageLoader imageLoader = ImageLoader.getInstance();

        String imgUrl = list.get(position).get(KiiBook.IMAGE_URL);
        ImageView imgView = ((ImageView)convertView.findViewById(R.id.imgTitle));
        imageLoader.displayImage(imgUrl,imgView);

        ((TextView)convertView.findViewById(R.id.rowTextTitle)).setText(list.get(position).get(KiiBook.TITLE));
        ((TextView)convertView.findViewById(R.id.rowTextAuthor)).setText(list.get(position).get(KiiBook.AUTHOR));

        final TextView bookOwner = (TextView) convertView.findViewById(R.id.book_owner);
        final ImageView userIcon = (ImageView) convertView.findViewById(R.id.user_icon);

        final String userId = book.get(KiiBook.USER_ID);
        final KiiCloudConnection membersConnection = new KiiCloudConnection(KiiCloudBucket.MEMBERS);
        membersConnection.loadMember(userId, new KiiCloudConnection.SearchFinishListener() {
            @Override
            public void didFinish(int token, KiiQueryResult<KiiObject> result, Exception e) {
                Log.d(TAG, "didFinish(result: " + result + ")");
                if (result == null) {
                    Log.w(TAG, e);
                    return;
                }

                final List<KiiObject> kiiObjects = result.getResult();
                Log.d(TAG, "members.size: " + kiiObjects.size());
                if (kiiObjects != null && kiiObjects.size() > 0) {
                    final KiiObject kiiObject = kiiObjects.get(0);// ひとつしか来ていないはずなので0番目だけ使う
                    final Member member = new Member(kiiObject);

                    final String name = member.get(Member.NAME);
                    Log.d(TAG, "name: " + name);
                    bookOwner.setText(name);

                    final String imageUrl = member.get(Member.IMAGE_URL);
                    Log.d(TAG, "imageUrl: " + imageUrl);
                    imageLoader.displayImage(imageUrl, userIcon);
                }
            }
        });

        return convertView;
    }




    public interface BookItemClickListener {
        void onClick(KiiBook book);
    }
}
