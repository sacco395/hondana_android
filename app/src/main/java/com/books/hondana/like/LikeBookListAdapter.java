package com.books.hondana.like;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.MyBookListAdapter;
import com.books.hondana.R;
import com.books.hondana.model.Like;

import java.util.ArrayList;
import java.util.List;

public class LikeBookListAdapter extends BaseAdapter {

    private static final String TAG = MyBookListAdapter.class.getSimpleName();

    private ArrayList<Like> mLikes;
    private LikeItemClickListener mListener;

    public LikeBookListAdapter(ArrayList<Like> likes, LikeItemClickListener listener) {
        this.mLikes = likes;
        this.mListener = listener;
    }

    public void add(List<Like> likes) {
        mLikes.addAll(likes);
    }

    public void add(Like like) {
        mLikes.add(like);
    }

    @Override
    public int getCount() {
        return mLikes.size();
    }

    @Override
    public Object getItem(int position) {
        return mLikes.get(position);
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

            // item_book というレイアウトの名前は、何も伝えていません。
            // また、あたかもリスト表示の1アイテムであるかのような印象を与えます。(実際にはGrid)
            // item_book とかいいかと思います。
            View itemLayout = inflater.inflate(R.layout.part_my_book_list, null);

            // ID の命名もう少し考えた方がいいです。
            // 辞書引いてでも、しっくりくる名前を考えるべきです。Title って聞いたら、
            // 文字列を連想して、TextView かと思ってしまいます。
            // ここでは表紙の画像なので、imgCovert とかがいいかと。
            ImageView ivCover = (ImageView) itemLayout.findViewById(R.id.iv_BookImg);
            TextView tvTitle = (TextView) itemLayout.findViewById(R.id.tv_BookTitle);
            TextView tvAuthor = (TextView) itemLayout.findViewById(R.id.tv_BookAuthor);
            itemLayout.setTag(new ViewHolder(ivCover, tvTitle, tvAuthor));

            convertView = itemLayout;
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final Like like = mLikes.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(like);
            }
        });

        String bookId = like.getBookId();
//        Like bookId = book.
//        String coverUrl = info.getImageUrl();
//
//        // http://square.github.io/picasso/
//        Picasso.with(convertView.getContext())
//                .load(coverUrl)
//                .into(holder.ivCover);
//
//        holder.tvTitle.setText(info.getTitle());
//        holder.tvAuthor.setText(info.getAuthor());


        return convertView;
    }

    public interface LikeItemClickListener {
        void onClick(Like like);
    }

    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;

        public ViewHolder(ImageView ivCover, TextView tvTitle, TextView tvAuthor) {
            this.ivCover = ivCover;
            this.tvTitle = tvTitle;
            this.tvAuthor = tvAuthor;
        }
    }
}

