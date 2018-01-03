package org.ministryofhealth.newimci.server.Service;

import org.ministryofhealth.newimci.model.GalleryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chriz on 12/5/2017.
 */

public interface GalleryItemService {
    @GET("/api/galleryitems")
    Call<List<GalleryItem>> getItems();
}
