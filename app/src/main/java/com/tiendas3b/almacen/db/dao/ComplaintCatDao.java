package com.tiendas3b.almacen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.ComplaintCat;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPLAINT_CAT".
*/
public class ComplaintCatDao extends AbstractDao<ComplaintCat, Long> {

    public static final String TABLENAME = "COMPLAINT_CAT";

    /**
     * Properties of entity ComplaintCat.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Group = new Property(1, String.class, "group", false, "GROUP");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property Active = new Property(3, Boolean.class, "active", false, "ACTIVE");
        public final static Property IdStr = new Property(4, String.class, "idStr", false, "ID_STR");
        public final static Property Type = new Property(5, int.class, "type", false, "TYPE");
    };


    public ComplaintCatDao(DaoConfig config) {
        super(config);
    }
    
    public ComplaintCatDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPLAINT_CAT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"GROUP\" TEXT," + // 1: group
                "\"DESCRIPTION\" TEXT," + // 2: description
                "\"ACTIVE\" INTEGER," + // 3: active
                "\"ID_STR\" TEXT NOT NULL ," + // 4: idStr
                "\"TYPE\" INTEGER NOT NULL );"); // 5: type
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_COMPLAINT_CAT_ID_STR_TYPE ON COMPLAINT_CAT" +
                " (\"ID_STR\",\"TYPE\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPLAINT_CAT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ComplaintCat entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String group = entity.getGroup();
        if (group != null) {
            stmt.bindString(2, group);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        Boolean active = entity.getActive();
        if (active != null) {
            stmt.bindLong(4, active ? 1L: 0L);
        }
        stmt.bindString(5, entity.getIdStr());
        stmt.bindLong(6, entity.getType());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ComplaintCat readEntity(Cursor cursor, int offset) {
        ComplaintCat entity = new ComplaintCat( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // group
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0, // active
            cursor.getString(offset + 4), // idStr
            cursor.getInt(offset + 5) // type
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ComplaintCat entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGroup(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActive(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
        entity.setIdStr(cursor.getString(offset + 4));
        entity.setType(cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ComplaintCat entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ComplaintCat entity) {
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
    
}
