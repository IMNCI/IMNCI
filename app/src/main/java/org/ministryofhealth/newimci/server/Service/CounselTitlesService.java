package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.CounselTitle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 11/14/2017.
 */

public interface CounselTitlesService {
    @GET("api/counsel-titles")
    Call<List<CounselTitle>> getTitles();
}
