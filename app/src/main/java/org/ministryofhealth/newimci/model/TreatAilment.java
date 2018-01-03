package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 10/31/2017.
 */

public class TreatAilment {
    private int id, treat_titles_id;
    private String ailment, content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTreat_titles_id() {
        return treat_titles_id;
    }

    public void setTreat_titles_id(int treat_titles_id) {
        this.treat_titles_id = treat_titles_id;
    }

    public String getAilment() {
        return ailment;
    }

    public void setAilment(String ailment) {
        this.ailment = ailment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
