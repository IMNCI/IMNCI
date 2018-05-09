package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryService {
    @GET("/api/countries")
    Call<List<Country>> get();
}
