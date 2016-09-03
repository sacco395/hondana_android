package com.books.hondana.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.books.hondana.R;
import com.books.hondana.util.LogUtil;
import com.kii.cloud.storage.KiiUser;
import com.kii.cloud.storage.callback.KiiUserCallBack;

/**
 * Created by MOOG on 16/08/19.
 */
public class PhoneAuthActivity extends Activity {

    private static final String TAG = "PhoneAuthActivity";

    // define our UI elements
    private TextView mCodeField;
    private ProgressDialog mProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.phone_auth);

        // link our variables to UI elements
        mCodeField = (TextView) findViewById(R.id.code_field);

    }

    // called by the '認証' button on the UI
    public void handleAuth(View v) {

        // show a loading progress dialog
        mProgress = ProgressDialog.show(PhoneAuthActivity.this, "",
                "Authenticate...", true);

        // get the code from the UI
        String code = mCodeField.getText().toString();
        LogUtil.d(TAG, "Authentication: " + code );

        try {
            final KiiUser user = KiiUser.getCurrentUser();
            // register the user asynchronously

            user.verifyPhone(new KiiUserCallBack() {
                @Override
                public void onVerifyPhoneCompleted(int token, Exception e) {

                    // hide our progress UI element
                    mProgress.cancel();

                    // check for an exception (successful request if e==null)
                    if (e == null) {
                        // tell the console and the user it was a success!
                        LogUtil.d(TAG, "Registered: " + user.toString());
                        showToast("User registered!");

                        Intent myIntent = new Intent(PhoneAuthActivity.this,
                                BookMainActivity.class);
                        PhoneAuthActivity.this.startActivity(myIntent);

                        finish();
                    }

                    // otherwise, something bad happened in the request
                    else {
                        // tell the console and the user there was a failure
                        LogUtil.d(TAG, "Error registering: " + e.getLocalizedMessage());
                        showToast("Error Registering: " + e.getLocalizedMessage());
                    }
                }
            }, code);

        } catch (Exception e) {
            mProgress.cancel();
            showToast("Error signing up: " + e.getLocalizedMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
