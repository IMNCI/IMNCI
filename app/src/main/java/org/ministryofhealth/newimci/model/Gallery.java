package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 12/5/2017.
 */

public class Gallery {
    private int id, gallery_ailments_id, gallery_items_id;
    private String title;
    private String description;
    private String thumbnail;
    private String link;
    private String size;
    private String mime;
    private String type;
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGallery_ailments_id() {
        return gallery_ailments_id;
    }

    public void setGallery_ailments_id(int gallery_ailments_id) {
        this.gallery_ailments_id = gallery_ailments_id;
    }

    public int getGallery_items_id() {
        return gallery_items_id;
    }

    public void setGallery_items_id(int gallery_items_id) {
        this.gallery_items_id = gallery_items_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
