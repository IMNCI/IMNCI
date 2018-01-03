package org.ministryofhealth.newimci.model;

/**
 * Created by chriz on 9/12/2017.
 */

public class AppUser {

    private int id;
    private String phone_id, opened_at, created_at, updated_at, brand, device, model, display_no, android_version, android_sdk, android_release, device_model;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getOpened_at() {
        return opened_at;
    }

    public void setOpened_at(String opened_at) {
        this.opened_at = opened_at;
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

    public String getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(String phone_id) {
        this.phone_id = phone_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAndroid_release() {
        return android_release;
    }

    public void setAndroid_release(String android_release) {
        this.android_release = android_release;
    }

    public String getDisplay_no() {
        return display_no;
    }

    public void setDisplay_no(String display_no) {
        this.display_no = display_no;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_sdk() {
        return android_sdk;
    }

    public void setAndroid_sdk(String android_sdk) {
        this.android_sdk = android_sdk;
    }

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }
}
