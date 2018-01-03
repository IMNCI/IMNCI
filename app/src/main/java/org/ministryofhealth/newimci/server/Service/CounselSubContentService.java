package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.CounselSubContent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 11/14/2017.
 */

public interface CounselSubContentService {
    @GET("/api/counsel-sub-contents")
    Call<List<CounselSubContent>> get();
}
