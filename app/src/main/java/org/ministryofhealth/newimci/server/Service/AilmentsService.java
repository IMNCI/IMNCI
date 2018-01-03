package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Ailment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 9/13/2017.
 */

public interface AilmentsService {
    @GET("/api/ailment")
    Call<List<Ailment>> getAilments();
}
