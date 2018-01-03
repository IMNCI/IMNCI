package org.ministryofhealth.newimci.service;

import android.os.Build;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jaredrummler.android.device.DeviceName;

import org.ministryofhealth.newimci.helper.AppHelper;
import org.ministryofhealth.newimci.model.AppUser;
import org.ministryofhealth.newimci.server.AppUserClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chriz on 9/12/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        try {
            final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String formatted_date = format.format(cal.getTime());

            DeviceName.with(getApplicationContext()).request(new DeviceName.Callback() {
                @Override
                public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                    AppUser appUser = new AppUser();

                    appUser.setPhone_id(refreshedToken);
                    appUser.setOpened_at(formatted_date);
                    appUser.setBrand(Build.BRAND);
                    appUser.setDevice(Build.DEVICE);
                    appUser.setModel(Build.MODEL);
                    appUser.setAndroid_release(Build.VERSION.RELEASE);
                    appUser.setDisplay_no(Build.DISPLAY);
                    appUser.setAndroid_version(AppHelper.getAndroidVersion());
                    appUser.setAndroid_sdk(String.valueOf(Build.VERSION.SDK_INT));
                    appUser.setDevice_model(info.marketName);

                    AppUserClient.getInstance(getApplicationContext()).send(appUser);
                }
            });
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
