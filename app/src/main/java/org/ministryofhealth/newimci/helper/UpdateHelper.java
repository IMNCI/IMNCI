package org.ministryofhealth.newimci.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;
import org.ministryofhealth.newimci.model.AilmentFollowUp;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.AssessmentClassification;
import org.ministryofhealth.newimci.model.Category;
import org.ministryofhealth.newimci.model.CounselSubContent;
import org.ministryofhealth.newimci.model.CounselTitle;
import org.ministryofhealth.newimci.model.DiseaseClassification;
import org.ministryofhealth.newimci.model.Gallery;
import org.ministryofhealth.newimci.model.GalleryAilment;
import org.ministryofhealth.newimci.model.GalleryItem;
import org.ministryofhealth.newimci.model.Glossary;
import org.ministryofhealth.newimci.model.HIVCare;
import org.ministryofhealth.newimci.model.TreatAilment;
import org.ministryofhealth.newimci.model.TreatAilmentTreatment;
import org.ministryofhealth.newimci.model.TreatTitle;
import org.ministryofhealth.newimci.server.Service.AgeGroupService;
import org.ministryofhealth.newimci.server.Service.AilmentFollowUpService;
import org.ministryofhealth.newimci.server.Service.AilmentsService;
import org.ministryofhealth.newimci.server.Service.AssessmentClassificationService;
import org.ministryofhealth.newimci.server.Service.AssessmentService;
import org.ministryofhealth.newimci.server.Service.CategoryService;
import org.ministryofhealth.newimci.server.Service.CounselSubContentService;
import org.ministryofhealth.newimci.server.Service.CounselTitlesService;
import org.ministryofhealth.newimci.server.Service.DiseaseClassificationService;
import org.ministryofhealth.newimci.server.Service.GalleryAilmentService;
import org.ministryofhealth.newimci.server.Service.GalleryItemService;
import org.ministryofhealth.newimci.server.Service.GalleryService;
import org.ministryofhealth.newimci.server.Service.GlossaryService;
import org.ministryofhealth.newimci.server.Service.HIVCareService;
import org.ministryofhealth.newimci.server.Service.TreatAilmentService;
import org.ministryofhealth.newimci.server.Service.TreatAilmentTreatmentService;
import org.ministryofhealth.newimci.server.Service.TreatTitlesService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by chriz on 1/15/2018.
 */

public class UpdateHelper {
    private static UpdateHelper mInstance;
    private static DatabaseHandler db;
    private static Boolean ui;
    private ProgressDialog progressDialog;
    Context c;

    public static UpdateHelper getInstance(Context context, Boolean ui) {
        if (mInstance == null){
            mInstance = new UpdateHelper(context, ui);
        }
        return mInstance;
    }

    public UpdateHelper(Context context, Boolean ui){
        db = new DatabaseHandler(context);
        this.ui = ui;
        progressDialog = new ProgressDialog(context);
        this.c = context;
    }

    public void updateContent() throws IOException {
        String ui_p = "true";
        if (!this.ui){
            ui_p = "false";
        }
        new UpdateContentTask(this.c).execute(ui_p);
    }

    class UpdateContentTask extends AsyncTask<String, String, Boolean>{
        Exception exception;
        String ui_passed;
        private TaskCompleted mCallback;
        private Context mContext;

