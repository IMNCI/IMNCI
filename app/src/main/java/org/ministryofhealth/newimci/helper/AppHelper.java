package org.ministryofhealth.newimci.helper;

import android.os.Build;

import java.lang.reflect.Field;

/**
 * Created by chriz on 10/17/2017.
 */

public class AppHelper {
    public static final String getAndroidVersion(){
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                return fieldName;
            }
        }
        return null;
    }
}
