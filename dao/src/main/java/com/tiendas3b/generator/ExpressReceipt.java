package com.tiendas3b.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

import static com.tiendas3b.generator.MyDaoGenerator.EQUALS_BASE;

/**
 * Created by danflo on 03/03/2017 madafaka.
 */

class ExpressReceipt {

    static Entity addExpressReceipt(Schema schema, Entity region, Entity buyDetail) {
        Entity entity = schema.addEntity("ExpressReceipt");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("barcode");
        entity.addStringProperty("brandType");
//        entity.addStringProperty("date");
        entity.addStringProperty("expirationDate");
        entity.addIntProperty("boxesPallet");
        entity.addIntProperty("folio");
        Property odc = entity.addIntProperty("folioOdc").getProperty();
        entity.addIntProperty("folioReceipt");
        Property iclave = entity.addIntProperty("iclave").getProperty();
        entity.addIntProperty("cantidad").notNull();
        entity.addStringProperty("lote");
        entity.addBooleanProperty("received");
        Property scaffoldNum = entity.addShortProperty("scaffoldNum").getProperty();
        entity.addStringProperty("unit");
        entity.addStringProperty("unitType");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property buyDetailIdForPK = entity.addLongProperty("buyDetailId").notNull().getProperty();
        entity.addToOne(buyDetail, buyDetailIdForPK, "buyDetail");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(odc);
        indexUnique.addProperty(iclave);
        indexUnique.addProperty(scaffoldNum);
//        indexUnique.addProperty(buyDetailIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addExpressProvider(Schema schema, Entity region, Entity provider) {
        Entity entity = schema.addEntity("ExpressProvider");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("type");
        entity.addBooleanProperty("automatic");
        entity.addBooleanProperty("active");
        entity.addBooleanProperty("ticket");
        entity.addStringProperty("delivery");
        entity.addIntProperty("unity");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().unique().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(providerIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addExpressArticles(Schema schema, Entity region, Entity provider) {
        Entity entity = schema.addEntity("ExpressArticle");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("description");
        entity.addStringProperty("unity");
        entity.addStringProperty("articleType");
        entity.addStringProperty("palletType");

        Property iclave = entity.addIntProperty("iclave").getProperty();
        Property odc = entity.addIntProperty("odc").getProperty();
        entity.addIntProperty("piecesOdc");
        entity.addIntProperty("boxesOdc");
        entity.addIntProperty("palletsOdc");
        entity.addIntProperty("piecesAsn");
        entity.addIntProperty("boxesAsn");
        entity.addIntProperty("palletsAsn");
        entity.addIntProperty("piecesEm");
        entity.addIntProperty("boxesEm");
        entity.addIntProperty("palletsEm");
        entity.addIntProperty("piecesBill");
        entity.addIntProperty("boxesBill");
        entity.addIntProperty("palletsBill");
        entity.addIntProperty("piecesByBox");
        entity.addIntProperty("boxesByBed");
        entity.addIntProperty("bedByPallet");
        entity.addIntProperty("boxesByPallet");
        entity.addIntProperty("lastPallet");
        entity.addIntProperty("folioEm");

        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

        Property providerIdForPK = entity.addLongProperty("providerId").notNull().getProperty();
        entity.addToOne(provider, providerIdForPK, "provider");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(providerIdForPK);
        indexUnique.addProperty(odc);
        indexUnique.addProperty(iclave);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addConfigProvider(Schema schema) {
        Entity entity = schema.addEntity("ConfigProvider");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
        entity.addStringProperty("name");
        entity.addBooleanProperty("active").notNull();
        entity.addIntProperty("p1").notNull();
        entity.addIntProperty("p2").notNull();
        entity.addIntProperty("p3").notNull();
        entity.addIntProperty("p4").notNull();
        entity.addIntProperty("p5").notNull();
        entity.addIntProperty("p6").notNull();
        entity.addIntProperty("p7").notNull();

//        Index indexUnique = new Index();
//        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(providerIdForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }

}
