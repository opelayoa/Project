package com.tiendas3b.almacen.db.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.FormApplied;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FORM_APPLIED".
*/
public class FormAppliedDao extends AbstractDao<FormApplied, Long> {

    public static final String TABLENAME = "FORM_APPLIED";

    /**
     * Properties of entity FormApplied.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property FormId = new Property(1, Long.class, "formId", false, "FORM_ID");
        public final static Property UserId = new Property(2, long.class, "userId", false, "USER_ID");
        public final static Property Date = new Property(3, java.util.Date.class, "date", false, "DATE");
        public final static Property EntityType = new Property(4, String.class, "entityType", false, "ENTITY_TYPE");
        public final static Property EntityId = new Property(5, long.class, "entityId", false, "ENTITY_ID");
    };

    private DaoSession daoSession;


    public FormAppliedDao(DaoConfig config) {
        super(config);
    }
    
    public FormAppliedDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FORM_APPLIED\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"FORM_ID\" INTEGER," + // 1: formId
                "\"USER_ID\" INTEGER NOT NULL ," + // 2: userId
                "\"DATE\" INTEGER," + // 3: date
                "\"ENTITY_TYPE\" TEXT NOT NULL ," + // 4: entityType
                "\"ENTITY_ID\" INTEGER NOT NULL );"); // 5: entityId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FORM_APPLIED\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FormApplied entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        Long formId = entity.getFormId();
        if (formId != null) {
            stmt.bindLong(2, formId);
        }
        stmt.bindLong(3, entity.getUserId());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
        stmt.bindString(5, entity.getEntityType());
        stmt.bindLong(6, entity.getEntityId());
    }

    @Override
    protected void attachEntity(FormApplied entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FormApplied readEntity(Cursor cursor, int offset) {
        FormApplied entity = new FormApplied( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // formId
            cursor.getLong(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // date
            cursor.getString(offset + 4), // entityType
            cursor.getLong(offset + 5) // entityId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FormApplied entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setFormId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUserId(cursor.getLong(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setEntityType(cursor.getString(offset + 4));
        entity.setEntityId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FormApplied entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FormApplied entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getFormDao().getAllColumns());
            builder.append(" FROM FORM_APPLIED T");
            builder.append(" LEFT JOIN FORM T0 ON T.\"FORM_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected FormApplied loadCurrentDeep(Cursor cursor, boolean lock) {
        FormApplied entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Form form = loadCurrentOther(daoSession.getFormDao(), cursor, offset);
        entity.setForm(form);

        return entity;    
    }

    public FormApplied loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<FormApplied> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<FormApplied> list = new ArrayList<FormApplied>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<FormApplied> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<FormApplied> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
