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

class Organization {

    static Entity addArea(Schema schema, Entity region) {
        Entity entity = schema.addEntity("Area");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(SPINNER_BASE);
        Property id = entity.addLongProperty("areaId").notNull().unique().getProperty();
        entity.addStringProperty("description");
        Property regionIdForPK = entity.addLongProperty("regionId").notNull().getProperty();
        entity.addToOne(region, regionIdForPK, "region");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(regionIdForPK);
//        indexUnique.addProperty(storeIdForPK);
//        indexUnique.addProperty(paybillForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addRack(Schema schema, Entity area) {
        Entity entity = schema.addEntity("Rack");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(SPINNER_BASE);
        Property id = entity.addLongProperty("rackId").notNull().getProperty();
        entity.addStringProperty("name");
        entity.addIntProperty("quantityLevels");
        Property areaIdForPK = entity.addLongProperty("areaId").notNull().getProperty();
        entity.addToOne(area, areaIdForPK, "area");

        ToMany areaToRacks = area.addToMany(entity, areaIdForPK);
        areaToRacks.setName("racks");

        Index indexUnique = new Index();
        indexUnique.addProperty(id);
        indexUnique.addProperty(areaIdForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addRackLevel(Schema schema, Entity rack) {
        Entity entity = schema.addEntity("RackLevel");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(SPINNER_BASE);
        Property id = entity.addLongProperty("levelId").notNull().getProperty();
        entity.addFloatProperty("length");
        entity.addFloatProperty("availableLength");
        Property idForPK = entity.addLongProperty("rackId").notNull().getProperty();
        entity.addToOne(rack, idForPK, "rack");

        ToMany rackToLevels = rack.addToMany(entity, idForPK);
        rackToLevels.setName("levels");

        Index indexUnique = new Index();
        indexUnique.addProperty(id);
        indexUnique.addProperty(idForPK);
        indexUnique.makeUnique();
        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addPosition(Schema schema, Entity level) {
        Entity entity = schema.addEntity("Position");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(SPINNER_BASE);
        Property id = entity.addLongProperty("positionId").notNull().getProperty();
        entity.addStringProperty("name");
        entity.addFloatProperty("length");
        entity.addBooleanProperty("available");
        Property areaIdForPK = entity.addLongProperty("levelId").notNull().getProperty();
        entity.addToOne(level, areaIdForPK, "level");

        ToMany areaToRacks = level.addToMany(entity, areaIdForPK);
        areaToRacks.setName("positions");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(id);
//        indexUnique.addProperty(areaIdForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }

    static Entity addPositionPackage(Schema schema, Entity position, Entity epackage) {
        Entity entity = schema.addEntity("PositionPackage");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
//        Property positionId = entity.addLongProperty("positionId").notNull().getProperty();
//        Property packageId = entity.addLongProperty("packageId").notNull().getProperty();
        entity.addBooleanProperty("status");

        Property areaIdForPK = entity.addLongProperty("positionId").notNull().getProperty();
        entity.addToOne(position, areaIdForPK, "position");

        Property packageIdForPK = entity.addLongProperty("packageId").notNull().getProperty();
        entity.addToOne(epackage, packageIdForPK, "epackage");

//        ToMany areaToRacks = position.addToMany(entity, areaIdForPK);
//        areaToRacks.setName("positions");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(id);
//        indexUnique.addProperty(areaIdForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }


    static Entity addTruckPackage(Schema schema, Entity epackageTruck, Entity epackage) {
        Entity entity = schema.addEntity("TruckPackage");
        entity.addIdProperty().primaryKey();
        entity.setSuperclass(EQUALS_BASE);
//        Property positionId = entity.addLongProperty("positionId").notNull().getProperty();
//        Property packageId = entity.addLongProperty("packageId").notNull().getProperty();
        entity.addIntProperty("status");

        Property truckIdForPK = entity.addLongProperty("truckId").notNull().getProperty();
        entity.addToOne(epackageTruck, truckIdForPK, "epackageTruck");

        Property packageIdForPK = entity.addLongProperty("packageId").notNull().getProperty();
        entity.addToOne(epackage, packageIdForPK, "epackage");

//        ToMany areaToRacks = position.addToMany(entity, areaIdForPK);
//        areaToRacks.setName("positions");

//        Index indexUnique = new Index();
//        indexUnique.addProperty(id);
//        indexUnique.addProperty(areaIdForPK);
//        indexUnique.makeUnique();
//        entity.addIndex(indexUnique);

        return entity;
    }













}
