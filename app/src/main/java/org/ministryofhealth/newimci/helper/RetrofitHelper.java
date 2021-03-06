package org.ministryofhealth.newimci.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ministryofhealth.newimci.config.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chriz on 9/12/2017.
 */

public class RetrofitHelper {

    private static RetrofitHelper mInstance;

    public static RetrofitHelper getInstance() {
        if (mInstance == null){
            mInstance = new RetrofitHelper();
        }
        return mInstance;
    }

    public Retrofit createHelper(){
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        Gson gson = gbuilder.create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        return builder.build();
    }

    public Retrofit createHelper(Boolean type){
        final OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.APP_STATS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client);

        return builder.build();
    }
}
