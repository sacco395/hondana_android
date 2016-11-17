package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendBookLargerActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = SendBookLargerActivity.class.getSimpleName();

    private Request request;

    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent(context, SendBookLargerActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_send_book_larger);

        request = getIntent().getParcelableExtra(Request.class.getSimpleName());
        if (request == null) {
            throw new IllegalArgumentException("createIntentを使ってください");
        }

        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonSent).setOnClickListener(this);

        TextView tvDate = (TextView)findViewById(R.id.tv_date);
        String requested_date = request.getRequestedDate ();
        LogUtil.d(TAG, "requested_date: " + requested_date);
        tvDate.setText(requested_date);

        TextView tv_accept_several = (TextView)findViewById(R.id.several);
        String acceptSeveral = request.getSeveralBooksText();
        if (!acceptSeveral.equals("")) {
            tv_accept_several.setText (acceptSeveral);
        }

        CheckBox cbAcceptSeveral = (CheckBox) findViewById(R.id.acceptSeveralCheckBox);
        assert cbAcceptSeveral != null;
        cbAcceptSeveral.setChecked(false);
        cbAcceptSeveral.setOnClickListener(this);

        final TextView message = (TextView) findViewById(R.id.tv_client_message);
        String requestMessage = request.getRequestMessage();
        Log.d(TAG, "requestMessage: " + requestMessage);
        if (!requestMessage.equals ("")) {
            assert message != null;
            message.setText(requestMessage);
        }

        final TextView clientName = (TextView)findViewById(R.id.client_name);
        final TextView clientFullName = (TextView)findViewById(R.id.tv_client_full_name);
        final TextView clientAddress = (TextView)findViewById(R.id.tv_client_address);
        final TextView clientPhone = (TextView)findViewById(R.id.tv_client_phone);
        final String clientId = request.getClientId();
        LogUtil.d(TAG, "clientId: " + clientId);
        KiiMemberConnection.fetch(clientId, new KiiObjectCallback<Member> () {
            @Override
            public void success(int token, Member member) {
                final String name = member.getName();
                LogUtil.d(TAG, "name: " + name);
                assert clientName != null;
                clientName.setText(name + "さんから");

                final String fullName = member.getFullName();
                LogUtil.d(TAG, "fullName: " + fullName);
                assert clientFullName != null;
                clientFullName.setText(fullName);

                final String Address = member.getAddress();
                LogUtil.d(TAG, "clientAddress: " + clientAddress);
                assert clientAddress != null;
                clientAddress.setText(Address);

                final String Phone = member.getPhone();
                LogUtil.d(TAG, "clientPhone: " + clientPhone);
                assert clientPhone != null;
                clientPhone.setText(Phone);
            }

            @Override
            public void failure(Exception e) {
                LogUtil.e(TAG, "failure: ", e);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar02);
        setSupportActionBar(toolbar);
    }
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonSent:
                    // クリック処理
                    saveSendDate();
                    break;


                default:
                    break;
            }
        }
        if (!(v instanceof CheckBox)) {
            return;
        }
        CheckBox cb = (CheckBox) v;
        switch (cb.getId()) {
            case R.id.acceptSeveralCheckBox:
                request.setAcceptSeveralBooks(cb.isChecked());
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //kiiBookに本の発送完了日時を記録して保存する
    private void saveSendDate() {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format(date); // 2016-09-03 17:24:33

        request.setSentDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(getApplicationContext(), "本の発送を完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(SendBookLargerActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(SendBookLargerActivity.this, "本の発送に失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
