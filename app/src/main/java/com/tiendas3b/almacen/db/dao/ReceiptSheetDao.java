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

import com.tiendas3b.almacen.db.dao.ReceiptSheet;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RECEIPT_SHEET".
*/
public class ReceiptSheetDao extends AbstractDao<ReceiptSheet, Long> {

    public static final String TABLENAME = "RECEIPT_SHEET";

    /**
     * Properties of entity ReceiptSheet.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Level = new Property(1, Integer.class, "level", false, "LEVEL");
        public final static Property DateTime = new Property(2, String.class, "dateTime", false, "DATE_TIME");
        public final static Property ArriveTime = new Property(3, String.class, "arriveTime", false, "ARRIVE_TIME");
        public final static Property DeliveryTime = new Property(4, String.class, "deliveryTime", false, "DELIVERY_TIME");
        public final static Property DepartureTime = new Property(5, String.class, "departureTime", false, "DEPARTURE_TIME");
        public final static Property Arrive = new Property(6, Boolean.class, "arrive", false, "ARRIVE");
        public final static Property NumReceivers = new Property(7, Integer.class, "numReceivers", false, "NUM_RECEIVERS");
        public final static Property WithoutDate = new Property(8, Integer.class, "withoutDate", false, "WITHOUT_DATE");
        public final static Property Folio = new Property(9, Integer.class, "folio", false, "FOLIO");
        public final static Property Paletizado = new Property(10, Integer.class, "paletizado", false, "PALETIZADO");
        public final static Property AmountFact = new Property(11, Float.class, "amountFact", false, "AMOUNT_FACT");
        public final static Property AmountEm = new Property(12, Float.class, "amountEm", false, "AMOUNT_EM");
        public final static Property RfcProvider = new Property(13, String.class, "rfcProvider", false, "RFC_PROVIDER");
        public final static Property Iva = new Property(14, Float.class, "iva", false, "IVA");
        public final static Property Ieps = new Property(15, Float.class, "ieps", false, "IEPS");
        public final static Property Platform = new Property(16, Integer.class, "platform", false, "PLATFORM");
        public final static Property RegionId = new Property(17, long.class, "regionId", false, "REGION_ID");
        public final static Property ProviderId = new Property(18, long.class, "providerId", false, "PROVIDER_ID");
        public final static Property ReceiptDate = new Property(19, java.util.Date.class, "receiptDate", false, "RECEIPT_DATE");
        public final static Property Odc = new Property(20, int.class, "odc", false, "ODC");
        public final static Property FacturaRef = new Property(21, String.class, "facturaRef", false, "FACTURA_REF");
    };

    private DaoSession daoSession;


    public ReceiptSheetDao(DaoConfig config) {
        super(config);
    }
    
    public ReceiptSheetDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECEIPT_SHEET\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"LEVEL\" INTEGER," + // 1: level
                "\"DATE_TIME\" TEXT," + // 2: dateTime
                "\"ARRIVE_TIME\" TEXT," + // 3: arriveTime
                "\"DELIVERY_TIME\" TEXT," + // 4: deliveryTime
                "\"DEPARTURE_TIME\" TEXT," + // 5: departureTime
                "\"ARRIVE\" INTEGER," + // 6: arrive
                "\"NUM_RECEIVERS\" INTEGER," + // 7: numReceivers
                "\"WITHOUT_DATE\" INTEGER," + // 8: withoutDate
                "\"FOLIO\" INTEGER," + // 9: folio
                "\"PALETIZADO\" INTEGER," + // 10: paletizado
                "\"AMOUNT_FACT\" REAL," + // 11: amountFact
                "\"AMOUNT_EM\" REAL," + // 12: amountEm
                "\"RFC_PROVIDER\" TEXT," + // 13: rfcProvider
                "\"IVA\" REAL," + // 14: iva
                "\"IEPS\" REAL," + // 15: ieps
                "\"PLATFORM\" INTEGER," + // 16: platform
                "\"REGION_ID\" INTEGER NOT NULL ," + // 17: regionId
                "\"PROVIDER_ID\" INTEGER NOT NULL ," + // 18: providerId
                "\"RECEIPT_DATE\" INTEGER NOT NULL ," + // 19: receiptDate
                "\"ODC\" INTEGER NOT NULL ," + // 20: odc
                "\"FACTURA_REF\" TEXT NOT NULL );"); // 21: facturaRef
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_RECEIPT_SHEET_REGION_ID_PROVIDER_ID_FACTURA_REF_RECEIPT_DATE_ODC ON RECEIPT_SHEET" +
                " (\"REGION_ID\",\"PROVIDER_ID\",\"FACTURA_REF\",\"RECEIPT_DATE\",\"ODC\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECEIPT_SHEET\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReceiptSheet entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(2, level);
        }
 
        String dateTime = entity.getDateTime();
        if (dateTime != null) {
            stmt.bindString(3, dateTime);
        }
 
        String arriveTime = entity.getArriveTime();
        if (arriveTime != null) {
            stmt.bindString(4, arriveTime);
        }
 
        String deliveryTime = entity.getDeliveryTime();
        if (deliveryTime != null) {
            stmt.bindString(5, deliveryTime);
        }
 
        String departureTime = entity.getDepartureTime();
        if (departureTime != null) {
            stmt.bindString(6, departureTime);
        }
 
        Boolean arrive = entity.getArrive();
        if (arrive != null) {
            stmt.bindLong(7, arrive ? 1L: 0L);
        }
 
        Integer numReceivers = entity.getNumReceivers();
        if (numReceivers != null) {
            stmt.bindLong(8, numReceivers);
        }
 
        Integer withoutDate = entity.getWithoutDate();
        if (withoutDate != null) {
            stmt.bindLong(9, withoutDate);
        }
 
        Integer folio = entity.getFolio();
        if (folio != null) {
            stmt.bindLong(10, folio);
        }
 
        Integer paletizado = entity.getPaletizado();
        if (paletizado != null) {
            stmt.bindLong(11, paletizado);
        }
 
        Float amountFact = entity.getAmountFact();
        if (amountFact != null) {
            stmt.bindDouble(12, amountFact);
        }
 
        Float amountEm = entity.getAmountEm();
        if (amountEm != null) {
            stmt.bindDouble(13, amountEm);
        }
 
        String rfcProvider = entity.getRfcProvider();
        if (rfcProvider != null) {
            stmt.bindString(14, rfcProvider);
        }
 
        Float iva = entity.getIva();
        if (iva != null) {
            stmt.bindDouble(15, iva);
        }
 
        Float ieps = entity.getIeps();
        if (ieps != null) {
            stmt.bindDouble(16, ieps);
        }
 
        Integer platform = entity.getPlatform();
        if (platform != null) {
            stmt.bindLong(17, platform);
        }
        stmt.bindLong(18, entity.getRegionId());
        stmt.bindLong(19, entity.getProviderId());
        stmt.bindLong(20, entity.getReceiptDate().getTime());
        stmt.bindLong(21, entity.getOdc());
        stmt.bindString(22, entity.getFacturaRef());
    }

    @Override
    protected void attachEntity(ReceiptSheet entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ReceiptSheet readEntity(Cursor cursor, int offset) {
        ReceiptSheet entity = new ReceiptSheet( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // level
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dateTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // arriveTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // deliveryTime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // departureTime
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // arrive
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // numReceivers
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // withoutDate
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // folio
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // paletizado
            cursor.isNull(offset + 11) ? null : cursor.getFloat(offset + 11), // amountFact
            cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12), // amountEm
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // rfcProvider
            cursor.isNull(offset + 14) ? null : cursor.getFloat(offset + 14), // iva
            cursor.isNull(offset + 15) ? null : cursor.getFloat(offset + 15), // ieps
            cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // platform
            cursor.getLong(offset + 17), // regionId
            cursor.getLong(offset + 18), // providerId
            new java.util.Date(cursor.getLong(offset + 19)), // receiptDate
            cursor.getInt(offset + 20), // odc
            cursor.getString(offset + 21) // facturaRef
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReceiptSheet entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLevel(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setDateTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setArriveTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDeliveryTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDepartureTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setArrive(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setNumReceivers(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setWithoutDate(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setFolio(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setPaletizado(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setAmountFact(cursor.isNull(offset + 11) ? null : cursor.getFloat(offset + 11));
        entity.setAmountEm(cursor.isNull(offset + 12) ? null : cursor.getFloat(offset + 12));
        entity.setRfcProvider(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setIva(cursor.isNull(offset + 14) ? null : cursor.getFloat(offset + 14));
        entity.setIeps(cursor.isNull(offset + 15) ? null : cursor.getFloat(offset + 15));
        entity.setPlatform(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setRegionId(cursor.getLong(offset + 17));
        entity.setProviderId(cursor.getLong(offset + 18));
        entity.setReceiptDate(new java.util.Date(cursor.getLong(offset + 19)));
        entity.setOdc(cursor.getInt(offset + 20));
        entity.setFacturaRef(cursor.getString(offset + 21));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReceiptSheet entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReceiptSheet entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getRegionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getProviderDao().getAllColumns());
            builder.append(" FROM RECEIPT_SHEET T");
            builder.append(" LEFT JOIN REGION T0 ON T.\"REGION_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN PROVIDER T1 ON T.\"PROVIDER_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ReceiptSheet loadCurrentDeep(Cursor cursor, boolean lock) {
        ReceiptSheet entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Region region = loadCurrentOther(daoSession.getRegionDao(), cursor, offset);
         if(region != null) {
            entity.setRegion(region);
        }
        offset += daoSession.getRegionDao().getAllColumns().length;

        Provider provider = loadCurrentOther(daoSession.getProviderDao(), cursor, offset);
         if(provider != null) {
            entity.setProvider(provider);
        }

        return entity;    
    }

    public ReceiptSheet loadDeep(Long key) {
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
    public List<ReceiptSheet> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ReceiptSheet> list = new ArrayList<ReceiptSheet>(count);
        
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
    
    protected List<ReceiptSheet> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ReceiptSheet> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
