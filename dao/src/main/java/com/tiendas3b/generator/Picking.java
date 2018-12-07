package com.tiendas3b.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

import static com.tiendas3b.generator.MyDaoGenerator.EQUALS_BASE;

/**
 * Created by danflo on 03/03/2017 madafaka.
 */

class Picking {

    static Entity addOrder(Schema schema, Entity region, Entity store, Entity storePickingStatus) {
        Entity entity = schema.addEntity("OrderPicking");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("status");
        entity.addIntProperty("type");
//        entity.addBooleanProperty("forced");
        entity.addStringProperty("date");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property paybillForPK = entity.addIntProperty("paybill").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(paybillForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addStorePickingStatus(Schema schema, Entity region, Entity store/*, Entity order*/) {
        Entity entity = schema.addEntity("StorePickingStatus");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("pickDate").notNull();
        entity.addIntProperty("billSmn").notNull();
        Property otroiSmn = entity.addIntProperty("otroiSmn").notNull().getProperty();
        entity.addStringProperty("alias").notNull();
        entity.addBooleanProperty("ftp");
        entity.addStringProperty("orderDate");
        entity.addIntProperty("status").notNull();

//        Property orderIdForPK = entity.addLongProperty("orderId").notNull().getProperty();
//        entity.addToOne(order, orderIdForPK, "order");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");


        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(otroiSmn);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addOrderDetail(Schema schema, Entity order, Entity article) {
        Entity entity = schema.addEntity("OrderDetail");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("existence");
        entity.addIntProperty("quantity").notNull();
        entity.addStringProperty("type").notNull();
//        entity.addStringProperty("level").notNull();
        entity.addIntProperty("fatherIclave");
        entity.addBooleanProperty("child");
        Property orderIdForPK = entity.addLongProperty("orderId").notNull().getProperty();
        entity.addToOne(order, orderIdForPK, "order");

        Property articleIdForPK = entity.addLongProperty("articleId").notNull().getProperty();
        entity.addToOne(article, articleIdForPK, "article");

        Property iclaveForPK = entity.addIntProperty("iclave").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(orderIdForPK);
        indexUnique.addProperty(iclaveForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        ToMany orderToDetail = order.addToMany(entity, orderIdForPK);
        orderToDetail.setName("details");

        return entity;
    }

    static Entity addOrderCapture(Schema schema, Entity region, Entity store) {
        Entity entity = schema.addEntity("OrderPickingCapture");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
//        entity.addIntProperty("status");
//        entity.addStringProperty("date");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property paybillForPK = entity.addIntProperty("paybill").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(paybillForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addOrderDetailCapture(Schema schema, Entity orderCapture, Entity article) {
        Entity entity = schema.addEntity("OrderDetailCapture");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("order").notNull();
        entity.addIntProperty("quantity").notNull();
        entity.addStringProperty("type").notNull();
        entity.addStringProperty("status").notNull();
        entity.addIntProperty("packing").notNull();
        entity.addIntProperty("fatherIclave");
        entity.addBooleanProperty("child").notNull();
        Property orderIdForPK = entity.addLongProperty("orderCaptureId").notNull().getProperty();
        entity.addToOne(orderCapture, orderIdForPK, "orderCapture");

        Property articleIdForPK = entity.addLongProperty("articleId").notNull().getProperty();
        entity.addToOne(article, articleIdForPK, "article");

        Property iclaveForPK = entity.addIntProperty("iclave").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(orderIdForPK);
        indexUnique.addProperty(iclaveForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        ToMany orderToDetail = orderCapture.addToMany(entity, orderIdForPK);
        orderToDetail.setName("details");

        return entity;
    }

    static Entity addScanTime(Schema schema, Entity orderPickingCapture) {
        Entity entity = schema.addEntity("ScanTime");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
//        entity.addIntProperty("status");
//        entity.addStringProperty("date");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(orderPickingCapture, regionIdForPK, "region");

//        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
//        entity.addToOne(store, storeIdForPK, "store");

        Property paybillForPK = entity.addIntProperty("paybill").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(storeIdForPK);
        indexUnique.addProperty(paybillForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }
}
