package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by chriz on 1/8/2018.
 */

public interface UserProfileService {
    @POST("api/profile")
    Call<UserProfile> addProfile(@Body UserProfile profile);
}
