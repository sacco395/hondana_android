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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.Model.KiiCloudBucket;
import com.books.hondana.Model.Member;
import com.books.hondana.R;
import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
import com.kii.cloud.storage.callback.KiiUserCallBack;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    // define our UI elements
    private TextView mEmailField;
    private TextView mUsernameField;
    private TextView mPasswordField;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        // link our variables to UI elements
        mEmailField = (TextView) findViewById(R.id.email_field);
        mUsernameField = (TextView) findViewById(R.id.username_field);
        mPasswordField = (TextView) findViewById(R.id.password_field);

    }

    // called by the 'Sign Up' button on the UI
    public void handleSignUp(View v) {

        // show a loading progress dialog
        mProgress = ProgressDialog.show(LoginActivity.this, "",
                "Signing up...", true);

        // get the username/password combination from the UI
        String email = mEmailField.getText().toString();
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.v(TAG, "Registering: " + username + ":" + password);

        // create a KiiUser object
        try {
            KiiUser user = KiiUser.createWithEmail(username, email);
            // register the user asynchronously
            user.register(new KiiUserCallBack() {

                // catch the callback's "done" request
                public void onRegisterCompleted(int token, KiiUser user,
                                                Exception e) {

                    // hide our progress UI element
                    mProgress.cancel();

                    // check for an exception (successful request if e==null)
                    if (e == null) {

                        // tell the console and the user it was a success!
                        Log.v(TAG, "Registered: " + user.toString());
                        showToast("User registered!");


                    }

                    // otherwise, something bad happened in the request
                    else {

                        // tell the console and the user there was a failure
                        Log.v(TAG, "Error registering: " + e.getLocalizedMessage());
                        showToast("Error Registering: " + e.getLocalizedMessage());

                    }

                }

            }, password);

        } catch (Exception e) {
            mProgress.cancel();
            showToast("Error signing up: " + e.getLocalizedMessage());
        }

    }

    // called by the 'Log In' button on the UI
    public void handleLogin(View v) {

        // show a loading progress dialog
        mProgress = ProgressDialog.show(LoginActivity.this, "",
                "Signing in...", true);

        // get the username/password combination from the UI
        String Email = mEmailField.getText().toString();
        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.v(TAG, "Logging in: " + username + ":" + password);

        boolean result = false;

        // authenticate the user asynchronously
        KiiUser.logIn(new KiiUserCallBack() {

            // catch the callback's "done" request
            public void onLoginCompleted(int token, KiiUser user, Exception e) {

                // hide our progress UI element
                mProgress.cancel();

                // check for an exception (successful request if e==null)
                if (e == null) {
                    showToast("User authenticated!");

                    // Member の user_id が user.getID() と等しいものが欲しい
                    KiiQuery query = new KiiQuery (KiiClause.equals (Member.USER_ID, user.getID ()));

//                    KiiQuery allQuery = new KiiQuery ();

                    // 帰ってくる結果の上限
                    query.setLimit (1);

                    Kii.bucket (KiiCloudBucket.MEMBERS.getName ()).query (new KiiQueryCallBack<KiiObject> () {
                        @Override
                        public void onQueryCompleted(int token, @Nullable KiiQueryResult<KiiObject> result, @Nullable Exception exception) {
                            super.onQueryCompleted (token, result, exception);
                            if (exception == null) {
                                // Success!
                                if (result != null && result.getResult () != null) {
                                    KiiObject memberObj = result.getResult ().get (0);
                                    Member member = new Member (memberObj);
                                    Log.d (TAG, "onQueryCompleted: " + member.toString ());
                                } else {
                                    // Result is null! エラー処理
                                }
                            } else {
                                // エラー処理
                            }
                        }
                    }, query);

                    Intent myIntent = new Intent(LoginActivity.this,
                            BookMainActivity.class);
                    LoginActivity.this.startActivity(myIntent);
                }
                // otherwise, something bad happened in the request
                else {
                    // tell the console and the user there was a failure
                    //Log.v(TAG, "Logged in: " + username);
                    ;
                }
            }
        }, username, password);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
