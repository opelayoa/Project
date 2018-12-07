package com.tiendas3b.almacen.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.almacen.db.dao.ConfigProvider;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONFIG_PROVIDER".
*/
public class ConfigProviderDao extends AbstractDao<ConfigProvider, Long> {

    public static final String TABLENAME = "CONFIG_PROVIDER";

    /**
     * Properties of entity ConfigProvider.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Active = new Property(2, boolean.class, "active", false, "ACTIVE");
        public final static Property P1 = new Property(3, int.class, "p1", false, "P1");
        public final static Property P2 = new Property(4, int.class, "p2", false, "P2");
        public final static Property P3 = new Property(5, int.class, "p3", false, "P3");
        public final static Property P4 = new Property(6, int.class, "p4", false, "P4");
        public final static Property P5 = new Property(7, int.class, "p5", false, "P5");
        public final static Property P6 = new Property(8, int.class, "p6", false, "P6");
        public final static Property P7 = new Property(9, int.class, "p7", false, "P7");
    };


    public ConfigProviderDao(DaoConfig config) {
        super(config);
    }
    
    public ConfigProviderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONFIG_PROVIDER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"ACTIVE\" INTEGER NOT NULL ," + // 2: active
                "\"P1\" INTEGER NOT NULL ," + // 3: p1
                "\"P2\" INTEGER NOT NULL ," + // 4: p2
                "\"P3\" INTEGER NOT NULL ," + // 5: p3
                "\"P4\" INTEGER NOT NULL ," + // 6: p4
                "\"P5\" INTEGER NOT NULL ," + // 7: p5
                "\"P6\" INTEGER NOT NULL ," + // 8: p6
                "\"P7\" INTEGER NOT NULL );"); // 9: p7
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONFIG_PROVIDER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ConfigProvider entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getActive() ? 1L: 0L);
        stmt.bindLong(4, entity.getP1());
        stmt.bindLong(5, entity.getP2());
        stmt.bindLong(6, entity.getP3());
        stmt.bindLong(7, entity.getP4());
        stmt.bindLong(8, entity.getP5());
        stmt.bindLong(9, entity.getP6());
        stmt.bindLong(10, entity.getP7());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ConfigProvider readEntity(Cursor cursor, int offset) {
        ConfigProvider entity = new ConfigProvider( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getShort(offset + 2) != 0, // active
            cursor.getInt(offset + 3), // p1
            cursor.getInt(offset + 4), // p2
            cursor.getInt(offset + 5), // p3
            cursor.getInt(offset + 6), // p4
            cursor.getInt(offset + 7), // p5
            cursor.getInt(offset + 8), // p6
            cursor.getInt(offset + 9) // p7
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ConfigProvider entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setActive(cursor.getShort(offset + 2) != 0);
        entity.setP1(cursor.getInt(offset + 3));
        entity.setP2(cursor.getInt(offset + 4));
        entity.setP3(cursor.getInt(offset + 5));
        entity.setP4(cursor.getInt(offset + 6));
        entity.setP5(cursor.getInt(offset + 7));
        entity.setP6(cursor.getInt(offset + 8));
        entity.setP7(cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ConfigProvider entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ConfigProvider entity) {
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