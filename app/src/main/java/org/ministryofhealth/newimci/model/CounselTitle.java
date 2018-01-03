package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 11/10/2017.
 */

public class CounselTitle {
    private int id, age_group_id, is_parent;
    private String title, content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge_group_id() {
        return age_group_id;
    }

    public void setAge_group_id(int age_group_id) {
        this.age_group_id = age_group_id;
    }

    public int getIs_parent() {
        return is_parent;
    }

    public void setIs_parent(int is_parent) {
        this.is_parent = is_parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
