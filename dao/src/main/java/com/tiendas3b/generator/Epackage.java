package com.tiendas3b.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

import static com.tiendas3b.generator.MyDaoGenerator.EQUALS_BASE;
import static com.tiendas3b.generator.MyDaoGenerator.SPINNER_BASE;

/**
 * Created by danflo on 03/03/2017 madafaka.
 */

class Epackage {

    static Entity addProviderEpackagae(Schema schema) {
        Entity entity = schema.addEntity("ProviderEpackage");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("name");
        entity.addIntProperty("status");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(storeIdForPK);
//        indexUnique.addProperty(paybillForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addPackage(Schema schema, Entity region, Entity store, Entity provider) {
        Entity entity = schema.addEntity("EPackage");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("folio");
        entity.addStringProperty("description");
//        entity.addLongProperty("providerId");
//        entity.addLongProperty("storeId");
//        entity.addLongProperty("regionId");
        entity.addLongProperty("articleId");
        entity.addStringProperty("receiptDate");
        entity.addStringProperty("barcode");
        entity.addStringProperty("unity");
        entity.addIntProperty("status");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property storeIdForPK = entity.addLongProperty("storeId").notNull().getProperty();
        entity.addToOne(store, storeIdForPK, "store");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        ToMany providertToPackages = provider.addToMany(entity, providerIdForPK);
        providertToPackages.setName("packages");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(storeIdForPK);
//        indexUnique.addProperty(paybillForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addEpackageTruck(Schema schema) {
        Entity entity = schema.addEntity("EpackageTruck");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(SPINNER_BASE);
        entity.addStringProperty("description");
        entity.addIntProperty("active");
        entity.addIntProperty("aliasId");
        entity.addStringProperty("barcode");
        entity.addStringProperty("imei");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(truckIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addEpackageDownload(Schema schema) {
        Entity entity = schema.addEntity("EpackageDownload");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
        entity.addIntProperty("activo");
        entity.addIntProperty("statusLeave");
        entity.addIntProperty("tarjetStatus");
        entity.addStringProperty("fecha");

        Property almacenForPK = entity.addLongProperty("almacen").notNull().getProperty();
        Property camClaveForPK = entity.addLongProperty("camClave").notNull().getProperty();
        Property packageIdForPK = entity.addLongProperty("packageId").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(almacenForPK);
        indexUnique.addProperty(camClaveForPK);
        indexUnique.addProperty(packageIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }
}
