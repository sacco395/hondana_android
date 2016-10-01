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

package com.books.hondana.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.activity.UserPolicyActivity;
import com.books.hondana.model.Member;
import com.books.hondana.model.abst.KiiModel;
import com.books.hondana.model.exception.KiiModelException;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

public class RegisterActivity extends Activity {

    private static final String TAG = "RegisterActivity";

    // define our UI elements
    private TextView mPhoneField;
    private TextView mUsernameField;
    private TextView mPasswordField;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        // link our variables to UI elements
        mPhoneField = (TextView) findViewById(R.id.phone_field);
        mUsernameField = (TextView) findViewById(R.id.username_field);
        mPasswordField = (TextView) findViewById(R.id.password_field);

        ((CheckBox)findViewById(R.id.CheckBoxAgreeRegister)).setChecked(true);
        ((CheckBox)findViewById(R.id.CheckBoxAgreeRegister)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    //OFFに変更した時// TODO: 10/1 電話番号認証時にチェック判定on/off
                    Toast.makeText(RegisterActivity.this, "利用規約を読んで同意するにチェックを入れてください",
                            Toast.LENGTH_LONG).show();
                }else{
                    //ONに変更した時

                }
            }
        });

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
            String country = "JP";
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
                    }

                    // otherwise, something bad happened in the request
                    else {
                        mProgress.cancel();
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
    public void terms(View view) {
        Intent intent = new Intent(this, UserPolicyActivity.class);
        startActivity(intent);
    }


    private void createMember(KiiUser user) {
        Member member;
        try {
            member = Member.createNew(user);
        } catch (KiiModelException e) {
            mProgress.cancel();
            Log.e(TAG, "createMember: KiiModelException=", e);
            showToast("ユーザの登録に失敗しました。");
            return;
        }
        member.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                mProgress.cancel();

                // tell the console and the user it was a success!
                LogUtil.d(TAG, "Registered: " + object.toString());
                showToast("登録ありがとうございます！");

                Intent myIntent = new Intent(RegisterActivity.this,
                        PhoneAuthActivity.class);
                RegisterActivity.this.startActivity(myIntent);

                finish();
            }

            @Override
            public void failure(@Nullable Exception e) {
                mProgress.cancel();
                Log.e(TAG, "createMember: ", e);
                showToast("ユーザの登録に失敗しました。");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}