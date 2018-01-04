package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;
import org.ministryofhealth.newimci.model.AilmentFollowUp;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.AssessmentClassification;
import org.ministryofhealth.newimci.model.Category;
import org.ministryofhealth.newimci.model.CounselSubContent;
import org.ministryofhealth.newimci.model.CounselTitle;
import org.ministryofhealth.newimci.model.County;
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
import org.ministryofhealth.newimci.server.Service.CountyService;
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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean page = preference.getBoolean("elements_page", true);
        Boolean setup_page = preference.getBoolean("setup_page", true);
        db = new DatabaseHandler(this);
//        db.initDB();
        List<Ailment> ailmentList = db.getAilments();
        if (ailmentList.size() == 0){
            try {
                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(currentTime);

                Context context = SplashActivity.this;
                SharedPreferences pref = context.getSharedPreferences(getString(R.string.updates), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(getString(R.string.last_update), formattedDate);
                editor.commit();
                setupdata();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

//        if(!setup_page) {
            if (page) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainPageActivity.class));
            }
//        }else{
//            startActivity(new Intent(SplashActivity.this, SetupActivity.class));
//        }
        finish();
    }

    private void setupdata() throws Exception {
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
        CountyService countyClient = retrofit.create(CountyService.class);
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
        Call<List<County>> countyCall = countyClient.get();
        Call<List<HIVCare>> hivListCall = hivCareClient.get();
        Call<List<Gallery>> galleryCall = galleryClient.getGallery();
        Call<List<GalleryItem>> galleryItemCall = galleryItemClient.getItems();
        Call<List<GalleryAilment>> galleryAilmentCall = galleryAilmentClient.getGalleryAilment();


        countyCall.enqueue(new Callback<List<County>>() {
            @Override
            public void onResponse(Call<List<County>> call, Response<List<County>> response) {
                db.addCounties(response.body());
            }

            @Override
            public void onFailure(Call<List<County>> call, Throwable t) {
                Log.e("CountyError", t.getMessage());
            }
        });

        ageCall.enqueue(new Callback<List<AgeGroup>>() {
            @Override
            public void onResponse(Call<List<AgeGroup>> call, Response<List<AgeGroup>> response) {
                db.addAgeGroups(response.body());
            }

            @Override
            public void onFailure(Call<List<AgeGroup>> call, Throwable t) {
                Log.e("Age Groups Error::", t.getMessage());
            }
        });
        ailmentsCall.enqueue(new Callback<List<Ailment>>() {
            @Override
            public void onResponse(Call<List<Ailment>> call, Response<List<Ailment>> response) {
                db.addAilments(response.body());
            }

            @Override
            public void onFailure(Call<List<Ailment>> call, Throwable t) {
                Log.e("AilmentsError::", t.getMessage());
            }
        });
        ailmentFollowUpCall.enqueue(new Callback<List<AilmentFollowUp>>() {
            @Override
            public void onResponse(Call<List<AilmentFollowUp>> call, Response<List<AilmentFollowUp>> response) {
                Log.d("AilmentFollowUp", response.raw().toString());
                db.addAilmentFollowUps(response.body());
            }

            @Override
            public void onFailure(Call<List<AilmentFollowUp>> call, Throwable t) {
                Log.e("AilmentsFollowUpError::", t.getMessage());
            }
        });
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                db.addCategories(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddCategoryFailure", t.getMessage());
            }
        });

        assessmentCall.enqueue(new Callback<List<Assessment>>() {
            @Override
            public void onResponse(Call<List<Assessment>> call, Response<List<Assessment>> response) {
                db.addAssessments(response.body());
            }

            @Override
            public void onFailure(Call<List<Assessment>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddAssessmentFailure", t.getMessage());
            }
        });

        diseaseClassificationCall.enqueue(new Callback<List<DiseaseClassification>>() {
            @Override
            public void onResponse(Call<List<DiseaseClassification>> call, Response<List<DiseaseClassification>> response) {
                db.addDiseaseClassifications(response.body());
            }

            @Override
            public void onFailure(Call<List<DiseaseClassification>> call, Throwable t) {
                Log.e("AddDClassification", t.getMessage());
            }
        });

        assessmentClassificationCall.enqueue(new Callback<List<AssessmentClassification>>() {
            @Override
            public void onResponse(Call<List<AssessmentClassification>> call, Response<List<AssessmentClassification>> response) {
                db.addAssessmentClassifications(response.body());
            }

            @Override
            public void onFailure(Call<List<AssessmentClassification>> call, Throwable t) {
                Log.e("AddAssClassification", t.getMessage());
            }
        });

        glossaryCall.enqueue(new Callback<List<Glossary>>() {
            @Override
            public void onResponse(Call<List<Glossary>> call, Response<List<Glossary>> response) {
                db.addGlossaryItems(response.body());
            }

            @Override
            public void onFailure(Call<List<Glossary>> call, Throwable t) {
                Log.e("addGlossary", t.getMessage());
            }
        });

        treatTitlesCall.enqueue(new Callback<List<TreatTitle>>() {
            @Override
            public void onResponse(Call<List<TreatTitle>> call, Response<List<TreatTitle>> response) {
                db.addTreatmentTitles(response.body());
            }

            @Override
            public void onFailure(Call<List<TreatTitle>> call, Throwable t) {
                Log.e("TreatTitles", t.getMessage());
            }
        });

        treatAilmentCall.enqueue(new Callback<List<TreatAilment>>() {
            @Override
            public void onResponse(Call<List<TreatAilment>> call, Response<List<TreatAilment>> response) {
                db.addTreatAilments(response.body());
            }

            @Override
            public void onFailure(Call<List<TreatAilment>> call, Throwable t) {
                Log.e("TreatAilments", t.getMessage());
            }
        });

        treatAilmentTreatmentCall.enqueue(new Callback<List<TreatAilmentTreatment>>() {
            @Override
            public void onResponse(Call<List<TreatAilmentTreatment>> call, Response<List<TreatAilmentTreatment>> response) {
                db.addTreatAilmentTreatments(response.body());
            }

            @Override
            public void onFailure(Call<List<TreatAilmentTreatment>> call, Throwable t) {
                Log.e("TreatAilmentTreatments", t.getMessage());
            }
        });

        counselTitlesCall.enqueue(new Callback<List<CounselTitle>>() {
            @Override
            public void onResponse(Call<List<CounselTitle>> call, Response<List<CounselTitle>> response) {
                db.addCounselTitles(response.body());
            }

            @Override
            public void onFailure(Call<List<CounselTitle>> call, Throwable t) {
                Log.e("CounselTitles", t.getMessage());
            }
        });

        counselSubContentCall.enqueue(new Callback<List<CounselSubContent>>() {
            @Override
            public void onResponse(Call<List<CounselSubContent>> call, Response<List<CounselSubContent>> response) {
                db.addCounselSubContents(response.body());
            }

            @Override
            public void onFailure(Call<List<CounselSubContent>> call, Throwable t) {
                Log.e("CounselSubContent", t.getMessage());
            }
        });

        hivListCall.enqueue(new Callback<List<HIVCare>>() {
            @Override
            public void onResponse(Call<List<HIVCare>> call, Response<List<HIVCare>> response) {
                db.addHIVCares(response.body());
            }

            @Override
            public void onFailure(Call<List<HIVCare>> call, Throwable t) {
                Log.e("HIVCare", t.getMessage());
            }
        });

        galleryCall.enqueue(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, Response<List<Gallery>> response) {
                db.addGalleries(response.body());
            }

            @Override
            public void onFailure(Call<List<Gallery>> call, Throwable t) {
                Log.e("Gallery", t.getMessage());
            }
        });

        galleryAilmentCall.enqueue(new Callback<List<GalleryAilment>>() {
            @Override
            public void onResponse(Call<List<GalleryAilment>> call, Response<List<GalleryAilment>> response) {
                db.addGalleryAilments(response.body());
            }

            @Override
            public void onFailure(Call<List<GalleryAilment>> call, Throwable t) {
                Log.e("GalleryAilment", t.getMessage());
            }
        });

        galleryItemCall.enqueue(new Callback<List<GalleryItem>>() {
            @Override
            public void onResponse(Call<List<GalleryItem>> call, Response<List<GalleryItem>> response) {
                db.addGalleryItems(response.body());
            }

            @Override
            public void onFailure(Call<List<GalleryItem>> call, Throwable t) {
                Log.e("GalleryItem", t.getMessage());
            }
        });


    }
}
