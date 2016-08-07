package com.books.hondana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.books.hondana.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class BarcodeScanActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        // ボタンを取得し、タップした時の動作を指定
        Button buttonStartCamera = (Button) findViewById(R.id.buttonStartCamera);
        buttonStartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "buttonStartCamera onClick Start");
                new IntentIntegrator(BarcodeScanActivity.this).initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult Start");
        Log.d(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // 奇妙なことに null の場合
        if (intentResult == null) {
            Log.d(TAG, "Weird");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (intentResult.getContents() == null) {
            // 戻るボタンをタップした場合
            Log.d(TAG, "Cancelled Scan");

        } else {
            // カメラで読み取った情報をラベルに表示
            Log.d(TAG, "Scanned");
            TextView textViewScannedData = (TextView) findViewById(R.id.textViewScannedData);
            textViewScannedData.setText(intentResult.getContents());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
