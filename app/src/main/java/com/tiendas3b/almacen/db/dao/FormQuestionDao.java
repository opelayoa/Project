package com.tiendas3b.almacen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.FormQuestion;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FORM_QUESTION".
*/
public class FormQuestionDao extends AbstractDao<FormQuestion, Void> {

    public static final String TABLENAME = "FORM_QUESTION";

    /**
     * Properties of entity FormQuestion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property FormId = new Property(0, long.class, "formId", false, "FORM_ID");
        public final static Property QuestionId = new Property(1, long.class, "questionId", false, "QUESTION_ID");
    };


    public FormQuestionDao(DaoConfig config) {
        super(config);
    }
    
    public FormQuestionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FORM_QUESTION\" (" + //
                "\"FORM_ID\" INTEGER NOT NULL ," + // 0: formId
                "\"QUESTION_ID\" INTEGER NOT NULL );"); // 1: questionId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_FORM_QUESTION_FORM_ID_QUESTION_ID ON FORM_QUESTION" +
                " (\"FORM_ID\",\"QUESTION_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FORM_QUESTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FormQuestion entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getFormId());
        stmt.bindLong(2, entity.getQuestionId());
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public FormQuestion readEntity(Cursor cursor, int offset) {
        FormQuestion entity = new FormQuestion( //
            cursor.getLong(offset + 0), // formId
            cursor.getLong(offset + 1) // questionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FormQuestion entity, int offset) {
        entity.setFormId(cursor.getLong(offset + 0));
        entity.setQuestionId(cursor.getLong(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(FormQuestion entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(FormQuestion entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
