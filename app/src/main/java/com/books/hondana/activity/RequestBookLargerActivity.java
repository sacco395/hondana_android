package com.books.hondana.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.kii.cloud.storage.KiiUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestBookLargerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RequestBookLargerActivity.class.getSimpleName();

    private Request request;
    private String addressNote;
    private String nameNote;


    public static Intent createIntent(Context context, Request request) {
        Intent intent = new Intent (context, RequestBookLargerActivity.class);
        intent.putExtra(Request.class.getSimpleName(), request);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_request_book_larger);

        request = getIntent ().getParcelableExtra (Request.class.getSimpleName());
        if (request == null) {
            throw new IllegalArgumentException ("createIntentを使ってください");
        }

        findViewById(R.id.buttonCancel).setOnClickListener(this);
        findViewById(R.id.buttonRequest).setOnClickListener(this);

        CheckBox cbSeveral = (CheckBox) findViewById(R.id.severalCheckBox);
        assert cbSeveral != null;
        cbSeveral.setChecked(false);
        cbSeveral.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextView userPhone = (TextView) findViewById(R.id.tv_user_phone);

        KiiUser kiiUser = KiiUser.getCurrentUser();
        String userId = kiiUser.getID();
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                final String address = member.getAddress();
                Log.d(TAG, "address: " + address);
                assert address != null;
                EditText noteField = (EditText) (findViewById (R.id.address_field));
                noteField.setText(address);


                final String fullName = member.getFullName();
                Log.d(TAG, "fullName: " + fullName);
                assert fullName != null;
                EditText nameField = (EditText) (findViewById (R.id.name_field));
                nameField.setText(fullName);

                final String phone = member.getPhone();
                Log.d(TAG, "phoneNumber: " + phone);
                assert userPhone != null;
                userPhone.setText(phone);
            }

            @Override
            public void failure(Exception e) {
                Log.e(TAG, "failure: ", e);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buttonCancel:
                    // クリック処理
                    finish();
                    break;

                case R.id.buttonRequest:
                        saveRequestDate();
                        saveMinusPoint();
                        saveClientData();
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
            case R.id.severalCheckBox:
                request.setSeveralBooks(cb.isChecked());
                break;
        }
    }

    private void saveClientData() {
        EditText noteField = (EditText) (findViewById (R.id.address_field));
        EditText nameField = (EditText) (findViewById (R.id.name_field));
        addressNote = noteField.getText ().toString ();
        nameNote = nameField.getText ().toString ();
            KiiUser kiiUser = KiiUser.getCurrentUser ();
            String userId = kiiUser.getID ();
            KiiMemberConnection.fetch (userId, new KiiObjectCallback<Member> () {
                @Override
                public void success(int token, Member member) {
                    member.setAddress (addressNote);
                    member.setFullName (nameNote);
                    member.save (false, new KiiModel.KiiSaveCallback () {
                        @Override
                        public void success(int token, KiiObject object) {
                            LogUtil.d (TAG, "save_full_name:" + nameNote);
                        }

                        @Override
                        public void failure(@Nullable Exception e) {
                            LogUtil.e (TAG, "failure: ", e);
                        }
                    });
                }

                @Override
                public void failure(Exception e) {

                }
            });
        }


    //kiiBookに交換リクエストの日時と申請したユーザーIDを保存
    private void saveRequestDate() {
        Date date = new Date ();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33

        KiiUser user = KiiUser.getCurrentUser();
        if (user == null || user.getID() == null) {
            Log.e(TAG, "saveRequestDate: KiiUser is null!");
            Toast.makeText(RequestBookLargerActivity.this, "ログイン情報を取得できませんでした。再度ログインしてください。", Toast.LENGTH_SHORT).show();
            // TODO: 9/13/16 Launch LoginActivity or something
            return;
        }

        request.setRequestedDate(dateString);
        request.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Toast.makeText(getApplicationContext(), "本のリクエストを完了しました", Toast.LENGTH_LONG).show();

                //暫定的にTOPページにintentする
                Intent intent = new Intent(RequestBookLargerActivity.this, BookMainActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(@Nullable Exception e) {
                Log.e(TAG, "failure: ", e);
                Toast.makeText(RequestBookLargerActivity.this, "本のリクエストに失敗しました。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMinusPoint(){
        KiiUser kiiUser = KiiUser.getCurrentUser();
        assert kiiUser != null;
        final String userId = kiiUser.getID();
        int diff = 0;
        KiiMemberConnection.updatePoint(userId, diff, new KiiObjectCallback<Member> () {
            @Override
            public void success(int token, Member member) {
                int current = member.getPoint();
                Log.d(TAG, "point:" + current);
                member.setPoint(current -1);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
