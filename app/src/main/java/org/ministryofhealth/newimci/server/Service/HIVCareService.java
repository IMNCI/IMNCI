package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.HIVCare;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 11/21/2017.
 */

public interface HIVCareService {
    @GET("api/hiv-care")
    Call<List<HIVCare>> get();
}
