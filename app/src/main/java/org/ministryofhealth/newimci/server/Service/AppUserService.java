package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.AppUser;
import org.ministryofhealth.newimci.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by chriz on 9/12/2017.
 */

public interface AppUserService {
    @POST("/api/appuser/add")
    Call<AppUser> createAppUser(@Body AppUser appUser);

    @POST("/api/appuser/getuser/displayno")
    Call<UserProfile> getAppUser(@Field("display_no") String display_no);
}
