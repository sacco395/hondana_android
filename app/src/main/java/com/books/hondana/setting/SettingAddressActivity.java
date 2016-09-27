package com.books.hondana.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.books.hondana.R;
import com.books.hondana.connection.KiiMemberConnection;
import com.books.hondana.connection.KiiObjectCallback;
import com.books.hondana.model.Member;
import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;

public class SettingAddressActivity extends AppCompatActivity {

    private static final String TAG = "SettingAddressActivity";
    private String AddressNote;
    KiiUser user = KiiUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_setting);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("住所の登録・変更");
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final TextView UserAddress = (TextView) findViewById(R.id.tv_user_address);

        KiiUser kiiUser = KiiUser.getCurrentUser();
        assert kiiUser != null;
        final String userId = kiiUser.getID();
        KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
            @Override
            public void success(int token, Member member) {
                final String address = member.getAddress();
                Log.d(TAG, "address: " + address);
                assert UserAddress != null;
                UserAddress.setText(address);
            }

            @Override
            public void failure(Exception e) {
                UserAddress.setText("登録されていません");
                Log.e(TAG, "failure: ", e);
            }
        });

        Button postButton = (Button) findViewById(R.id.post_button);
        // ボタンにフォーカスを移動させる
        assert postButton != null;
        postButton.setFocusable(true);
        postButton.setFocusableInTouchMode(true);
        postButton.requestFocus();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText noteField = (EditText) (findViewById(R.id.address_field));
                assert noteField != null;
                AddressNote = noteField.getText().toString();
                KiiMemberConnection.fetch(userId, new KiiObjectCallback<Member>() {
                    @Override
                    public void success(int token, Member member) {
                        member.setAddress(AddressNote);
                        member.save(false, new KiiModel.KiiSaveCallback() {
                            @Override
                            public void success(int token, KiiObject object) {
                                TextView UserAddress = (TextView) findViewById(R.id.tv_user_address);
                                assert UserAddress != null;
                                UserAddress.setText(AddressNote);
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void failure(@Nullable Exception e) {

                            }
                        });
                    }

                    @Override
                    public void failure(Exception e) {

                    }
                });
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