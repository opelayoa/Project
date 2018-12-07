package com.tiendas3b.almacen.db.dao;

import java.util.List;
import com.tiendas3b.almacen.db.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "FORM_APPLIED".
 */
public class FormApplied {

    private long id;
    private Long formId;
    private long userId;
    private java.util.Date date;
    /** Not-null value. */
    private String entityType;
    private long entityId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FormAppliedDao myDao;

    private Form form;
    private Long form__resolvedKey;

    private List<FormAppliedResult> formAppliedResultList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public FormApplied() {
    }

    public FormApplied(long id) {
        this.id = id;
    }

    public FormApplied(long id, Long formId, long userId, java.util.Date date, String entityType, long entityId) {
        this.id = id;
        this.formId = formId;
        this.userId = userId;
        this.date = date;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFormAppliedDao() : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /** Not-null value. */
    public String getEntityType() {
        return entityType;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /** To-one relationship, resolved on first access. */
    public Form getForm() {
        Long __key = this.formId;
        if (form__resolvedKey == null || !form__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormDao targetDao = daoSession.getFormDao();
            Form formNew = targetDao.load(__key);
            synchronized (this) {
                form = formNew;
            	form__resolvedKey = __key;
            }
        }
        return form;
    }

    public void setForm(Form form) {
        synchronized (this) {
            this.form = form;
            formId = form == null ? null : form.getId();
            form__resolvedKey = formId;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<FormAppliedResult> getFormAppliedResultList() {
        if (formAppliedResultList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormAppliedResultDao targetDao = daoSession.getFormAppliedResultDao();
            List<FormAppliedResult> formAppliedResultListNew = targetDao._queryFormApplied_FormAppliedResultList(id);
            synchronized (this) {
                if(formAppliedResultList == null) {
                    formAppliedResultList = formAppliedResultListNew;
                }
            }
        }
        return formAppliedResultList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetFormAppliedResultList() {
        formAppliedResultList = null;
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
