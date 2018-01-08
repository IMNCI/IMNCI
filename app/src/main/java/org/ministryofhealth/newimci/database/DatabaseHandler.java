package org.ministryofhealth.newimci.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.measurement.AppMeasurement;

import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;
import org.ministryofhealth.newimci.model.AilmentFollowUp;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.AssessmentClassification;
import org.ministryofhealth.newimci.model.AssessmentClassificationSign;
import org.ministryofhealth.newimci.model.AssessmentClassificationTreatment;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chriz on 9/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NAME = "imci_mobile_app";

    private static final String TABLE_AILMENTS = "ailments";
    private static final String TABLE_AGE_GROUPS = "age_groups";
    private static final String TABLE_AILMENT_FOLLOWUP = "ailment_followup";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_ASSESSMENT = "assessments";
    private static final String TABLE_DISEASE_CLASSIFICATIONS = "disease_classifications";
    private static final String TABLE_ASSESSMENT_CLASSIFICATIONS = "assessment_classifications";
    private static final String TABLE_ASSESSMENT_CLASSIFICATION_SIGNS = "assessment_classification_signs";
    private static final String TABLE_ASSESSMENT_CLASSIFICATION_TREATMENTS = "assessment_classification_treatments";
    private static final String TABLE_GLOSSARY = "glossary";
    private static final String TABLE_TREAT_TITLES = "treat_titles";
    private static final String TABLE_TREAT_AILMENTS = "treat_ailments";
    private static final String TABLE_TREAT_AILMENT_TREATMENTS = "treat_ailment_treatments";
    public static final String TABLE_COUNSEL_TITLES = "counsel_titles";
    public static final String TABLE_COUNSEL_SUB_CONTENT = "counsel_sub_contents";
    public static final String TABLE_COUNTY = "county";
    public static final String TABLE_HIV_CARE = "hiv_care";
    public static final String TABLE_GALLERY = "gallery";
    public static final String TABLE_GALLERY_ITEM = "gallery_item";
    public static final String TABLE_GALLERY_AILMENT = "gallery_ailment";

    private static final String KEY_ID = "id";
    private static final String KEY_AILMENT = "ailment";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_AGE_GROUP_ID = "age_group_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private static final String KEY_AGE_GROUP = "age_group";

    private static final String KEY_ADVICE = "advice";
    private static final String KEY_TREATMENT = "treatment";
    private static final String KEY_AILMENT_ID = "ailment_id";

    private static final String KEY_CATEGORY = "category";

    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ASSESSMENT = "assessment";

    private static final String KEY_CLASSIFICATION = "classification";
    private static final String KEY_COLOR = "color";

    private static final String KEY_ASSESSMENT_ID = "assessment_id";
    private static final String KEY_DISEASE_CLASSIFICATION_ID = "disease_classification_id";
    private static final String KEY_PARENT = "parent";

    private static final String KEY_CLASSIFICATION_ID = "classification_id";
    private static final String KEY_SIGN = "sign";

    private static final String KEY_ACRONYM = "acronym";

    private static final String KEY_GUIDE = "guide";

    private static final String KEY_TITLE_ID = "title_id";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_IS_PARENT = "is_parent";

    public static final String KEY_SUB_CONTENT_TITLE = "sub_content_title";

    public static final String KEY_COUNTY = "county";

    public static final String KEY_THUMBNAIL = "thumbnail";

    public static final String KEY_ITEM  = "item";

    public static final String KEY_GALLERY_AILMENT_ID = "gallery_ailment_id";
    public static final String KEY_GALLERY_ITEM_ID = "gallery_item_id";
    public static final String KEY_LINK = "link";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SIZE = "size";
    public static final String KEY_MIME = "mime";


    SQLiteDatabase writableDB;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.writableDB = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_AILMENTS_TABLE = "CREATE TABLE " + TABLE_AILMENTS + "("
                + KEY_ID + " INTEGER,"
                + KEY_AILMENT + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_AGE_GROUP_ID + " INTEGER,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_UPDATED_AT + " TEXT"
                + ")";

        String CREATE_AGE_GROUPS_TABLE = "CREATE TABLE " + TABLE_AGE_GROUPS + "("
                + KEY_ID + " INTEGER,"
                + KEY_AGE_GROUP + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_UPDATED_AT + " TEXT"
                + ")";

        String CREATE_AILMENT_FOLLOWUP_TABLE = "CREATE TABLE " + TABLE_AILMENT_FOLLOWUP + "("
                + KEY_ID + " INTEGER,"
                + KEY_AILMENT_ID + " INTEGER,"
                + KEY_ADVICE + " TEXT,"
                + KEY_TREATMENT + " TEXT"
                + ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_ID + " INTEGER,"
                + KEY_CATEGORY + " TEXT"
                + ")";

        String CREATE_ASSESSMENTS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENT + "("
                + KEY_ID + " INTEGER,"
                + KEY_AGE_GROUP_ID + " INTEGER,"
                + KEY_CATEGORY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_ASSESSMENT + " TEXT" +
                ")";

        String CREATE_DISEASE_CLASSIFICATION_TABLE = "CREATE TABLE " + TABLE_DISEASE_CLASSIFICATIONS + "("
                + KEY_ID + " INTEGER,"
                + KEY_CLASSIFICATION + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_COLOR + " TEXT"
                + ");";

        String CREATE_ASSESSMENT_CLASSIFICATIONS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENT_CLASSIFICATIONS + "("
                + KEY_ID + " INTEGER,"
                + KEY_ASSESSMENT_ID + " INTEGER,"
                + KEY_DISEASE_CLASSIFICATION_ID + " INTEGER,"
                + KEY_CLASSIFICATION + " TEXT,"
                + KEY_PARENT + " TEXT,"
                + KEY_SIGN + " TEXT,"
                + KEY_TREATMENT + " TEXT"
                + ");";

        String CREATE_GLOSSARY_TABLE = "CREATE TABLE " + TABLE_GLOSSARY + "("
                + KEY_ID + " INTEGER,"
                + KEY_ACRONYM + " TEXT,"
                + KEY_DESCRIPTION + " TEXT"
                + ");";

        String CREATE_TREAT_TITLES_TABLE = "CREATE TABLE " + TABLE_TREAT_TITLES + "("
                + KEY_ID + " INTEGER,"
                + KEY_AGE_GROUP_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_GUIDE + " TEXT"
                + ");";

        String CREATE_TREAT_AILMENTS_TABLE = "CREATE TABLE " + TABLE_TREAT_AILMENTS + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE_ID + " INTEGER,"
                + KEY_AILMENT + " TEXT,"
                + KEY_CONTENT + " TEXT"
                + ");";

        String CREATE_TREAT_AILMENT_TREATMENTS_TABLE = "CREATE TABLE " + TABLE_TREAT_AILMENT_TREATMENTS + "("
                + KEY_ID + " INTEGER,"
                + KEY_AILMENT_ID + " INTEGER,"
                + KEY_TREATMENT + " TEXT,"
                + KEY_CONTENT + " TEXT"
                + ");";

        String CREATE_COUNSEL_TITLES_TABLE = "CREATE TABLE " + TABLE_COUNSEL_TITLES + "("
                + KEY_ID + " INTEGER,"
                + KEY_IS_PARENT + " INTEGER,"
                + KEY_AGE_GROUP_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT"
                + ");";

        String CREATE_COUNTY_TABLE = "CREATE TABLE " + TABLE_COUNTY + "("
                + KEY_ID + " INTEGER,"
                + KEY_COUNTY + " TEXT"
                + ");";

        String CREATE_COUNSEL_SUB_CONTENT_TABLE = "CREATE TABLE " + TABLE_COUNSEL_SUB_CONTENT + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE_ID + " INTEGER,"
                + KEY_SUB_CONTENT_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT"
                + ");";

        String CREATE_HIV_CARE_TABLE = "CREATE TABLE " + TABLE_HIV_CARE + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_THUMBNAIL + " TEXT"
                + ");";

        String CREATE_GALLERY_TABLE = "CREATE TABLE " + TABLE_GALLERY + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_GALLERY_AILMENT_ID + " INTEGER,"
                + KEY_GALLERY_ITEM_ID + " INTEGER,"
                + KEY_THUMBNAIL + " TEXT,"
                + KEY_LINK + " TEXT,"
                + KEY_SIZE + " TEXT,"
                + KEY_MIME + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_UPDATED_AT + " TEXT"
                + ");";

        String CREATE_GALLERY_ITEMS_TABLE = "CREATE TABLE " + TABLE_GALLERY_ITEM + "("
                + KEY_ID + " INTEGER,"
                + KEY_ITEM + " TEXT"
                + ");";

        String CREATE_GALLERY_AILMENTS_TABLE = "CREATE TABLE " + TABLE_GALLERY_AILMENT + "("
                + KEY_ID + " INTEGER,"
                + KEY_AILMENT + " TEXT"
                + ");";
