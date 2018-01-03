package org.ministryofhealth.newimci.server.Service;


import org.ministryofhealth.newimci.model.GalleryAilment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 12/5/2017.
 */

public interface GalleryAilmentService {
    @GET("/api/gallery-ailments")
    Call<List<GalleryAilment>> getGalleryAilment();
}
