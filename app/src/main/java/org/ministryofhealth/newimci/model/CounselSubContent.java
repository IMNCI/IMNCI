package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 11/14/2017.
 */

public class CounselSubContent {
    private int id, counsel_titles_id;
    private String sub_content_title, content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCounsel_titles_id() {
        return counsel_titles_id;
    }

    public void setCounsel_titles_id(int counsel_titles_id) {
        this.counsel_titles_id = counsel_titles_id;
    }

    public String getSub_content_title() {
        return sub_content_title;
    }

    public void setSub_content_title(String sub_content_title) {
        this.sub_content_title = sub_content_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
