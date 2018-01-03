package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.AilmentFollowUp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 9/26/2017.
 */

public interface AilmentFollowUpService {
    @GET("/api/ailment-followup")
    Call<List<AilmentFollowUp>> getAilmentFollowUps();
}
