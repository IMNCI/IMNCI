package org.ministryofhealth.newimci.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chriz on 9/10/2017.
 */

public class Menu{
    private int _image_resource;
    private String _menu_title, _menu_slug, _menu_color;
    private boolean _alert;

    public int get_image_resource() {
        return _image_resource;
    }

    public void set_image_resource(int _image_resource) {
        this._image_resource = _image_resource;
    }

    public String get_menu_title() {
        return _menu_title;
    }

    public void set_menu_title(String _menu_title) {
        this._menu_title = _menu_title;
    }

    public String get_menu_slug() {
        return _menu_slug;
    }

    public void set_menu_slug(String _menu_slug) {
        this._menu_slug = _menu_slug;
    }

    public String get_menu_color() {
        return _menu_color;
    }

    public void set_menu_color(String _menu_color) {
        this._menu_color = _menu_color;
    }

    public boolean is_alert() {
        return _alert;
    }

    public void set_alert(boolean _alert) {
        this._alert = _alert;
    }
}
