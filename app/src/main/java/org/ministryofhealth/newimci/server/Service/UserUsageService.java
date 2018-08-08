package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.UserUsage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserUsageService {
    @POST("/api/userusage/add")
    Call<UserUsage> add(@Body UserUsage usage);
}