//        String CREATE_ASSESSMENT_CLASSIFICATION_SIGNS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENT_CLASSIFICATION_SIGNS + "("
//                + KEY_ID + " INTEGER,"
//                + KEY_CLASSIFICATION_ID + " INTEGER,"
//                + KEY_SIGN + " TEXT"
//                + ");";
//
//        String CREATE_ASSESSMENT_CLASSIFICATION_TREATMENTS_TABLE = "CREATE TABLE " + TABLE_ASSESSMENT_CLASSIFICATION_TREATMENTS + "("
//                + KEY_ID + " INTEGER,"
//                + KEY_CLASSIFICATION_ID + " INTEGER,"
//                + KEY_TREATMENT + " TEXT"
//                + ");";

        db.execSQL(CREATE_AGE_GROUPS_TABLE);
        db.execSQL(CREATE_AILMENTS_TABLE);
        db.execSQL(CREATE_AILMENT_FOLLOWUP_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_ASSESSMENTS_TABLE);
        db.execSQL(CREATE_DISEASE_CLASSIFICATION_TABLE);
        db.execSQL(CREATE_ASSESSMENT_CLASSIFICATIONS_TABLE);
        db.execSQL(CREATE_GLOSSARY_TABLE);
        db.execSQL(CREATE_TREAT_TITLES_TABLE);
        db.execSQL(CREATE_TREAT_AILMENTS_TABLE);
        db.execSQL(CREATE_TREAT_AILMENT_TREATMENTS_TABLE);
        db.execSQL(CREATE_COUNSEL_TITLES_TABLE);
        db.execSQL(CREATE_COUNSEL_SUB_CONTENT_TABLE);
        db.execSQL(CREATE_COUNTY_TABLE);
        db.execSQL(CREATE_HIV_CARE_TABLE);
        db.execSQL(CREATE_GALLERY_TABLE);
        db.execSQL(CREATE_GALLERY_ITEMS_TABLE);
        db.execSQL(CREATE_GALLERY_AILMENTS_TABLE);
