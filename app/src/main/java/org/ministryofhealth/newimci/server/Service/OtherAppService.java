package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.App;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by CHRIS on 28/03/2018.
 */

public interface OtherAppService {
    @GET("/all-except/{package}")
    Call<List<App>> get(@Path("package") String pack);
}
