package com.books.hondana;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.Connection.KiiCloudConnection;
import com.books.hondana.Model.book.Book;
import com.books.hondana.Model.book.Condition;
import com.books.hondana.Model.book.Info;
import com.books.hondana.Model.kii.KiiCloudBucket;
import com.books.hondana.Model.kii.KiiMember;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/07/13.
 */
public class HondanaBookAdapter extends BaseAdapter {

    private static final String TAG = HondanaBookAdapter.class.getSimpleName();

    private ArrayList<Book> mBooks;
    private BookItemClickListener mListener;

    public HondanaBookAdapter(ArrayList<Book> books, BookItemClickListener listener) {
        this.mBooks = books;
        this.mListener = listener;
    }

    public void clear() {
        mBooks.clear();
    }

    public void add(Book book) {
        mBooks.add(book);
        Log.d(TAG, "add: " + mBooks.size());
    }

    @Nullable
    public Book getLastItem() {
        if (mBooks.isEmpty()) {
            return null;
        }
        return mBooks.get(getCount() - 1);
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        // ViewHolder パターンというのがあって、これによってパフォーマンスの向上が見込めます
        // http://outofmem.hatenablog.com/entry/2014/10/29/040510
        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // row というレイアウトの名前は、何も伝えていません。
            // また、あたかもリスト表示の1アイテムであるかのような印象を与えます。(実際にはGrid)
            // item_book とかいいかと思います。
            View itemLayout = inflater.inflate(R.layout.row, null);

            // ID の命名もう少し考えた方がいいです。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。Title って聞いたら、
            // 文字列を連想して、TextView かと思ってしまいます。
            // ここでは表紙の画像なので、imgCovert とかがいいかと。
            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.imgTitle);
            ImageView ivCondition = (ImageView) itemLayout.findViewById(R.id.MainBookConditionIcon);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.rowTextTitle);
            TextView tvAuthor = (TextView) itemLayout.findViewById(R.id.rowTextAuthor);

            ImageView ivOwnerImage = (ImageView) itemLayout.findViewById(R.id.user_icon);
            TextView tvOwnerName = (TextView) itemLayout.findViewById(R.id.book_owner);
            itemLayout.setTag(new ViewHolder(ivCover, ivCondition, tvTitle, tvAuthor, ivOwnerImage, tvOwnerName));

            convertView = itemLayout;
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final Book book = mBooks.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(book);
            }
        });

        Info info = book.getInfo();
        String coverUrl = info.getImageUrl();

        // http://square.github.io/picasso/
        Picasso.with(convertView.getContext())
                .load(coverUrl)
                .into(holder.ivCover);

        Condition condition = book.getCondition();
        int resId = condition.getIconDrawableResId();
        if (resId != 0) {
            Context context = convertView.getContext();
            Drawable conditionDrawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            holder.ivConditionIcon.setImageDrawable(conditionDrawable);
        }

        holder.tvTitle.setText(info.getTitle());
        holder.tvAuthor.setText(info.getAuthor());

        final String userId = book.getOwnerId();
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
                    final KiiMember kiiMember = new KiiMember(kiiObject);

                    final String name = kiiMember.get(KiiMember.NAME);
                    Log.d(TAG, "name: " + name);
                    holder.tvOwnerName.setText(name);

                    final String imageUrl = kiiMember.get(KiiMember.IMAGE_URL);
                    Log.d(TAG, "imageUrl: " + imageUrl);
                    Picasso.with(parent.getContext())
                            .load(imageUrl)
                            .into(holder.ivOwnerIcon);
                }
            }
        });

        return convertView;
    }

    public interface BookItemClickListener {
        void onClick(Book book);
    }

    private static class ViewHolder {
        public ImageView ivCover;
        public ImageView ivConditionIcon;
        public TextView tvTitle;
        public TextView tvAuthor;
        public ImageView ivOwnerIcon;
        public TextView tvOwnerName;

        public ViewHolder(ImageView ivCover, ImageView ivConditionIcon, TextView tvTitle, TextView tvAuthor, ImageView ivOwnerIcon, TextView tvOwnerName) {
            this.ivCover = ivCover;
            this.ivConditionIcon = ivConditionIcon;
            this.tvTitle = tvTitle;
            this.tvAuthor = tvAuthor;
            this.ivOwnerIcon = ivOwnerIcon;
            this.tvOwnerName = tvOwnerName;
        }
    }
}