//        db.execSQL(CREATE_ASSESSMENT_CLASSIFICATION_SIGNS_TABLE);
//        db.execSQL(CREATE_ASSESSMENT_CLASSIFICATION_TREATMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AILMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AILMENT_FOLLOWUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASE_CLASSIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_CLASSIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOSSARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_TITLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_AILMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_AILMENT_TREATMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNSEL_TITLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNSEL_SUB_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIV_CARE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY_AILMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY_ITEM);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_CLASSIFICATION_SIGNS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_CLASSIFICATION_TREATMENTS);
        onCreate(db);
    }

    public void initDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AILMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AILMENT_FOLLOWUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISEASE_CLASSIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENT_CLASSIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOSSARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_TITLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_AILMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREAT_AILMENT_TREATMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNSEL_TITLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNSEL_SUB_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIV_CARE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY_AILMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GALLERY_ITEM);

        onCreate(db);
    }

    public List<Ailment> getAilments(){
        List<Ailment> ailmentList = new ArrayList<Ailment>();

        String selectAilmentQuery = "SELECT * FROM " + TABLE_AILMENTS;
        Cursor cursor = writableDB.rawQuery(selectAilmentQuery, null);

        if (cursor.moveToFirst()){
            do {
                Ailment ailment = new Ailment();

                ailment.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                ailment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));
                ailment.setAge_group_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AGE_GROUP_ID))));
                ailment.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                ailment.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                ailment.setUpdated_at(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));

                ailmentList.add(ailment);
            }while (cursor.moveToNext());
        }

        return ailmentList;
    }

    public Ailment getAilment(int id){
        Ailment ailment = new Ailment();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_AILMENTS, null, KEY_ID + " =?", new String[]{String.valueOf(id)}, null, null, null);
        if (cur.moveToFirst()){
            ailment.setId(id);
            ailment.setAilment(cur.getString(cur.getColumnIndex(KEY_AILMENT)));
        }
        return ailment;
    }

    public AgeGroup getAgeGroup(int id){
        AgeGroup group = new AgeGroup();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_AGE_GROUPS, null, KEY_ID + " =?", new String[]{String.valueOf(id)}, null, null, null);
        if (cur.moveToFirst()){
            group.setId(id);
            group.setAge_group(cur.getString(cur.getColumnIndex(KEY_AGE_GROUP)));
        }
        return group;
    }

    public ArrayList<Ailment> getAilments(int age_group_id){
        ArrayList<Ailment> ailmentList = new ArrayList<Ailment>();

        String selectAilmentQuery = "SELECT * FROM " + TABLE_AILMENTS + " WHERE " + KEY_AGE_GROUP_ID + " = " + age_group_id;
        Cursor cursor = writableDB.rawQuery(selectAilmentQuery, null);

        if (cursor.moveToFirst()){
            do {
                Ailment ailment = new Ailment();

                ailment.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                ailment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));
                ailment.setAge_group_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_AGE_GROUP_ID))));
                ailment.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                ailment.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                ailment.setUpdated_at(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));

                ailmentList.add(ailment);
            }while (cursor.moveToNext());
        }

        return ailmentList;
    }

    public void addAilments(List<Ailment> ailmentList){
        for (Ailment ailment:
             ailmentList) {
            addAilment(ailment);
        }
    }

    public void addAilment(Ailment ailment){
        ContentValues value = new ContentValues();

        value.put(KEY_ID, ailment.getId());
        value.put(KEY_AILMENT, ailment.getAilment());
        value.put(KEY_DESCRIPTION, ailment.getDescription());
        value.put(KEY_AGE_GROUP_ID, ailment.getAge_group_id());
        value.put(KEY_CREATED_AT, ailment.getCreated_at());
        value.put(KEY_UPDATED_AT, ailment.getUpdated_at());

        writableDB.insert(TABLE_AILMENTS, null, value);
    }

    public void addAgeGroup(AgeGroup ageGroup){
        ContentValues value = new ContentValues();

        value.put(KEY_ID, ageGroup.getId());
        value.put(KEY_AGE_GROUP, ageGroup.getAge_group());
        value.put(KEY_CREATED_AT, ageGroup.getCreated_at());
        value.put(KEY_UPDATED_AT, ageGroup.getUpdated_at());

        writableDB.insert(TABLE_AGE_GROUPS, null, value);
    }

    public void addAgeGroups(List<AgeGroup> ageGroups){
        for (AgeGroup ageGroup:
                ageGroups) {
            addAgeGroup(ageGroup);
        }
    }

    public List<AgeGroup> getAgeGroups(){
        List<AgeGroup> ageGroupList = new ArrayList<AgeGroup>();

        String selectAgeGroupQuery = "SELECT * FROM " + TABLE_AGE_GROUPS;
        Cursor cursor = writableDB.rawQuery(selectAgeGroupQuery, null);

        if (cursor.moveToFirst()){
            do {
                AgeGroup ageGroup = new AgeGroup();

                ageGroup.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                ageGroup.setAge_group(cursor.getString(cursor.getColumnIndex(KEY_AGE_GROUP)));
                ageGroup.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
                ageGroup.setUpdated_at(cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT)));

                ageGroupList.add(ageGroup);
            }while (cursor.moveToNext());
        }

        return ageGroupList;
    }

    public void addAilmentFollowUp(AilmentFollowUp ailmentFollowUp){
        ContentValues value = new ContentValues();

        value.put(KEY_ID, ailmentFollowUp.getId());
        value.put(KEY_AILMENT_ID, ailmentFollowUp.getAilment_id());
        value.put(KEY_ADVICE, ailmentFollowUp.getAdvice());
        value.put(KEY_TREATMENT, ailmentFollowUp.getTreatment());

        writableDB.insert(TABLE_AILMENT_FOLLOWUP, null, value);
    }

    public void addAilmentFollowUps(List<AilmentFollowUp> ailmentFollowUpList){
        for (AilmentFollowUp ailmentFollowUp:
                ailmentFollowUpList){
            addAilmentFollowUp(ailmentFollowUp);
        }
    }

    public AilmentFollowUp getAilmentFollowUp(int ailment_id){
        AilmentFollowUp ailmentFollowUp = new AilmentFollowUp();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_AILMENT_FOLLOWUP, null, KEY_AILMENT_ID + " =?", new String[]{String.valueOf(ailment_id)}, null, null, null);
        if (cur.moveToFirst()){
            ailmentFollowUp.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
            ailmentFollowUp.setAilment_id(ailment_id);
            ailmentFollowUp.setAdvice(cur.getString(cur.getColumnIndex(KEY_ADVICE)));
            ailmentFollowUp.setTreatment(cur.getString(cur.getColumnIndex(KEY_TREATMENT)));
        }
        return ailmentFollowUp;
    }

    public void addCategory(Category category){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, category.getId());
        values.put(KEY_CATEGORY, category.getCategory());

        writableDB.insert(TABLE_CATEGORIES, null ,values);
    }

    public Category getCategory(int id){
        Category category = new Category();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            category.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
        }
        return category;
    }

    public void addCategories(List<Category> categories){
        for (Category category:
                categories){
            addCategory(category);
        }
    }

    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<Category>();
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Category category = new Category();

                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                category.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));

                categories.add(category);
            }while (cursor.moveToNext());
        }
        return categories;
    }

    public void addAssessment(Assessment assessment){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, assessment.getId());
        values.put(KEY_AGE_GROUP_ID, assessment.getAge_group_id());
        values.put(KEY_CATEGORY_ID, assessment.getCategory_id());
        values.put(KEY_TITLE, assessment.getTitle());
        values.put(KEY_ASSESSMENT, assessment.getAssessment());

        this.writableDB.insert(TABLE_ASSESSMENT, null, values);
    }

    public void addAssessments(List<Assessment> assessments){
        for (Assessment assessment
                : assessments){
            addAssessment(assessment);
        }
    }

    public Assessment getAssessment(int id){
        Assessment assessment = new Assessment();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_ASSESSMENT, null, KEY_ID + " =?", new String[]{String.valueOf(id)}, null, null, null);

        if (cur.moveToFirst()){
            assessment.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
            assessment.setAge_group_id(cur.getInt(cur.getColumnIndex(KEY_AGE_GROUP_ID)));
            assessment.setCategory_id(cur.getInt(cur.getColumnIndex(KEY_CATEGORY_ID)));
            assessment.setTitle(cur.getString(cur.getColumnIndex(KEY_TITLE)));
            assessment.setAssessment(cur.getString(cur.getColumnIndex(KEY_ASSESSMENT)));
        }
        return assessment;
    }

    public List<Assessment> getAssessments(){
        List<Assessment> assessments = new ArrayList<Assessment>();

        String query = "SELECT * FROM " + TABLE_ASSESSMENT;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Assessment assessment = new Assessment();

                assessment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                assessment.setAge_group_id(cursor.getInt(cursor.getColumnIndex(KEY_AGE_GROUP_ID)));
                assessment.setCategory_id(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                assessment.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                assessment.setAssessment(cursor.getString(cursor.getColumnIndex(KEY_ASSESSMENT)));

                assessments.add(assessment);
            }while (cursor.moveToNext());
        }
        return assessments;
    }

    public List<Assessment> getAssessments(int age_group_id, int category_id){
        List<Assessment> assessments = new ArrayList<Assessment>();

        String query = "SELECT * FROM " + TABLE_ASSESSMENT + " WHERE " + KEY_AGE_GROUP_ID + " = " + age_group_id + " AND " + KEY_CATEGORY_ID + " = " + category_id;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                Assessment assessment = new Assessment();

                assessment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                assessment.setAge_group_id(cursor.getInt(cursor.getColumnIndex(KEY_AGE_GROUP_ID)));
                assessment.setCategory_id(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                assessment.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                assessment.setAssessment(cursor.getString(cursor.getColumnIndex(KEY_ASSESSMENT)));

                assessments.add(assessment);
            }while (cursor.moveToNext());
        }
        return assessments;
    }

    public void addDiseaseClassification(DiseaseClassification classification){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, classification.getId());
        values.put(KEY_CLASSIFICATION, classification.getClassification());
        values.put(KEY_DESCRIPTION, classification.getDescription());
        values.put(KEY_COLOR, classification.getColor());

        writableDB.insert(TABLE_DISEASE_CLASSIFICATIONS, null, values);
    }

    public void addDiseaseClassifications(List<DiseaseClassification> classifications){
        for (DiseaseClassification classification
                : classifications){
            addDiseaseClassification(classification);
        }
    }


    public DiseaseClassification getClassification(int id){
        DiseaseClassification diseaseClassification = new DiseaseClassification();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_DISEASE_CLASSIFICATIONS, null, KEY_ID + " =?", new String[]{String.valueOf(id)}, null, null, null);
        if (cur.moveToFirst()){
            diseaseClassification.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
            diseaseClassification.setClassification(cur.getString(cur.getColumnIndex(KEY_CLASSIFICATION)));
            diseaseClassification.setDescription(cur.getString(cur.getColumnIndex(KEY_DESCRIPTION)));
            diseaseClassification.setColor(cur.getString(cur.getColumnIndex(KEY_COLOR)));
        }
        return diseaseClassification;
    }

    public void addAssessmentClassification(AssessmentClassification assessmentClassification){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, assessmentClassification.getId());
        values.put(KEY_DISEASE_CLASSIFICATION_ID, assessmentClassification.getDisease_classification_id());
        values.put(KEY_ASSESSMENT_ID, assessmentClassification.getAssessment_id());
        values.put(KEY_CLASSIFICATION, assessmentClassification.getClassification());
        values.put(KEY_PARENT, assessmentClassification.getParent());
        values.put(KEY_SIGN, assessmentClassification.getSigns());
        values.put(KEY_TREATMENT, assessmentClassification.getTreatments());


        writableDB.insert(TABLE_ASSESSMENT_CLASSIFICATIONS, null, values);
    }

    public void addAssessmentClassifications(List<AssessmentClassification> assessmentClassificationList){
        for (AssessmentClassification assessmentClassification: assessmentClassificationList){
            addAssessmentClassification(assessmentClassification);
        }
    }

    public List<AssessmentClassification> getAssessmentClassifications(){
        List<AssessmentClassification> assessmentClassifications = new ArrayList<AssessmentClassification>();
        String query = "SELECT * FROM " + TABLE_ASSESSMENT_CLASSIFICATIONS;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                AssessmentClassification assessmentClassification = new AssessmentClassification();

                assessmentClassification.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                assessmentClassification.setAssessment_id(cursor.getInt(cursor.getColumnIndex(KEY_ASSESSMENT_ID)));
                assessmentClassification.setDisease_classification_id(cursor.getInt(cursor.getColumnIndex(KEY_DISEASE_CLASSIFICATION_ID)));
                assessmentClassification.setClassification(cursor.getString(cursor.getColumnIndex(KEY_CLASSIFICATION)));
                assessmentClassification.setParent(cursor.getString(cursor.getColumnIndex(KEY_PARENT)));
                assessmentClassification.setSigns(cursor.getString(cursor.getColumnIndex(KEY_SIGN)));
                assessmentClassification.setTreatments(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));

                assessmentClassifications.add(assessmentClassification);
            } while (cursor.moveToNext());
        }
        return assessmentClassifications;
    }

    public List<AssessmentClassification> getAssessmentClassifications(int assessment_id){
        List<AssessmentClassification> assessmentClassifications = new ArrayList<AssessmentClassification>();
        String query = "SELECT * FROM " + TABLE_ASSESSMENT_CLASSIFICATIONS + " WHERE " + KEY_ASSESSMENT_ID + " = " + assessment_id;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                AssessmentClassification assessmentClassification = new AssessmentClassification();

                assessmentClassification.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                assessmentClassification.setAssessment_id(cursor.getInt(cursor.getColumnIndex(KEY_ASSESSMENT_ID)));
                assessmentClassification.setDisease_classification_id(cursor.getInt(cursor.getColumnIndex(KEY_DISEASE_CLASSIFICATION_ID)));
                assessmentClassification.setClassification(cursor.getString(cursor.getColumnIndex(KEY_CLASSIFICATION)));
                assessmentClassification.setParent(cursor.getString(cursor.getColumnIndex(KEY_PARENT)));
                assessmentClassification.setSigns(cursor.getString(cursor.getColumnIndex(KEY_SIGN)));
                assessmentClassification.setTreatments(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));

                assessmentClassifications.add(assessmentClassification);
            } while (cursor.moveToNext());
        }
        return assessmentClassifications;
    }

    public List<String> getParents(int assessment_id){
        List<String> parents = new ArrayList<String>();
        String query = "SELECT DISTINCT "+ KEY_PARENT +" FROM " + TABLE_ASSESSMENT_CLASSIFICATIONS + " WHERE " + KEY_ASSESSMENT_ID + " = " + assessment_id;
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                parents.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return parents;
    }

    public List<AssessmentClassification> getAssessmentByParent(String parent){
        List<AssessmentClassification> assessmentClassifications = new ArrayList<AssessmentClassification>();
        String query = "SELECT * FROM " + TABLE_ASSESSMENT_CLASSIFICATIONS + " WHERE " + KEY_PARENT + " = '" + parent + "'";
        Cursor cursor = writableDB.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                AssessmentClassification assessmentClassification = new AssessmentClassification();

                assessmentClassification.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                assessmentClassification.setAssessment_id(cursor.getInt(cursor.getColumnIndex(KEY_ASSESSMENT_ID)));
                assessmentClassification.setDisease_classification_id(cursor.getInt(cursor.getColumnIndex(KEY_DISEASE_CLASSIFICATION_ID)));
                assessmentClassification.setClassification(cursor.getString(cursor.getColumnIndex(KEY_CLASSIFICATION)));
                assessmentClassification.setParent(cursor.getString(cursor.getColumnIndex(KEY_PARENT)));
                assessmentClassification.setSigns(cursor.getString(cursor.getColumnIndex(KEY_SIGN)));
                assessmentClassification.setTreatments(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));

                assessmentClassifications.add(assessmentClassification);
            } while (cursor.moveToNext());
        }
        return assessmentClassifications;
    }

    public void addClassificationSign(AssessmentClassificationSign sign){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, sign.getId());
        values.put(KEY_CLASSIFICATION_ID, sign.getClassification_id());
        values.put(KEY_SIGN, sign.getSign());

        writableDB.insert(TABLE_ASSESSMENT_CLASSIFICATION_SIGNS, null, values);
    }

    public void addClassificationSigns(List<AssessmentClassificationSign> signs){
        for (AssessmentClassificationSign sign : signs){
            addClassificationSign(sign);
        }
    }

    public List<AssessmentClassificationSign> getSign(int classification_id){
        List<AssessmentClassificationSign> signs = new ArrayList<AssessmentClassificationSign>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_ASSESSMENT_CLASSIFICATION_SIGNS, null, KEY_CLASSIFICATION_ID + " =?", new String[]{String.valueOf(classification_id)}, null, null, null);
        if (cur.moveToFirst()){
            do {
                AssessmentClassificationSign sign = new AssessmentClassificationSign();

                sign.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
                sign.setClassification_id(cur.getInt(cur.getColumnIndex(KEY_CLASSIFICATION_ID)));
                sign.setSign(cur.getString(cur.getColumnIndex(KEY_SIGN)));

                signs.add(sign);
            }while (cur.moveToNext());
        }
        return signs;
    }

    public void addClassificationTreatment(AssessmentClassificationTreatment treatment){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, treatment.getId());
        values.put(KEY_CLASSIFICATION_ID, treatment.getClassification_id());
        values.put(KEY_TREATMENT, treatment.getTreatment());

        writableDB.insert(TABLE_ASSESSMENT_CLASSIFICATION_TREATMENTS, null, values);
    }

    public void addClassificationTreatments(List<AssessmentClassificationTreatment> treatments){
        for (AssessmentClassificationTreatment treatment : treatments){
            addClassificationTreatment(treatment);
        }
    }

    public List<AssessmentClassificationTreatment> getTreatments(int classification_id){
        List<AssessmentClassificationTreatment> treatments = new ArrayList<AssessmentClassificationTreatment>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(TABLE_ASSESSMENT_CLASSIFICATION_TREATMENTS, null, KEY_CLASSIFICATION_ID + " =?", new String[]{String.valueOf(classification_id)}, null, null, null);
        if (cur.moveToFirst()){
            do {
                AssessmentClassificationTreatment treatment = new AssessmentClassificationTreatment();

                treatment.setId(cur.getInt(cur.getColumnIndex(KEY_ID)));
                treatment.setClassification_id(cur.getInt(cur.getColumnIndex(KEY_CLASSIFICATION_ID)));
                treatment.setTreatment(cur.getString(cur.getColumnIndex(KEY_TREATMENT)));

                treatments.add(treatment);
            }while (cur.moveToNext());
        }
        return treatments;
    }

    public void addGlossary(Glossary glossary){
        ContentValues values = new ContentValues();

        values.put(KEY_ID, glossary.getId());
        values.put(KEY_ACRONYM, glossary.getAcronym());
        values.put(KEY_DESCRIPTION, glossary.getDescription());

        writableDB.insert(TABLE_GLOSSARY, null, values);
    }
    public void addGlossaryItems(List<Glossary> glossaryList){
        for (Glossary glossary : glossaryList){
            addGlossary(glossary);
        }
    }

    public List<Glossary> getGlossary(){
        List<Glossary> glossaryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GLOSSARY, null, null, null,null,null,KEY_ACRONYM + " ASC");
        if (cursor.moveToFirst()){
            do {
                Glossary glossary = new Glossary();

                glossary.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                glossary.setAcronym(cursor.getString(cursor.getColumnIndex(KEY_ACRONYM)));
                glossary.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));

                glossaryList.add(glossary);
            }while (cursor.moveToNext());
        }
        return glossaryList;
    }

    public void addTreatTitle(TreatTitle treatTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, treatTitle.getId());
        values.put(KEY_AGE_GROUP_ID, treatTitle.getAge_group_id());
        values.put(KEY_TITLE, treatTitle.getTitle());
        values.put(KEY_GUIDE, treatTitle.getGuide());

        db.insert(TABLE_TREAT_TITLES, null, values);
    }

    public void addTreatmentTitles(List<TreatTitle> treatTitles){
        for (TreatTitle treatTitle:
             treatTitles) {
            addTreatTitle(treatTitle);
        }
    }

    public TreatTitle getTreatTitle(int id){
        TreatTitle title = new TreatTitle();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TREAT_TITLES, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            title.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            title.setAge_group_id(cursor.getColumnIndex(KEY_AGE_GROUP_ID));
            title.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            title.setGuide(cursor.getString(cursor.getColumnIndex(KEY_GUIDE)));
        }
        return title;
    }

    public List<TreatTitle> getTreatTitles(int age_group_id){
        List<TreatTitle> titles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TREAT_TITLES, null, KEY_AGE_GROUP_ID + "=?", new String[]{String.valueOf(age_group_id)}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                TreatTitle title = new TreatTitle();

                title.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                title.setAge_group_id(cursor.getColumnIndex(KEY_AGE_GROUP_ID));
                title.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                title.setGuide(cursor.getString(cursor.getColumnIndex(KEY_GUIDE)));

                titles.add(title);
            }while (cursor.moveToNext());
        }
        return titles;
    }

    public void addTreatAilment(TreatAilment treatAilment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(KEY_ID, treatAilment.getId());
        value.put(KEY_TITLE_ID, treatAilment.getTreat_titles_id());
        value.put(KEY_AILMENT, treatAilment.getAilment());
        value.put(KEY_CONTENT, treatAilment.getContent());

        db.insert(TABLE_TREAT_AILMENTS, null, value);
    }

    public void addTreatAilments(List<TreatAilment> treatAilments){
        for (TreatAilment treatAilment:
             treatAilments) {
            addTreatAilment(treatAilment);
        }
    }

    public List<TreatAilment> getTreatAilments(int title_id){
        List<TreatAilment> treatAilments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TREAT_AILMENTS, null, KEY_TITLE_ID + "=?", new String[]{String.valueOf(title_id)}, null, null, null);

        if (cursor.moveToFirst()){
            do{
                TreatAilment treatAilment = new TreatAilment();

                treatAilment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                treatAilment.setTreat_titles_id(cursor.getInt(cursor.getColumnIndex(KEY_TITLE_ID)));
                treatAilment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));
                treatAilment.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));

                treatAilments.add(treatAilment);
            }while(cursor.moveToNext());
        }
        return treatAilments;
    }

    public TreatAilment getTreatAilment(int id){
        TreatAilment treatAilment = new TreatAilment();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TREAT_AILMENTS, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            treatAilment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            treatAilment.setTreat_titles_id(cursor.getInt(cursor.getColumnIndex(KEY_TITLE_ID)));
            treatAilment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));
            treatAilment.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
        }
        return treatAilment;
    }

    public void addTreatAilmentTreatment(TreatAilmentTreatment treatment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(KEY_ID, treatment.getId());
        value.put(KEY_AILMENT_ID, treatment.getAilment_id());
        value.put(KEY_TREATMENT, treatment.getTreatment());
        value.put(KEY_CONTENT, treatment.getContent());

        db.insert(TABLE_TREAT_AILMENT_TREATMENTS, null, value);
    }

    public void addTreatAilmentTreatments(List<TreatAilmentTreatment> treatments){
        for (TreatAilmentTreatment treatment:
             treatments) {
            addTreatAilmentTreatment(treatment);
        }
    }

    public List<TreatAilmentTreatment> getTreatAilmentTreatments(int ailment_id){
        List<TreatAilmentTreatment> treatments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TREAT_AILMENT_TREATMENTS, null, KEY_AILMENT_ID + "=?", new String[]{String.valueOf(ailment_id)}, null, null, null);
        if (cursor.moveToFirst()){
            do{
                TreatAilmentTreatment treatment = new TreatAilmentTreatment();

                treatment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                treatment.setAilment_id(cursor.getInt(cursor.getColumnIndex(KEY_AILMENT_ID)));
                treatment.setTreatment(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));
                treatment.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));

                treatments.add(treatment);
            }while (cursor.moveToNext());
        }
        return treatments;
    }

    public TreatAilmentTreatment getTreatAilmentTreatment(int treatment_id) {
        TreatAilmentTreatment treatment = new TreatAilmentTreatment();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TREAT_AILMENT_TREATMENTS, null, KEY_ID + "=?", new String[]{String.valueOf(treatment_id)}, null, null, null);

        if (cursor.moveToFirst()){
            treatment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            treatment.setAilment_id(cursor.getInt(cursor.getColumnIndex(KEY_AILMENT_ID)));
            treatment.setTreatment(cursor.getString(cursor.getColumnIndex(KEY_TREATMENT)));
            treatment.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
        }
        return treatment;
    }

    public void addCounselTitle(CounselTitle title){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(KEY_ID, title.getId());
        value.put(KEY_IS_PARENT, title.getIs_parent());
        value.put(KEY_AGE_GROUP_ID, title.getAge_group_id());
        value.put(KEY_TITLE, title.getTitle());
        value.put(KEY_CONTENT, title.getContent());

        db.insert(TABLE_COUNSEL_TITLES, null, value);
    }

    public void addCounselTitles(List<CounselTitle> titles){
        for (CounselTitle title:
             titles) {
            addCounselTitle(title);
        }
    }

    public List<CounselTitle> getCounselTitles(int age_group_id){
        List<CounselTitle> titles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNSEL_TITLES, null, KEY_AGE_GROUP_ID + "=?", new String[]{String.valueOf(age_group_id)}, null, null, null);

        if (cursor.moveToFirst()){
            do{
                CounselTitle title = new CounselTitle();

                title.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                title.setIs_parent(cursor.getInt(cursor.getColumnIndex(KEY_IS_PARENT)));
                title.setAge_group_id(cursor.getInt(cursor.getColumnIndex(KEY_AGE_GROUP_ID)));
                title.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                title.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));

                titles.add(title);
            }while (cursor.moveToNext());
        }
        return titles;
    }

    public CounselTitle getCounselTitle(int id){
        CounselTitle title = new CounselTitle();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNSEL_TITLES, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            title.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            title.setIs_parent(cursor.getInt(cursor.getColumnIndex(KEY_IS_PARENT)));
            title.setAge_group_id(cursor.getInt(cursor.getColumnIndex(KEY_AGE_GROUP_ID)));
            title.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            title.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
        }
        return title;
    }

    public void addCounselSubContent(CounselSubContent subContent){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, subContent.getId());
        values.put(KEY_TITLE_ID, subContent.getCounsel_titles_id());
        values.put(KEY_SUB_CONTENT_TITLE, subContent.getSub_content_title());
        values.put(KEY_CONTENT, subContent.getContent());

        db.insert(TABLE_COUNSEL_SUB_CONTENT, null, values);
    }

    public void addCounselSubContents(List<CounselSubContent> subContents){
        for (CounselSubContent subContent:
             subContents) {
            addCounselSubContent(subContent);
        }
    }

    public List<CounselSubContent> getCounselSubContents(int title_id){
        List<CounselSubContent> subContents = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNSEL_SUB_CONTENT, null, KEY_TITLE_ID + "=?", new String[]{String.valueOf(title_id)}, null, null, null);

        if (cursor.moveToFirst()){
            do{
                CounselSubContent subContent = new CounselSubContent();

                subContent.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                subContent.setCounsel_titles_id(cursor.getInt(cursor.getColumnIndex(KEY_TITLE_ID)));
                subContent.setSub_content_title(cursor.getString(cursor.getColumnIndex(KEY_SUB_CONTENT_TITLE)));
                subContent.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));

                subContents.add(subContent);
            }while(cursor.moveToNext());
        }

        return subContents;
    }

    public CounselSubContent getCounselSubContent(int id){
        CounselSubContent subContent = new CounselSubContent();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNSEL_SUB_CONTENT, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            subContent.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            subContent.setCounsel_titles_id(cursor.getInt(cursor.getColumnIndex(KEY_TITLE_ID)));
            subContent.setSub_content_title(cursor.getString(cursor.getColumnIndex(KEY_SUB_CONTENT_TITLE)));
            subContent.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
        }
        return subContent;
    }

    public void addCounty(County county){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, county.getId());
        values.put(KEY_COUNTY, county.getCounty());

        db.insert(TABLE_COUNTY, null, values);
    }

    public void addCounties(List<County> counties){
        for (County county:
             counties) {
            this.addCounty(county);
        }
    }

    public List<County> getCounties(){
        List<County> counties = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTY, null, null, null, null, null, KEY_COUNTY + " ASC");

        if (cursor.moveToFirst()){
            do{
                County county = new County();

                county.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                county.setCounty(cursor.getString(cursor.getColumnIndex(KEY_COUNTY)));

                counties.add(county);
            }while(cursor.moveToNext());
        }
        return counties;
    }

    public County getCounty(int id){
        County county = new County();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTY, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()){
            county.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            county.setCounty(cursor.getString(cursor.getColumnIndex(KEY_COUNTY)));
        }

        return county;
    }

    public void addHIVCare(HIVCare care){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, care.getId());
        values.put(KEY_TITLE, care.getTitle());
        values.put(KEY_THUMBNAIL, care.getThumbnail());

        db.insert(TABLE_HIV_CARE, null, values);
    }

    public void addHIVCares(List<HIVCare> hivCareList){
        for (HIVCare care:
             hivCareList) {
            addHIVCare(care);
        }
    }

    public List<HIVCare> getHIVCare(){
        List<HIVCare> careList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HIV_CARE, null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do{
                HIVCare care = new HIVCare();

                care.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                care.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                care.setThumbnail(cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL)));

                careList.add(care);
            }while(cursor.moveToNext());
        }

        return careList;
    }

    public HIVCare getCare(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIV_CARE, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        HIVCare care = new HIVCare();

        if(cursor.moveToFirst()) {
            care.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            care.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            care.setThumbnail(cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL)));
        }

        return care;
    }

    public void addGalleries(List<Gallery> galleries){
        for (Gallery gallery:
             galleries) {
            addGallery(gallery);
        }
    }

    public void addGallery(Gallery gallery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, gallery.getId());
        values.put(KEY_TITLE, gallery.getTitle());
        values.put(KEY_DESCRIPTION, gallery.getDescription());
        values.put(KEY_GALLERY_AILMENT_ID, gallery.getGallery_ailments_id());
        values.put(KEY_GALLERY_ITEM_ID, gallery.getGallery_items_id());
        values.put(KEY_THUMBNAIL, gallery.getThumbnail());
        values.put(KEY_LINK, gallery.getLink());
        values.put(KEY_SIZE, gallery.getSize());
        values.put(KEY_MIME, gallery.getMime());
        values.put(KEY_TYPE, gallery.getType());
        values.put(KEY_CREATED_AT, gallery.getCreated_at());
        values.put(KEY_UPDATED_AT, gallery.getUpdated_at());

        db.insert(TABLE_GALLERY, null, values);
    }

    public void addGalleryItem(GalleryItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, item.getId());
        values.put(KEY_ITEM, item.getItem());

        db.insert(TABLE_GALLERY_ITEM, null, values);
    }

    public void addGalleryItems(List<GalleryItem> items){
        for (GalleryItem item:
             items) {
            addGalleryItem(item);
        }
    }

    public void addGalleryAilment(GalleryAilment ailment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, ailment.getId());
        values.put(KEY_AILMENT, ailment.getAilment());

        db.insert(TABLE_GALLERY_AILMENT, null, values);
    }

    public void addGalleryAilments(List<GalleryAilment> ailments){
        for (GalleryAilment ailment:
                ailments) {
            addGalleryAilment(ailment);
        }
    }

    public List<GalleryAilment> getGalleryAilments(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<GalleryAilment> ailments = new ArrayList<>();

        Cursor cursor = db.query(TABLE_GALLERY_AILMENT, null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do {
                GalleryAilment ailment = new GalleryAilment();

                ailment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                ailment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));

                ailments.add(ailment);
            }while (cursor.moveToNext());
        }
        return ailments;
    }

    public GalleryAilment getGalleryAilment(int id){
        GalleryAilment ailment = new GalleryAilment();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GALLERY_AILMENT, null, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()){
            ailment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            ailment.setAilment(cursor.getString(cursor.getColumnIndex(KEY_AILMENT)));
        }

        return ailment;
    }

    public List<GalleryItem> getGalleryItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<GalleryItem> items = new ArrayList<>();

        Cursor cursor = db.query(TABLE_GALLERY_ITEM, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                GalleryItem item = new GalleryItem();

                item.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                item.setItem(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));

                items.add(item);
            }while (cursor.moveToNext());
        }
        return items;
    }

    public List<GalleryItem> getGalleryItems(int ailment_id){
        SQLiteDatabase db = this.getReadableDatabase();
        List<GalleryItem> items = new ArrayList<>();

        Cursor cursor;

        if(ailment_id != 0) {

            String SQL = "SELECT gi." + KEY_ID + ", gi." + KEY_ITEM + " FROM " + TABLE_GALLERY_ITEM + " gi" +
                    " JOIN " + TABLE_GALLERY + " g ON g." + KEY_GALLERY_ITEM_ID + " = gi." + KEY_ID +
                    " JOIN " + TABLE_GALLERY_AILMENT + " a ON a." + KEY_ID + " = g." + KEY_GALLERY_AILMENT_ID + " AND a.id = ?" +
                    " GROUP BY gi." + KEY_ID;
            cursor = db.rawQuery(SQL, new String[]{String.valueOf(ailment_id)});
        }else{
            String SQL = "SELECT gi." + KEY_ID + ", gi." + KEY_ITEM + " FROM " + TABLE_GALLERY_ITEM + " gi" +
                    " JOIN " + TABLE_GALLERY + " g ON g." + KEY_GALLERY_ITEM_ID + " = gi." + KEY_ID +
                    " JOIN " + TABLE_GALLERY_AILMENT + " a ON a." + KEY_ID + " = g." + KEY_GALLERY_AILMENT_ID +
                    " GROUP BY gi." + KEY_ID;
            cursor = db.rawQuery(SQL, null);
        }

        if (cursor.moveToFirst()){
            do {
                GalleryItem item = new GalleryItem();

                item.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                item.setItem(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));

                items.add(item);
            }while (cursor.moveToNext());
        }
        return items;
    }

    public List<Gallery> getGallery(int ailment_id, int item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Gallery> galleryList = new ArrayList<>();

        Cursor cursor;
        if (ailment_id != 0) {
            cursor = db.query(TABLE_GALLERY, null, KEY_GALLERY_AILMENT_ID + "=? AND " + KEY_GALLERY_ITEM_ID + "=?", new String[]{String.valueOf(ailment_id), String.valueOf(item_id)}, null, null, null);
        }else{
            cursor = db.query(TABLE_GALLERY, null, KEY_GALLERY_ITEM_ID + "=?", new String[]{String.valueOf(item_id)}, null, null, null);
        }

        if (cursor.moveToFirst()){
            do {
                Gallery gallery = new Gallery();

                gallery.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                gallery.setGallery_ailments_id(cursor.getInt(cursor.getColumnIndex(KEY_GALLERY_AILMENT_ID)));
                gallery.setGallery_items_id(cursor.getInt(cursor.getColumnIndex(KEY_GALLERY_ITEM_ID)));
                gallery.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                gallery.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                gallery.setThumbnail(cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL)));
                gallery.setLink(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
                gallery.setSize(cursor.getString(cursor.getColumnIndex(KEY_SIZE)));
                gallery.setMime(cursor.getString(cursor.getColumnIndex(KEY_MIME)));
                gallery.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                gallery.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));

                galleryList.add(gallery);
            }while (cursor.moveToNext());
        }
        return galleryList;
    }
}
