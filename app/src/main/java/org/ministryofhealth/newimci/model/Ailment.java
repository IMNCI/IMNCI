package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 9/13/2017.
 */

public class Ailment {
    private int id, age_group_id;
    private String ailment, description, created_at, updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAilment() {
        return ailment;
    }

    public void setAilment(String ailment) {
        this.ailment = ailment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public int getAge_group_id() {
        return age_group_id;
    }

    public void setAge_group_id(int age_group_id) {
        this.age_group_id = age_group_id;
    }
}
