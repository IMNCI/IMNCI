package org.ministryofhealth.newimci.server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.ministryofhealth.newimci.config.Constants;
import org.ministryofhealth.newimci.model.AppUser;
import org.ministryofhealth.newimci.server.Service.AppUserService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chriz on 9/12/2017.
 */

public class AppUserClient {
    private static AppUserClient mInstance;
    private Context context;

    public AppUserClient(Context ctx){
        this.context = ctx;
    }
    public static AppUserClient getInstance(Context ctx) {
        if (mInstance == null){
            mInstance = new AppUserClient(ctx);
        }
        return mInstance;
    }
    public void send(AppUser appUser){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        AppUserService client = retrofit.create(AppUserService.class);

        Call<AppUser> call = client.createAppUser(appUser);

        call.enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, Response<AppUser> response) {
                if (response.code() > 200){
                    try {
                        System.out.println("SOME TAGE: ERROR::: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {
                Log.e("SOME TAG", t.getMessage());
            }
        });
    }
}
