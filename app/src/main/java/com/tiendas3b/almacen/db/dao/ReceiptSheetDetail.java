package com.tiendas3b.almacen.db.dao;

import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "RECEIPT_SHEET_DETAIL".
 */
public class ReceiptSheetDetail extends EqualsBase  {

    private Long id;
    private Long iclave;
    private String expiration;
    private String fillRate;
    private String description;
    private Integer numBoxesOdc;
    private Integer numBoxesEm;
    private long receiptSheetId;
    private long timetableReceiptId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ReceiptSheetDetailDao myDao;

    private ReceiptSheet receiptSheet;
    private Long receiptSheet__resolvedKey;

    private TimetableReceipt timetableReceipt;
    private Long timetableReceipt__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ReceiptSheetDetail() {
    }

    public ReceiptSheetDetail(Long id) {
        this.id = id;
    }

    public ReceiptSheetDetail(Long id, Long iclave, String expiration, String fillRate, String description, Integer numBoxesOdc, Integer numBoxesEm, long receiptSheetId, long timetableReceiptId) {
        this.id = id;
        this.iclave = iclave;
        this.expiration = expiration;
        this.fillRate = fillRate;
        this.description = description;
        this.numBoxesOdc = numBoxesOdc;
        this.numBoxesEm = numBoxesEm;
        this.receiptSheetId = receiptSheetId;
        this.timetableReceiptId = timetableReceiptId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReceiptSheetDetailDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIclave() {
        return iclave;
    }

    public void setIclave(Long iclave) {
        this.iclave = iclave;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getFillRate() {
        return fillRate;
    }

    public void setFillRate(String fillRate) {
        this.fillRate = fillRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumBoxesOdc() {
        return numBoxesOdc;
    }

    public void setNumBoxesOdc(Integer numBoxesOdc) {
        this.numBoxesOdc = numBoxesOdc;
    }

    public Integer getNumBoxesEm() {
        return numBoxesEm;
    }

    public void setNumBoxesEm(Integer numBoxesEm) {
        this.numBoxesEm = numBoxesEm;
    }

    public long getReceiptSheetId() {
        return receiptSheetId;
    }

    public void setReceiptSheetId(long receiptSheetId) {
        this.receiptSheetId = receiptSheetId;
    }

    public long getTimetableReceiptId() {
        return timetableReceiptId;
    }

    public void setTimetableReceiptId(long timetableReceiptId) {
        this.timetableReceiptId = timetableReceiptId;
    }

    /** To-one relationship, resolved on first access. */
    public ReceiptSheet getReceiptSheet() {
        long __key = this.receiptSheetId;
        if (receiptSheet__resolvedKey == null || !receiptSheet__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReceiptSheetDao targetDao = daoSession.getReceiptSheetDao();
            ReceiptSheet receiptSheetNew = targetDao.load(__key);
            synchronized (this) {
                receiptSheet = receiptSheetNew;
            	receiptSheet__resolvedKey = __key;
            }
        }
        return receiptSheet;
    }

    public void setReceiptSheet(ReceiptSheet receiptSheet) {
        if (receiptSheet == null) {
            throw new DaoException("To-one property 'receiptSheetId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.receiptSheet = receiptSheet;
            receiptSheetId = receiptSheet.getId();
            receiptSheet__resolvedKey = receiptSheetId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public TimetableReceipt getTimetableReceipt() {
        long __key = this.timetableReceiptId;
        if (timetableReceipt__resolvedKey == null || !timetableReceipt__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TimetableReceiptDao targetDao = daoSession.getTimetableReceiptDao();
            TimetableReceipt timetableReceiptNew = targetDao.load(__key);
            synchronized (this) {
                timetableReceipt = timetableReceiptNew;
            	timetableReceipt__resolvedKey = __key;
            }
        }
        return timetableReceipt;
    }

    public void setTimetableReceipt(TimetableReceipt timetableReceipt) {
        if (timetableReceipt == null) {
            throw new DaoException("To-one property 'timetableReceiptId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.timetableReceipt = timetableReceipt;
            timetableReceiptId = timetableReceipt.getId();
            timetableReceipt__resolvedKey = timetableReceiptId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
