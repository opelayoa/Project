package com.tiendas3b.almacen.db.dao;

import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "VARTICLE".
 */
public class VArticle extends EqualsBase  {

    private Long id;
    private String description;
    private String unity;
    private Integer packing;
    private Long ivaId;
    private Float cost;
    private Float sale;
    private Short inosurte;
    private Short expiryMax;
    private Boolean purchasing;
    private Integer typeId;
    private String circuit;
    private Integer fatherIclave;
    private long activeId;
    private long unitId;
    private long providerId;
    private long iclave;
    private String barcode;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient VArticleDao myDao;

    private ArticleState active;
    private Long active__resolvedKey;

    private Unit unit;
    private Long unit__resolvedKey;

    private Provider provider;
    private Long provider__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public VArticle() {
    }

    public VArticle(Long id) {
        this.id = id;
    }

    public VArticle(Long id, String description, String unity, Integer packing, Long ivaId, Float cost, Float sale, Short inosurte, Short expiryMax, Boolean purchasing, Integer typeId, String circuit, Integer fatherIclave, long activeId, long unitId, long providerId, long iclave, String barcode) {
        this.id = id;
        this.description = description;
        this.unity = unity;
        this.packing = packing;
        this.ivaId = ivaId;
        this.cost = cost;
        this.sale = sale;
        this.inosurte = inosurte;
        this.expiryMax = expiryMax;
        this.purchasing = purchasing;
        this.typeId = typeId;
        this.circuit = circuit;
        this.fatherIclave = fatherIclave;
        this.activeId = activeId;
        this.unitId = unitId;
        this.providerId = providerId;
        this.iclave = iclave;
        this.barcode = barcode;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVArticleDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public Integer getPacking() {
        return packing;
    }

    public void setPacking(Integer packing) {
        this.packing = packing;
    }

    public Long getIvaId() {
        return ivaId;
    }

    public void setIvaId(Long ivaId) {
        this.ivaId = ivaId;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getSale() {
        return sale;
    }

    public void setSale(Float sale) {
        this.sale = sale;
    }

    public Short getInosurte() {
        return inosurte;
    }

    public void setInosurte(Short inosurte) {
        this.inosurte = inosurte;
    }

    public Short getExpiryMax() {
        return expiryMax;
    }

    public void setExpiryMax(Short expiryMax) {
        this.expiryMax = expiryMax;
    }

    public Boolean getPurchasing() {
        return purchasing;
    }

    public void setPurchasing(Boolean purchasing) {
        this.purchasing = purchasing;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public Integer getFatherIclave() {
        return fatherIclave;
    }

    public void setFatherIclave(Integer fatherIclave) {
        this.fatherIclave = fatherIclave;
    }

    public long getActiveId() {
        return activeId;
    }

    public void setActiveId(long activeId) {
        this.activeId = activeId;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public long getIclave() {
        return iclave;
    }

    public void setIclave(long iclave) {
        this.iclave = iclave;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /** To-one relationship, resolved on first access. */
    public ArticleState getActive() {
        long __key = this.activeId;
        if (active__resolvedKey == null || !active__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ArticleStateDao targetDao = daoSession.getArticleStateDao();
            ArticleState activeNew = targetDao.load(__key);
            synchronized (this) {
                active = activeNew;
            	active__resolvedKey = __key;
            }
        }
        return active;
    }

    public void setActive(ArticleState active) {
        if (active == null) {
            throw new DaoException("To-one property 'activeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.active = active;
            activeId = active.getId();
            active__resolvedKey = activeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Unit getUnit() {
        long __key = this.unitId;
        if (unit__resolvedKey == null || !unit__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDao targetDao = daoSession.getUnitDao();
            Unit unitNew = targetDao.load(__key);
            synchronized (this) {
                unit = unitNew;
            	unit__resolvedKey = __key;
            }
        }
        return unit;
    }

    public void setUnit(Unit unit) {
        if (unit == null) {
            throw new DaoException("To-one property 'unitId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.unit = unit;
            unitId = unit.getId();
            unit__resolvedKey = unitId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Provider getProvider() {
        long __key = this.providerId;
        if (provider__resolvedKey == null || !provider__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProviderDao targetDao = daoSession.getProviderDao();
            Provider providerNew = targetDao.load(__key);
            synchronized (this) {
                provider = providerNew;
            	provider__resolvedKey = __key;
            }
        }
        return provider;
    }

    public void setProvider(Provider provider) {
        if (provider == null) {
            throw new DaoException("To-one property 'providerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.provider = provider;
            providerId = provider.getId();
            provider__resolvedKey = providerId;
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
