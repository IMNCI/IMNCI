package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Glossary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 10/21/2017.
 */

public interface GlossaryService {
    @GET("/api/glossary")
    Call<List<Glossary>> getGlossary();
}
