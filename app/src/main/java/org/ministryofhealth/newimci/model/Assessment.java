package org.ministryofhealth.newimci.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chriz on 9/29/2017.
 */

public class Assessment implements Parcelable {
    private int id, age_group_id, category_id;
    private String title, assessment;

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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
