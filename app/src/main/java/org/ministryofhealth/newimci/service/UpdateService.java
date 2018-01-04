package org.ministryofhealth.newimci.service;


import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.ministryofhealth.newimci.server.Service.UpdateContentService;

/**
 * Created by chriz on 1/3/2018.
 */
public class UpdateService extends JobService{
    private static final String TAG = UpdateContentService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters job) {
        System.out.println("Job Started===");
        new Thread(new Runnable() {
            @Override
            public void run() {
                runUpdate(job);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    public void runUpdate(final JobParameters parameters){
        try {

            Log.d(TAG, "completeJob: " + "jobStarted");
            Thread.sleep(2000);
            Log.d(TAG, "completeJob: " + "jobFinished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            jobFinished(parameters, true);
        }
    }
}
