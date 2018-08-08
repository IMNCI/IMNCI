package org.ministryofhealth.newimci.model;

public class KeyElements {
    private int id;
    private String elements, created_at, updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements;
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
}
