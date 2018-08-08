package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.KeyElements;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ElementsService {
    @GET("/api/elements/get")
    Call<KeyElements> get();
}