        public UpdateContentTask(Context context){
            this.mContext = context;
            mCallback = (TaskCompleted) mContext;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ui_passed = strings[0];
            try {
                Retrofit retrofit = RetrofitHelper.getInstance().createHelper();

                AilmentsService client = retrofit.create(AilmentsService.class);
                AgeGroupService ageClient = retrofit.create(AgeGroupService.class);
                AilmentFollowUpService ailmentFollowUpClient = retrofit.create(AilmentFollowUpService.class);
                CategoryService categoryClient = retrofit.create(CategoryService.class);
                AssessmentService assessmentClient = retrofit.create(AssessmentService.class);
                DiseaseClassificationService diseaseClassificationClient = retrofit.create(DiseaseClassificationService.class);
                AssessmentClassificationService assessmentClassificationClient = retrofit.create(AssessmentClassificationService.class);
                GlossaryService glossaryClient = retrofit.create(GlossaryService.class);
                TreatTitlesService treatTitlesClient = retrofit.create(TreatTitlesService.class);
                TreatAilmentService treatAilmentClient = retrofit.create(TreatAilmentService.class);
                TreatAilmentTreatmentService treatAilmentTreatmentClient = retrofit.create(TreatAilmentTreatmentService.class);
                CounselTitlesService counselTitlesClient = retrofit.create(CounselTitlesService.class);
                CounselSubContentService counselSubContentClient = retrofit.create(CounselSubContentService.class);
                HIVCareService hivCareClient = retrofit.create(HIVCareService.class);
                GalleryService galleryClient = retrofit.create(GalleryService.class);
                GalleryAilmentService galleryAilmentClient = retrofit.create(GalleryAilmentService.class);
                GalleryItemService galleryItemClient = retrofit.create(GalleryItemService.class);

                Call<List<AgeGroup>> ageCall = ageClient.getAgeGroups();
                Call<List<Ailment>> ailmentsCall = client.getAilments();
                Call<List<AilmentFollowUp>> ailmentFollowUpCall = ailmentFollowUpClient.getAilmentFollowUps();
                Call<List<Category>> categoryCall = categoryClient.getCategories();
                Call<List<Assessment>> assessmentCall = assessmentClient.getAssessments();
                Call<List<DiseaseClassification>> diseaseClassificationCall = diseaseClassificationClient.get();
                Call<List<AssessmentClassification>> assessmentClassificationCall = assessmentClassificationClient.getClassifications();
                Call<List<Glossary>> glossaryCall = glossaryClient.getGlossary();
                Call<List<TreatTitle>> treatTitlesCall = treatTitlesClient.getTitles();
                Call<List<TreatAilment>> treatAilmentCall = treatAilmentClient.getAilments();
                Call<List<TreatAilmentTreatment>> treatAilmentTreatmentCall = treatAilmentTreatmentClient.get();
                Call<List<CounselTitle>> counselTitlesCall = counselTitlesClient.getTitles();
                Call<List<CounselSubContent>> counselSubContentCall = counselSubContentClient.get();
                Call<List<HIVCare>> hivListCall = hivCareClient.get();
                Call<List<Gallery>> galleryCall = galleryClient.getGallery();
                Call<List<GalleryItem>> galleryItemCall = galleryItemClient.getItems();
                Call<List<GalleryAilment>> galleryAilmentCall = galleryAilmentClient.getGalleryAilment();

                publishProgress("Getting Age Groups");
                List<AgeGroup> ageGroups = ageCall.execute().body();
                if (ageGroups.size() > 0) {
                    db.clearTable(db.TABLE_AGE_GROUPS);
                    db.addAgeGroups(ageGroups);
                }

                publishProgress("Getting Ailments");
                List<Ailment> ailmentList = ailmentsCall.execute().body();
                if (ailmentList.size() > 0) {
                    db.clearTable(db.TABLE_AILMENTS);
                    db.addAilments(ailmentList);
                }

                publishProgress("Getting Follow Up Ailments");
                List<AilmentFollowUp> ailmentFollowUpList = ailmentFollowUpCall.execute().body();
                if (ailmentFollowUpList.size() > 0) {
                    db.clearTable(db.TABLE_AILMENT_FOLLOWUP);
                    db.addAilmentFollowUps(ailmentFollowUpList);
                }

                publishProgress("Getting Categories");
                List<Category> categoryList = categoryCall.execute().body();
                if (categoryList.size() > 0){
                    db.clearTable(db.TABLE_CATEGORIES);
                    db.addCategories(categoryList);
                }

                publishProgress("Getting Assessments");
                List<Assessment> assessmentList = assessmentCall.execute().body();
                if (assessmentList.size() > 0){
                    db.clearTable(db.TABLE_ASSESSMENT);
                    db.addAssessments(assessmentList);
                }

                publishProgress("Getting disease classifications");
                List<DiseaseClassification> diseaseClassificationList = diseaseClassificationCall.execute().body();
                if (diseaseClassificationList.size() > 0){
                    db.clearTable(db.TABLE_DISEASE_CLASSIFICATIONS);
                    db.addDiseaseClassifications(diseaseClassificationList);
                }

                publishProgress("Getting Assessment Classifications");
                List<AssessmentClassification> assessmentClassificationList = assessmentClassificationCall.execute().body();
                if (assessmentClassificationList.size() > 0){
                    db.clearTable(db.TABLE_ASSESSMENT_CLASSIFICATIONS);
                    db.addAssessmentClassifications(assessmentClassificationList);
                }

                publishProgress("Getting Glossary");
                List<Glossary> glossaryList = glossaryCall.execute().body();
                if (glossaryList.size() > 0){
                    db.clearTable(db.TABLE_GLOSSARY);
                    db.addGlossaryItems(glossaryList);
                }

                publishProgress("Getting Treatment Titles");
                List<TreatTitle> treatTitleList = treatTitlesCall.execute().body();
                if (treatTitleList.size() > 0){
                    db.clearTable(db.TABLE_TREAT_TITLES);
                    db.addTreatmentTitles(treatTitleList);
                }

                publishProgress("Getting Treatment Ailments");
                List<TreatAilment> treatAilmentList = treatAilmentCall.execute().body();
                if (treatAilmentList.size() > 0){
                    db.clearTable(db.TABLE_TREAT_AILMENTS);
                    db.addTreatAilments(treatAilmentList);
                }

                publishProgress("Getting Treatments for Ailments");
                List<TreatAilmentTreatment> treatAilmentTreatmentList = treatAilmentTreatmentCall.execute().body();
                if (treatAilmentTreatmentList.size() > 0){
                    db.clearTable(db.TABLE_TREAT_AILMENT_TREATMENTS);
                    db.addTreatAilmentTreatments(treatAilmentTreatmentList);
                }

                publishProgress("Getting Counsel the Mother Titles");
                List<CounselTitle> counselTitleList = counselTitlesCall.execute().body();
                if (counselTitleList.size() > 0){
                    db.clearTable(db.TABLE_COUNSEL_TITLES);
                    db.addCounselTitles(counselTitleList);
                }

                publishProgress("Getting Counsel the Mother sub content");
                List<CounselSubContent> counselSubContentList = counselSubContentCall.execute().body();
                if (counselSubContentList.size() > 0){
                    db.clearTable(db.TABLE_COUNSEL_SUB_CONTENT);
                    db.addCounselSubContents(counselSubContentList);
                }

                publishProgress("Getting HIV Care for Children");
                List<HIVCare> hivCareList = hivListCall.execute().body();
                if (hivCareList.size() > 0){
                    db.clearTable(db.TABLE_HIV_CARE);
                    db.addHIVCares(hivCareList);
                }

                publishProgress("Getting Gallery");
                List<Gallery> galleryList = galleryCall.execute().body();
                if (galleryList.size() > 0){
                    db.clearTable(db.TABLE_GALLERY);
                    db.addGalleries(galleryList);
                }

                publishProgress("Getting Gallery Item");
                List<GalleryItem> galleryItemList = galleryItemCall.execute().body();
                if (galleryItemList.size() > 0){
                    db.clearTable(db.TABLE_GALLERY_ITEM);
                    db.addGalleryItems(galleryItemList);
                }

                publishProgress("Getting Gallery Ailments");
                List<GalleryAilment> galleryAilmentList = galleryAilmentCall.execute().body();
                if (galleryAilmentList.size() > 0){
                    db.clearTable(db.TABLE_GALLERY_AILMENT);
                    db.addGalleryAilments(galleryAilmentList);
                }

                return true;
            }catch (Exception e) {
                this.exception = e;

                return false;
            } finally {
            }
        }

        @Override
        protected void onPreExecute() {
            if (ui){
                progressDialog.setMessage("Preparing update");
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (ui){
                progressDialog.dismiss();
                if (aBoolean) {
                    Toast.makeText(c, "Successfully updated app content", Toast.LENGTH_SHORT).show();
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(currentTime);
                    SharedPreferences pref = c.getSharedPreferences(c.getString(R.string.updates), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(c.getString(R.string.last_update), formattedDate);
                    editor.commit();
                    mCallback.onTaskComplete(aBoolean);
                }
                else {
                    Toast.makeText(c, "There was an error updating content", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (ui)
                progressDialog.setMessage(values[0]);
        }
    }
}
