package com.example.yoavgross.rxexmaple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subscribers.DisposableSubscriber;

import static android.text.TextUtils.isEmpty;

/**
 * Created by yoavgross on 24/01/2018.
 */

public class FormActivity extends AppCompatActivity {

    private static final String TAG = FormActivity.class.getSimpleName();
    private EditText mEtName;
    private EditText mEtProfession;
    private EditText mEtAge;
    private Button mBtnSend;
    private DisposableSubscriber<Boolean> mDisposableSubscriber;
    private Flowable<CharSequence> mNameFlowable;
    private Flowable<CharSequence> mProfessionFlowable;
    private Flowable<CharSequence> mAgeFlowable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity);
        mEtName = findViewById(R.id.et_name);
        mEtProfession = findViewById(R.id.et_profession);
        mEtAge = findViewById(R.id.et_age);
        mBtnSend = findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(v ->
                Toast.makeText(this, "sending data to server", Toast.LENGTH_SHORT).show());
        setRxValidation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposableSubscriber != null) {
            mDisposableSubscriber.dispose();
        }
    }

    private void setRxValidation() {
        mDisposableSubscriber = new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean formValid) {
                if (formValid) {
                    mBtnSend.setText("send");
                    mBtnSend.setEnabled(true);
                } else {
                    mBtnSend.setText("data incomplete");
                    mBtnSend.setEnabled(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "there was an error");
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "completed");
            }
        };
        mNameFlowable =
                RxTextView.textChanges(mEtName).toFlowable(BackpressureStrategy.LATEST);
        mProfessionFlowable =
                RxTextView.textChanges(mEtProfession).toFlowable(BackpressureStrategy.LATEST);
        mAgeFlowable =
                RxTextView.textChanges(mEtAge).toFlowable(BackpressureStrategy.LATEST);

        Flowable.combineLatest(
                mNameFlowable,
                mProfessionFlowable,
                mAgeFlowable,
                (name, profession, age) -> {
                    boolean nameValid = !isEmpty(name);
                    boolean professionValid = !isEmpty(profession);
                    boolean ageValid = !isEmpty(age) && Integer.parseInt(age.toString()) > 98;
                    return nameValid && professionValid && ageValid;
                })
                .subscribe(mDisposableSubscriber);
    }

}
