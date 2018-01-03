package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Review;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by chriz on 9/12/2017.
 */

public interface ReviewService {
    @POST("api/review")
    Call<Review> create(@Body Review review);
}
