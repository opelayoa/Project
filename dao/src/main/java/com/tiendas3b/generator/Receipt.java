package com.tiendas3b.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

import static com.tiendas3b.generator.MyDaoGenerator.SPINNER_BASE;

/**
 * Created by danflo on 03/03/2017 madafaka.
 */

class Receipt {

    static Entity addOrderType(Schema schema, Entity region) {
        Entity entity = schema.addEntity("OrderType");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(SPINNER_BASE);
        Property typeId = entity.addIntProperty("typeId").notNull().unique().getProperty();
        entity.addStringProperty("description");
        entity.addStringProperty("alias");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
//        entity.addToOne(region, regionIdForPK, "region");
        Property circuitId = entity.addIntProperty("circuitId").getProperty();
        entity.addBooleanProperty("separate");
        entity.addBooleanProperty("active");
        entity.addBooleanProperty("scannerAvailable");
        entity.addBooleanProperty("mobileAvailable");

        Index indexUnique = new Index();
        indexUnique.addProperty(regionIdForPK);
        indexUnique.addProperty(circuitId);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }














}
