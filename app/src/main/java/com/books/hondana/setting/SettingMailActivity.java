package com.books.hondana.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.books.hondana.R;
import com.kii.cloud.storage.IdentityData;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserUpdateCallback;

public class SettingMailActivity extends AppCompatActivity {

    private static final String TAG = "SettingAddressActivity";
    private String Note;
    KiiUser user = KiiUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_setting);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("メールアドレスの登録・変更");
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView UserMail = (TextView) findViewById(R.id.tv_user_address);
        String user_mail = user.getEmail();
        if (user_mail == null) {
            assert UserMail != null;
            UserMail.setText("登録されていません");
        } else {
            assert UserMail != null;
            UserMail.setText(user_mail);
        }

        Button postButton = (Button) findViewById(R.id.post_button);
        // ボタンにフォーカスを移動させる
        assert postButton != null;
        postButton.setFocusable(true);
        postButton.setFocusableInTouchMode(true);
        postButton.requestFocus();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //備考欄のテキストここから
                EditText noteField = (EditText) (findViewById(R.id.mailAddress_field));
                assert noteField != null;
                Note = noteField.getText().toString();
                IdentityData.Builder builder = IdentityData.Builder.newWithEmail(Note);
                IdentityData identityData = builder.build();

                user.update(identityData, null, new KiiUserUpdateCallback() {
                    @Override
                    public void onUpdateCompleted(@NonNull KiiUser KiiUser, Exception exception) {
                        if (exception != null) {
                            // Error handling
                            return;
                        } else {
                            TextView UserMail = (TextView) findViewById(R.id.tv_user_address);
                            assert UserMail != null;
                            UserMail.setText(user.getEmail());
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