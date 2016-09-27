//本の受け取り完了（serverIdへの評価）
package com.books.hondana.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.books.hondana.R;
import com.books.hondana.model.Book;
import com.books.hondana.model.Evaluation;
import com.books.hondana.model.Request;
import com.books.hondana.model.abst.KiiModel;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;

public class ReceivedBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ReceivedBookActivity.class.getSimpleName();

    private Request request;
    private Book book;
    private Evaluation targetEvaluation;


//    public static Intent createIntent(Context context, Request request) {
//        Intent intent = new Intent(context, ReceivedBookActivity.class);
//        intent.putExtra(Request.class.getSimpleName(), request);
//        return intent;
//    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_book);

        request = getIntent().getParcelableExtra(Request.class.getSimpleName());
        book = getIntent().getParcelableExtra(Book.class.getSimpleName());

//        request = getIntent ().getParcelableExtra(Request.class.getSimpleName());
//        if (request == null) {
//            throw new IllegalArgumentException ("createIntentを使ってください");
//        }

        findViewById(R.id.buttonReceived).setOnClickListener(this);
    }

    //kiiBookに本の受け取り日時を記録して保存する
    @Override
    public void onClick(View v) {
//        Date date = new Date ();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss", Locale.JAPAN);
//        String dateString = simpleDateFormat.format (date); // 2016-09-03 17:24:33
//
//        request.setReceivedDate(dateString);
//        request.save(false, new KiiModel.KiiSaveCallback() {
//            @Override
//            public void success(int token, KiiObject object) {
//                Toast.makeText(ReceivedBookActivity.this, "本の受け取りと評価を完了しました", Toast.LENGTH_LONG).show();
//                //暫定的にTOPページにintentする
//                Intent intent = new Intent(ReceivedBookActivity.this, BookMainActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void failure(@Nullable Exception e) {
//                Log.e(TAG, "failure: ", e);
//                Toast.makeText(ReceivedBookActivity.this, "保存に失敗しました。", Toast.LENGTH_SHORT).show();
//            }
//        });

        ((RadioGroup)findViewById(R.id.evaluation)).setOnCheckedChangeListener
                (new RadioGroup.OnCheckedChangeListener () {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.eva_excellent){
                            //１つめを選択
                            targetEvaluation.setEvaluationByClient(Evaluation.EVALUATION_EXCELLENT);
                        }else if(checkedId == R.id.eva_good){
                            //２つめを選択
                            targetEvaluation.setEvaluationByClient(Evaluation.EVALUATION_GOOD);
                        }else if(checkedId == R.id.eva_bad){
                            //３つめを選択
                            targetEvaluation.setEvaluationByClient(Evaluation.EVALUATION_BAD);
                        }
                    }

                });

        EditText noteField = (EditText) (findViewById (R.id.evaluation_comment));
        assert noteField != null;
        String noteStr = noteField.getText().toString ();
        // 入力された文字を取得して保存
        targetEvaluation.setCommentByClient(noteStr);
        KiiUser currentUser = KiiUser.getCurrentUser ();
        targetEvaluation.setServerId(currentUser.getID());
        targetEvaluation.setEvaluationByClient(evaluationByClient);
        targetEvaluation.save(false, new KiiModel.KiiSaveCallback() {
            @Override
            public void success(int token, KiiObject object) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(@Nullable Exception e) {

            }
        });
    }
}
