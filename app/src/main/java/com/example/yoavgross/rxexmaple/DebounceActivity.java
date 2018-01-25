package com.example.yoavgross.rxexmaple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by yoavgross on 24/01/2018.
 */

public class DebounceActivity extends AppCompatActivity {

    private static final String TAG = DebounceActivity.class.getSimpleName();

    private List<String> mListStreets;
    private Disposable mDisposable;
    private MyAdapter mAdapter;
    private EditText mEtFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debounce_activity);
        setUpData();
        setRXDebounce();
    }

    private void setUpData() {
        mEtFilter = findViewById(R.id.et_filter);
        ListView lv = findViewById(R.id.lv);
        mListStreets = new ArrayList<>();
        readFile();
        mAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, mListStreets);
        lv.setAdapter(mAdapter);
    }

    private void readFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.massa2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                line = bufferedReader.readLine();
                mListStreets.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mListStreets.size(); i++) {
            String st = mListStreets.get(i);
            if (st != null) {
                st = st.replace("us,", "");
            }
            if (st != null && st.contains(",")) {
                st = st.substring(0, st.indexOf(","));
            }
            mListStreets.set(i, st);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void setRXDebounce() {
        mDisposable =
                RxTextView.textChangeEvents(mEtFilter)
                        .skip(1)
                        .debounce(400, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                        .filter(changes -> DataUtil.isNotNullOrEmpty(changes.text().toString()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getSearchObserver());
    }

    private DisposableObserver<TextViewTextChangeEvent> getSearchObserver() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onComplete() {
                Log.e(TAG, "--------- onComplete");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "--------- on error!");
            }

            @Override
            public void onNext(TextViewTextChangeEvent onTextChangeEvent) {
                String res = onTextChangeEvent.text().toString();
                mAdapter.filter(res);
            }
        };
    }


    private static class MyAdapter extends ArrayAdapter<String> {

        private List<String> mData;
        private List<String> mFilteredData;

        public MyAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            mData = objects;
            mFilteredData = new ArrayList<>();
            mFilteredData.addAll(objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }

        public void filter(String st) {
            mData.clear();
            if (st.isEmpty()) {
                mData.addAll(mFilteredData);
            } else {
                for (String s : mFilteredData) {
                    if (s != null && s.contains(st)) {
                        mData.add(s);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public String getItem(int position) {
            return mData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mData.size();
        }
    }
}
