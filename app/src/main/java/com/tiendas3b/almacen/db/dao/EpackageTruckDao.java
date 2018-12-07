package com.tiendas3b.almacen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.EpackageTruck;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EPACKAGE_TRUCK".
*/
public class EpackageTruckDao extends AbstractDao<EpackageTruck, Long> {

    public static final String TABLENAME = "EPACKAGE_TRUCK";

    /**
     * Properties of entity EpackageTruck.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Description = new Property(1, String.class, "description", false, "DESCRIPTION");
        public final static Property Active = new Property(2, Integer.class, "active", false, "ACTIVE");
        public final static Property AliasId = new Property(3, Integer.class, "aliasId", false, "ALIAS_ID");
        public final static Property Barcode = new Property(4, String.class, "barcode", false, "BARCODE");
        public final static Property Imei = new Property(5, String.class, "imei", false, "IMEI");
        public final static Property RegionId = new Property(6, long.class, "regionId", false, "REGION_ID");
        public final static Property TruckId = new Property(7, long.class, "truckId", false, "TRUCK_ID");
    };


    public EpackageTruckDao(DaoConfig config) {
        super(config);
    }
    
    public EpackageTruckDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EPACKAGE_TRUCK\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"DESCRIPTION\" TEXT," + // 1: description
                "\"ACTIVE\" INTEGER," + // 2: active
                "\"ALIAS_ID\" INTEGER," + // 3: aliasId
                "\"BARCODE\" TEXT," + // 4: barcode
                "\"IMEI\" TEXT," + // 5: imei
                "\"REGION_ID\" INTEGER NOT NULL ," + // 6: regionId
                "\"TRUCK_ID\" INTEGER NOT NULL );"); // 7: truckId
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_EPACKAGE_TRUCK_REGION_ID_TRUCK_ID ON EPACKAGE_TRUCK" +
                " (\"REGION_ID\",\"TRUCK_ID\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EPACKAGE_TRUCK\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EpackageTruck entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(2, description);
        }
 
        Integer active = entity.getActive();
        if (active != null) {
            stmt.bindLong(3, active);
        }
 
        Integer aliasId = entity.getAliasId();
        if (aliasId != null) {
            stmt.bindLong(4, aliasId);
        }
 
        String barcode = entity.getBarcode();
        if (barcode != null) {
            stmt.bindString(5, barcode);
        }
 
        String imei = entity.getImei();
        if (imei != null) {
            stmt.bindString(6, imei);
        }
        stmt.bindLong(7, entity.getRegionId());
        stmt.bindLong(8, entity.getTruckId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public EpackageTruck readEntity(Cursor cursor, int offset) {
        EpackageTruck entity = new EpackageTruck( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // description
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // active
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // aliasId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // barcode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // imei
            cursor.getLong(offset + 6), // regionId
            cursor.getLong(offset + 7) // truckId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EpackageTruck entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDescription(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setActive(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setAliasId(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setBarcode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setImei(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRegionId(cursor.getLong(offset + 6));
        entity.setTruckId(cursor.getLong(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(EpackageTruck entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(EpackageTruck entity) {
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
