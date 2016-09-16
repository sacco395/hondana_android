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

package com.books.hondana.Login_Register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Connection.KiiMemberConnection;
import com.books.hondana.Connection.KiiObjectCallback;
import com.books.hondana.Model.Member;
import com.books.hondana.R;
import com.books.hondana.activity.BookMainActivity;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    // define our UI elements
    private TextView mPhoneField;
    private TextView mPasswordField;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        // link our variables to UI elements
        mPhoneField = (TextView) findViewById(R.id.phone_field);
        mPasswordField = (TextView) findViewById(R.id.password_field);

    }

    // called by the 'Log In' button on the UI
    public void handleLogin(View v) {

        // show a loading progress dialog
        mProgress = ProgressDialog.show(LoginActivity.this, "",
                "Signing in...", true);

        // get the username/password combination from the UI
        String phone = mPhoneField.getText().toString();
        String password = mPasswordField.getText().toString();
        LogUtil.d(TAG, "Logging in: " + phone + ":" + password);

        // authenticate the user asynchronously
        String country = "JP";
        KiiUser.logInWithLocalPhone(new KiiUserCallBack() {

            // catch the callback's "done" request
            public void onLoginCompleted(int token, KiiUser user, Exception e) {

                // hide our progress UI element
                mProgress.cancel();

                if (e != null) {
                    showToast("ログインできません。もう一度やり直して下さい。");
                    return;
                }

                SharedPreferences pref = getSharedPreferences(getString(R.string.save_data_name), Context.MODE_PRIVATE);
                pref.edit().putString(getString(R.string.save_token), user.getAccessToken()).apply();//保存されていない時は""
                showToast("User authenticated!");

                KiiMemberConnection.fetch(user.getID(), new KiiObjectCallback<Member>() {
                    @Override
                    public void success(int token, Member member) {
                        LogUtil.d (TAG, "onQueryCompleted: " + member.toString());

                        Intent myIntent = new Intent(LoginActivity.this,
                                BookMainActivity.class);
                        LoginActivity.this.startActivity(myIntent);
                    }

                    @Override
                    public void failure(Exception e) {
                        Log.e(TAG, "failure: ", e);
                        showToast("ログインできません。もう一度やり直して下さい。");
                    }
                });
            }
        }, phone, password, country);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
