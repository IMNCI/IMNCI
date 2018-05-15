package org.ministryofhealth.newimci.server;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.SetupActivity;
import org.ministryofhealth.newimci.config.Constants;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AppUser;
import org.ministryofhealth.newimci.model.UserProfile;
import org.ministryofhealth.newimci.server.Service.AppUserService;
import org.ministryofhealth.newimci.server.Service.UserProfileService;
import org.ministryofhealth.newimci.server.model.ProfileRequest;

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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson));

        final Retrofit retrofit = builder.build();
        AppUserService client = retrofit.create(AppUserService.class);

        Call<AppUser> call = client.createAppUser(appUser);

        call.enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(@NonNull Call<AppUser> call, @NonNull Response<AppUser> response) {
                if (response.code() > 200){
                    try {
                        System.out.println("SOME TAGE: ERROR::: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.addAppUser(response.body());

                    assert response.body() != null;
                    int app_user_id = response.body().getId();
                    assert response.body() != null;
                    String token = response.body().getPhone_id();

                    UserProfileService userProfileService = retrofit.create(UserProfileService.class);
                    ProfileRequest profileRequest = new ProfileRequest();

                    profileRequest.setApp_user_id(app_user_id);
                    profileRequest.setToken(token);
                    Call<UserProfile> userProfileCall = userProfileService.getProfile(profileRequest);

                    userProfileCall.enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> uresponse) {
                            if (uresponse.body().getId() != 0) {
                                assert uresponse.body() != null;
                                saveUserPreference(uresponse.body(), true);
                            }else{
                                Log.w("UserProfile", "There is no profile tied to the phone");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            t.getMessage();
                            Log.e("User", "There was an error" + t.getMessage());
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {
                Log.e("SOME TAG", t.getMessage());
            }
        });
    }

    public void saveUserPreference(UserProfile profile, Boolean uploaded){
        SharedPreferences pref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("id", profile.getId());
        editor.putString("email", profile.getEmail());
        editor.putString("phone", profile.getPhone());
        editor.putString("gender", profile.getGender());
        editor.putString("age_group", profile.getAge_group());
        editor.putString("county", profile.getCounty());
        editor.putString("profession", profile.getProfession());
        editor.putString("cadre", profile.getCadre());
        editor.putString("sector", profile.getSector());
        editor.putBoolean("uploaded", uploaded);
        editor.apply();
    }
}
