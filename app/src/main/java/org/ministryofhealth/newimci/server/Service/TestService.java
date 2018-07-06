package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.Question;
import org.ministryofhealth.newimci.model.QuestionChoice;
import org.ministryofhealth.newimci.model.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestService {
    @GET("api/tests/get")
    Call<List<Test>> getTests();

    @GET("api/tests/questions/get")
    Call<List<Question>> getQuestions();

    @GET("api/tests/questions/choices/get")
    Call<List<QuestionChoice>> getQuestionChoices();
}
