package org.ministryofhealth.newimci.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chriz on 9/26/2017.
 */

public class AilmentFollowUp implements Parcelable {
    private int id, ailment_id;
    private String advice, treatment;

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

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
