package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.server.model.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by chriz on 1/3/2018.
 */

public interface UpdateContentService {
    @POST("api/check-last-update")
    @FormUrlEncoded
    Call<Response> getUpdateStatus(@Field("last_update") String last_update);
}
