//
//
// Copyright 2012 Kii Corporation
// http://kii.com
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
//

package com.books.hondana.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Model.kii.KiiMember;
import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";

    // define our UI elements
    private TextView mPhoneField;
    private TextView mUsernameField;
    private TextView mPasswordField;
    private ProgressDialog mProgress;
    private String country = "JP";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        // link our variables to UI elements
        mPhoneField = (TextView) findViewById(R.id.phone_field);
        mUsernameField = (TextView) findViewById(R.id.username_field);
        mPasswordField = (TextView) findViewById(R.id.password_field);

    }

    // called by the 'Sign Up' button on the UI
    public void handleSignUp(View v) {

        // show a loading progress dialog
        mProgress = ProgressDialog.show(RegisterActivity.this, "",
                "Signing up...", true);

        // get the username/password combination from the UI
        String phone = mPhoneField.getText().toString();
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        LogUtil.d(TAG, "Registering: " + username + ":" + password);

        // create a KiiUser object
        try {
            KiiUser user = KiiUser.createWithPhone(username, phone);
            user.setCountry(country);
            // register the user asynchronously
            user.register(new KiiUserCallBack() {

                // catch the callback's "done" request
                public void onRegisterCompleted(int token, KiiUser user,
                                                final Exception e) {
                    // check for an exception (successful request if e==null)
                    if (e == null) {

                        // tell the console and the user it was a success!
                        LogUtil.d(TAG, "Registered: " + user.toString());

                        //自動ログイン(ログイン前)のためにSharedPreferenceに保存。アプリのストレージ。参考：http://qiita.com/Yuki_Yamada/items/f8ea90a7538234add288
                        //.apply()で保存
                        SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
                        pref.edit().putString(getString(R.string.save_token), user.getAccessToken()).apply();

                        createMember (user);

                        Intent myIntent = new Intent(RegisterActivity.this,
                                PhoneAuthActivity.class);
                        RegisterActivity.this.startActivity(myIntent);
                    }

                    // otherwise, something bad happened in the request
                    else {

                        // tell the console and the user there was a failure
                        LogUtil.d(TAG, "Error registering: " + e.getLocalizedMessage());
                        showToast("Error Registering: " + e.getLocalizedMessage());

                    }

                }

            }, password);

        } catch (Exception e) {
            mProgress.cancel();
            showToast("Error signing up: " + e.getLocalizedMessage());
        }

    }

    private void createMember(KiiUser user) {
        // 普通にインスタンス化
        KiiMember member = KiiMember.createNewMember(user);

        // サーバにポスト
        member.save (new KiiObjectCallBack () {
            @Override
            public void onSaveCompleted(int token, @NonNull KiiObject object, @Nullable Exception exception) {
                super.onSaveCompleted (token, object, exception);


                // hide our progress UI element
                mProgress.cancel();

                // check for an exception (successful request if e==null)
                if (exception == null) {

                    // tell the console and the user it was a success!
                    LogUtil.d(TAG, "Registered: " + object.toString());
                    showToast("登録ありがとうございます！");

                    //SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
                    //pref.edit().putString(getString(R.string.save_token), user.getAccessToken()).apply();

                    Intent myIntent = new Intent(RegisterActivity.this,
                            PhoneAuthActivity.class);
                    RegisterActivity.this.startActivity(myIntent);

                    finish();
                }

                // otherwise, something bad happened in the request
                else {

                    // tell the console and the user there was a failure
                    LogUtil.d(TAG, "Error registering: " + exception.getLocalizedMessage());
                    showToast("Error Registering: " + exception.getLocalizedMessage());

                }
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}