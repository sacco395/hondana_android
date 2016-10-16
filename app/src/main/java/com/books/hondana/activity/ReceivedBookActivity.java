//本の受け取り完了（serverIdへの評価）
package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Member;
import com.books.hondana.model.Request;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.utils.Log;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceivedBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ReceivedBookActivity.class.getSimpleName();

    private Request request;


    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent(context, ReceivedBookActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_book);
        request = getIntent ().getParcelableExtra(Request.class.getSimpleName());
        if (request == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonReceived).setOnClickListener(this);

        final TextView ServerName = (TextView)findViewById(R.id.server_name);
        final String serverId = request.getServerId();
        LogUtil.d(TAG, "serverId: " + serverId);
        KiiMemberConnection.fetch(serverId, new KiiObjectCallback<Member> () {
            @Override
            public void success(int token, Member member) {
                final String name = member.getName();
                LogUtil.d(TAG, "name: " + name);
                ServerName.setText("本が到着したら" + name + "さんの評価をしてください");
            }

            @Override
            public void failure(Exception e) {
                LogUtil.e(TAG, "failure: ", e);
            }
        });

        TextView tvDate = (TextView)findViewById(R.id.tv_date);
        TextView tvBookTitle = (TextView)findViewById(R.id.tv_BookTitle);
        ImageView BookImg = (ImageView)findViewById(R.id.iv_BookImg);


        String requested_date = request.getRequestedDate();
        String sent_date = request.getSentDate();
        final String BookTitle = request.getBookTitle();

        if (!sent_date.equals ("")) {
            assert tvDate != null;
            tvDate.setText(sent_date);
            assert tvBookTitle != null;
            tvBookTitle.setText("「" + BookTitle + "」が発送されました。本が届いたら相手の評価をしましょう\n");
        } else {
            assert tvDate != null;
            tvDate.setText(requested_date);
            tvBookTitle.setText("「" + BookTitle + "」にリクエストしました");
        }

        TextView tv_parcel = (TextView)findViewById(R.id.parcel);
        String parcel = request.getParcelByServerText();
        if (!parcel.equals("")) {
            tv_parcel.setText (parcel);
        }

        String coverUrl = request.getBookImageUrl();

        Picasso.with(ReceivedBookActivity.this)
                .load(coverUrl)
                .into(BookImg);


        ((RadioGroup)findViewById(R.id.evaluation)).setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener () {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.eva_excellent){
                            //１つめを選択
                            request.setEvaluationByClient(Request.EVALUATION_EXCELLENT);
                        }else if(checkedId == R.id.eva_good){
                            //２つめを選択
                            request.setEvaluationByClient(Request.EVALUATION_GOOD);
                        }else if(checkedId == R.id.eva_bad){
                            //３つめを選択
                            request.setEvaluationByClient(Request.EVALUATION_BAD);
                        }
                    }

                });
    }

    //kiiBookに本の受け取り日時を記録して保存する
    @Override
    public void onClick(View v) {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        EditText noteField = (EditText) (findViewById (R.id.evaluation_comment));
        assert noteField != null;
        String noteStr = noteField.getText().toString ();
        // 入力された文字を取得して保存
        request.setEvaluateMessage(noteStr);
        request.setReceivedDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(ReceivedBookActivity.this, "本の受け取りと評価を完了しました", Toast.LENGTH_LONG).show();
                //暫定的にTOPページにintentする
                Intent intent = new Intent(ReceivedBookActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(ReceivedBookActivity.this, "保存に失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
        final String serverId = request.getServerId();
        LogUtil.d(TAG,"server_id:"+serverId);
        int diff = +1;
        KiiMemberConnection.updatePoint(serverId, diff, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                int current = member.getPoint();
                android.util.Log.d(TAG, "point:" + current);
                member.setPoint(current +1);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }
}
