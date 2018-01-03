package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 9/29/2017.
 */

public class AssessmentClassification {
    private int id, assessment_id, disease_classification_id;
    private String classification, parent, signs, treatments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public int getDisease_classification_id() {
        return disease_classification_id;
    }

    public void setDisease_classification_id(int disease_classification_id) {
        this.disease_classification_id = disease_classification_id;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSigns() {
        return signs;
    }

    public void setSigns(String signs) {
        this.signs = signs;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }
}
