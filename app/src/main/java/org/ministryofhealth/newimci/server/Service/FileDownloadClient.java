package org.ministryofhealth.newimci.server.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by chriz on 12/10/2017.
 */

public interface FileDownloadClient {
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
