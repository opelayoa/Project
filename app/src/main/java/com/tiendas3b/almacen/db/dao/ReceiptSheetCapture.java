package com.tiendas3b.almacen.db.dao;

import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "RECEIPT_SHEET_CAPTURE".
 */
public class ReceiptSheetCapture extends EqualsBase  {

    private Long id;
    private String dateTime;
    private String arriveTime;
    private String deliveryTime;
    private String departureTime;
    private Integer arrive;
    private Integer numReceivers;
    private Integer paletizado;
    private Float amountFact;
    private Float amountEm;
    private Float iva;
    private Float ieps;
    private String deliveryman;
    private String receiver;
    private Integer platform;
    private Long levelId;
    private Long dateTypeId;
    private long buyId;
    /** Not-null value. */
    private String receiptDate;
    private int odc;
    private String facturaRef;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ReceiptSheetCaptureDao myDao;

    private Level level;
    private Long level__resolvedKey;

    private DateType dateType;
    private Long dateType__resolvedKey;

    private Buy buy;
    private Long buy__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ReceiptSheetCapture() {
    }

    public ReceiptSheetCapture(Long id) {
        this.id = id;
    }

    public ReceiptSheetCapture(Long id, String dateTime, String arriveTime, String deliveryTime, String departureTime, Integer arrive, Integer numReceivers, Integer paletizado, Float amountFact, Float amountEm, Float iva, Float ieps, String deliveryman, String receiver, Integer platform, Long levelId, Long dateTypeId, long buyId, String receiptDate, int odc, String facturaRef) {
        this.id = id;
        this.dateTime = dateTime;
        this.arriveTime = arriveTime;
        this.deliveryTime = deliveryTime;
        this.departureTime = departureTime;
        this.arrive = arrive;
        this.numReceivers = numReceivers;
        this.paletizado = paletizado;
        this.amountFact = amountFact;
        this.amountEm = amountEm;
        this.iva = iva;
        this.ieps = ieps;
        this.deliveryman = deliveryman;
        this.receiver = receiver;
        this.platform = platform;
        this.levelId = levelId;
        this.dateTypeId = dateTypeId;
        this.buyId = buyId;
        this.receiptDate = receiptDate;
        this.odc = odc;
        this.facturaRef = facturaRef;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReceiptSheetCaptureDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getArrive() {
        return arrive;
    }

    public void setArrive(Integer arrive) {
        this.arrive = arrive;
    }

    public Integer getNumReceivers() {
        return numReceivers;
    }

    public void setNumReceivers(Integer numReceivers) {
        this.numReceivers = numReceivers;
    }

    public Integer getPaletizado() {
        return paletizado;
    }

    public void setPaletizado(Integer paletizado) {
        this.paletizado = paletizado;
    }

    public Float getAmountFact() {
        return amountFact;
    }

    public void setAmountFact(Float amountFact) {
        this.amountFact = amountFact;
    }

    public Float getAmountEm() {
        return amountEm;
    }

    public void setAmountEm(Float amountEm) {
        this.amountEm = amountEm;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public Float getIeps() {
        return ieps;
    }

    public void setIeps(Float ieps) {
        this.ieps = ieps;
    }

    public String getDeliveryman() {
        return deliveryman;
    }

    public void setDeliveryman(String deliveryman) {
        this.deliveryman = deliveryman;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Long getDateTypeId() {
        return dateTypeId;
    }

    public void setDateTypeId(Long dateTypeId) {
        this.dateTypeId = dateTypeId;
    }

    public long getBuyId() {
        return buyId;
    }

    public void setBuyId(long buyId) {
        this.buyId = buyId;
    }

    /** Not-null value. */
    public String getReceiptDate() {
        return receiptDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public int getOdc() {
        return odc;
    }

    public void setOdc(int odc) {
        this.odc = odc;
    }

    public String getFacturaRef() {
        return facturaRef;
    }

    public void setFacturaRef(String facturaRef) {
        this.facturaRef = facturaRef;
    }

    /** To-one relationship, resolved on first access. */
    public Level getLevel() {
        Long __key = this.levelId;
        if (level__resolvedKey == null || !level__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LevelDao targetDao = daoSession.getLevelDao();
            Level levelNew = targetDao.load(__key);
            synchronized (this) {
                level = levelNew;
            	level__resolvedKey = __key;
            }
        }
        return level;
    }

    public void setLevel(Level level) {
        synchronized (this) {
            this.level = level;
            levelId = level == null ? null : level.getId();
            level__resolvedKey = levelId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public DateType getDateType() {
        Long __key = this.dateTypeId;
        if (dateType__resolvedKey == null || !dateType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DateTypeDao targetDao = daoSession.getDateTypeDao();
            DateType dateTypeNew = targetDao.load(__key);
            synchronized (this) {
                dateType = dateTypeNew;
            	dateType__resolvedKey = __key;
            }
        }
        return dateType;
    }

    public void setDateType(DateType dateType) {
        synchronized (this) {
            this.dateType = dateType;
            dateTypeId = dateType == null ? null : dateType.getId();
            dateType__resolvedKey = dateTypeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Buy getBuy() {
        long __key = this.buyId;
        if (buy__resolvedKey == null || !buy__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BuyDao targetDao = daoSession.getBuyDao();
            Buy buyNew = targetDao.load(__key);
            synchronized (this) {
                buy = buyNew;
            	buy__resolvedKey = __key;
            }
        }
        return buy;
    }

    public void setBuy(Buy buy) {
        if (buy == null) {
            throw new DaoException("To-one property 'buyId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.buy = buy;
            buyId = buy.getId();
            buy__resolvedKey = buyId;
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
