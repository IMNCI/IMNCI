package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 9/29/2017.
 */

public class AssessmentClassificationSign {
    private int id, classification_id;
    private String sign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassification_id() {
        return classification_id;
    }

    public void setClassification_id(int classification_id) {
        this.classification_id = classification_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
