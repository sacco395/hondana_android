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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
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
        Log.v(TAG, "Registering: " + username + ":" + password);

        // create a KiiUser object
        try {
            KiiUser user = KiiUser.createWithPhone(username, phone);
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

                        Intent myIntent = new Intent(RegisterActivity.this,
                                PhoneAuthActivity.class);
                        RegisterActivity.this.startActivity(myIntent);


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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

