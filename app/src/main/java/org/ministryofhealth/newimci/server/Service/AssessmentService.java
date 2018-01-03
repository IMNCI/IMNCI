package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Assessment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 9/29/2017.
 */

public interface AssessmentService {
    @GET("/api/assessment")
    Call<List<Assessment>> getAssessments();
}
