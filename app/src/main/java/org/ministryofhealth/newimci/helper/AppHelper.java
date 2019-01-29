package org.ministryofhealth.newimci.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import org.ministryofhealth.newimci.MainPageActivity;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;
import org.ministryofhealth.newimci.provider.UsageContract;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public String formatDate(String date, String currentDateFormat, String desiredDateFormat) throws ParseException {
        String formattedDate = "";

        SimpleDateFormat currentSimpleDateFormat = new SimpleDateFormat(currentDateFormat);
        Date currentDate = currentSimpleDateFormat.parse(date);

        SimpleDateFormat newDateFormat = new SimpleDateFormat(desiredDateFormat);
        formattedDate = newDateFormat.format(currentDate);

        return formattedDate;
    }

    public static void addAppUsage(Context context, String utilization){
        if (context == null){
        }
        SharedPreferences userPref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        int id = userPref.getInt("id", 0);

        Date currTime = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = simpleDateFormat.format(currTime);

        ContentResolver mContentResolver = context.getContentResolver();

        ContentValues value = new ContentValues();
        value.put(UsageContract.Usages.COL_USER_ID, id);
        value.put(UsageContract.Usages.COL_DEVICE_ID, Build.DISPLAY);
        value.put(UsageContract.Usages.COL_TIMESTAMP, timestamp);
        value.put(UsageContract.Usages.COL_TYPE, utilization);

        Uri uri = mContentResolver.insert(UsageContract.Usages.CONTENT_URI, value);
    }
}
