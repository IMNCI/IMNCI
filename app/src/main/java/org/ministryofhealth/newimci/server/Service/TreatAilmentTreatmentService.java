package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.TreatAilmentTreatment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 10/31/2017.
 */

public interface TreatAilmentTreatmentService {
    @GET("api/treat_ailment_treatments")
    Call<List<TreatAilmentTreatment>> get();
}
