package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.TreatTitle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 10/23/2017.
 */

public interface TreatTitlesService {
    @GET("api/titles")
    Call<List<TreatTitle>> getTitles();
}
