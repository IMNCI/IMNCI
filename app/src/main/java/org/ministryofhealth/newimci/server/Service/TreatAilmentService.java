package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.TreatAilment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 10/31/2017.
 */

public interface TreatAilmentService {
    @GET("api/treat_ailments")
    Call<List<TreatAilment>> getAilments();
}
