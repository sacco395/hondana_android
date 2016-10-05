package com.books.hondana.evaluation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.util.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EvaluationListViewAdapter extends BaseAdapter {

    private static final String TAG = EvaluationListViewAdapter.class.getSimpleName();

    private ArrayList<Request> mRequests;
    private EvaluationClickListener mListener;

    public EvaluationListViewAdapter(ArrayList<Request> requests, EvaluationClickListener listener) {
        this.mRequests = requests;
        this.mListener = listener;
    }
    public void add(List<Request> requests) {
        mRequests.addAll(requests);
    }

    public void add(Request request) {
        mRequests.add(request);
    }

    @Override
    public int getCount() {
        return mRequests.size();
    }

    @Override
    public Object getItem(int position) {
        return mRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemLayout = inflater.inflate(R.layout.part_evaluation_list, null);

            ImageView ivClientUserIcon = (ImageView) itemLayout.findViewById(R.id.client_user_icon);
            ImageView ivEvaluationIcon = (ImageView) itemLayout.findViewById(R.id.evaluation_icon);
            TextView tvClientUser = (TextView) itemLayout.findViewById(R.id.client_user);
            TextView tvEvaluationComment = (TextView) itemLayout.findViewById(R.id.evaluation_comment);
            TextView tvEvaluatedDate = (TextView) itemLayout.findViewById(R.id.evaluated_date);
            itemLayout.setTag(new ViewHolder(ivClientUserIcon, ivEvaluationIcon, tvClientUser,tvEvaluationComment,tvEvaluatedDate));

            convertView = itemLayout;
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        final Request request = mRequests.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(request);
            }
        });
        int resId = request.getIconDrawableResId();
        if (resId != 0) {
            Context context = convertView.getContext();
            Drawable evaluationDrawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
            holder.ivEvaluationIcon.setImageDrawable(evaluationDrawable);
        }

        holder.tvEvaluationComment.setText(request.getEvaluateMessage());
        holder.tvEvaluatedDate.setText(request.getReceivedDate());

        final String clientId = request.getClientId();
        KiiMemberConnection.fetch(clientId, new KiiObjectCallback<Member> () {
            @Override
            public void success(int token, Member member) {
                final String name = member.getName();
                LogUtil.d(TAG, "name: " + name);
                holder.tvClientUser.setText(name);

                if (!member.hasValidImageUrl()) {
                    return;
                }

                final String imageUrl = member.getImageUrl();
                LogUtil.d(TAG, "imageUrl: " + imageUrl);
                Picasso.with(parent.getContext())
                        .load(imageUrl)
                        .into(holder.ivClientUserIcon);
            }

            @Override
            public void failure(Exception e) {
                LogUtil.e(TAG, "failure: ", e);
            }
        });

        return convertView;
    }

    public interface EvaluationClickListener {
        void onClick(Request request);
    }

    private static class ViewHolder {
        public ImageView ivClientUserIcon;
        public ImageView ivEvaluationIcon;
        public TextView tvClientUser;
        public TextView tvEvaluationComment;
        public TextView tvEvaluatedDate;

        public ViewHolder(ImageView ivClientUserIcon,ImageView ivEvaluationIcon, TextView tvClientUser, TextView tvEvaluationComment, TextView tvEvaluatedDate) {
            this.ivClientUserIcon = ivClientUserIcon;
            this.ivEvaluationIcon = ivEvaluationIcon;
            this.tvClientUser = tvClientUser;
            this.tvEvaluationComment = tvEvaluationComment;
            this.tvEvaluatedDate = tvEvaluatedDate;
        }
    }
}

