package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.County;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 11/15/2017.
 */

public interface CountyService {
    @GET("/api/counties")
    Call<List<County>> get();
}
