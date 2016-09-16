package com.books.hondana.Setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.books.hondana.R;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.UserFields;
import com.kii.cloud.storage.callback.KiiUserUpdateCallback;

public class SettingAddressActivity extends AppCompatActivity {

    private static final String TAG = "SettingAddressActivity";
    private Button postButton;
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

        TextView UserAddress = (TextView) findViewById(R.id.tv_user_address);

        final String address = user.getString("address");
        if (address.equals("")) {
            UserAddress.setText("登録されていません");
        } else {
            UserAddress.setText(address);
        }

        postButton = (Button) findViewById(R.id.post_button);
        // ボタンにフォーカスを移動させる
        postButton.setFocusable(true);
        postButton.setFocusableInTouchMode(true);
        postButton.requestFocus();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //備考欄のテキストここから
                final EditText noteField = (EditText) (findViewById(R.id.address_field));
                assert noteField != null;
                AddressNote = noteField.getText().toString();
                UserFields userFields = new UserFields();
                userFields.set("address", AddressNote);

                user.update(null, userFields, new KiiUserUpdateCallback() {
                    @Override
                    public void onUpdateCompleted(KiiUser KiiUser, Exception exception) {
                        if (exception != null) {
                            // Error handling
                            return;
                        } else {
                            TextView UserAddress = (TextView) findViewById(R.id.tv_user_address);
                            assert UserAddress != null;
                            UserAddress.setText(address);
                            finish();
                            startActivity(getIntent());
                        }
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