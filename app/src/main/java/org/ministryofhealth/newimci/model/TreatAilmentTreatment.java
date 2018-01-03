package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 10/31/2017.
 */

public class TreatAilmentTreatment {
    private int id, ailment_id;
    private String treatment, content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAilment_id() {
        return ailment_id;
    }

    public void setAilment_id(int ailment_id) {
        this.ailment_id = ailment_id;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
