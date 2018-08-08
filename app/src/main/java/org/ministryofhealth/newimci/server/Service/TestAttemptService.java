package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.TestAttempt;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TestAttemptService {
    @POST("/api/test/uploaduserattempt")
    Call<TestAttempt> addTestAttempt(@Body TestAttempt attempt);
}
