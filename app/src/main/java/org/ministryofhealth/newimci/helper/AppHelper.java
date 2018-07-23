package org.ministryofhealth.newimci.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import org.ministryofhealth.newimci.MainPageActivity;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;

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

    public boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void exitTest(final Activity context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quit Test?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("You will lose your progress. Continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(context, TestActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
