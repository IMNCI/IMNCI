package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 10/21/2017.
 */

public class Glossary {
    private int id;
    private String acronym, description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
