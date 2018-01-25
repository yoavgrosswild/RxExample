package com.example.yoavgross.rxexmaple;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yoavgross on 24/01/2018.
 */

public class RetrofitClient {

    private static IRetrofitCalls sRetrofitCallsInstance;

    /**
     * that method checks if the instance of IRetrofitCalls is null,
     * if so builds the retrofit client, sets the timeout we define, and GsonConverter.
     * else returns the already created IRetrofitCalls instance.
     *
     * @return instance of IRetrofitCalls
     */
    public static IRetrofitCalls getCallsInstance() {
        if (sRetrofitCallsInstance == null) {
            String baseUrl = "http://api.mabrukey.co.il/api/";
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sRetrofitCallsInstance = retrofit.create(IRetrofitCalls.class);
        }
        return sRetrofitCallsInstance;
    }
}
