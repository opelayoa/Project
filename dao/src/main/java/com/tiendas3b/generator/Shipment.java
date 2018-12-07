package com.tiendas3b.generator;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class Shipment {

    public static void addShipment(Schema schema, Entity truck) {
        Entity trip = addTrip(schema, truck);
        Entity tripDetail = addTripDetail(schema, trip);
        Entity question = addQuestion(schema);
        Entity answer = addAnswer(schema);
        Entity questionAnswer = addQuestionAnswer(schema, question, answer);
        Entity form = addForm(schema);
        Entity formQuestion = addFormQuestion(schema, form, question);
        Entity formApplied = addFormApplied(schema, form);
        Entity formAppliedResult = addFormAppliedResult(schema, formApplied, question, answer);
        Entity log = addSheetTrip(schema);
        Entity shipmentControl = addShipmentControl(schema);
    }

    public static Entity addTrip(Schema schema, Entity truck) {
        Entity trip = schema.addEntity("Trip");
        Property routeIdForPK = trip.addIdProperty().primaryKey().getProperty();
        trip.addLongProperty("sequence").notNull();
        Property regionIdForPK = trip.addLongProperty("regionId").notNull().getProperty();
        Property truckIdForPK = trip.addLongProperty("truckId").notNull().getProperty();

        trip.addToOne(truck, truckIdForPK);
        truck.addToMany(trip, truckIdForPK);


        Property dateForPK = trip.addDateProperty("date").notNull().getProperty();
        trip.addLongProperty("routeTypeId");
        trip.addDoubleProperty("linearDistance");
        trip.addDoubleProperty("routeDistance");
        trip.addDoubleProperty("timeDistance");

        trip.addIntProperty("status").notNull();

        Index index = new Index();
        index.addProperty(routeIdForPK);
        index.addProperty(regionIdForPK);
        index.addProperty(truckIdForPK);
        index.addProperty(dateForPK);

        trip.addIndex(index);

        return trip;
    }

    public static Entity addTripDetail(Schema schema, Entity trip) {
        Entity tripDetail = schema.addEntity("TripDetail");
        tripDetail.addIdProperty();
        Property tripId = tripDetail.addLongProperty("tripId").notNull().getProperty();

        tripDetail.addLongProperty("sequence").notNull();
        tripDetail.addLongProperty("storeId");
        tripDetail.addStringProperty("storeDescription");
        tripDetail.addLongProperty("palletsNumber");
        tripDetail.addIntProperty("uploadedPalletCounter").notNull();
        tripDetail.addIntProperty("deliveredPalletCounter").notNull();
        tripDetail.addIntProperty("collectedPalletTotal").notNull();
        tripDetail.addIntProperty("returnedPalletTotal").notNull();

        tripDetail.addIntProperty("status").notNull();

        tripDetail.addToOne(trip, tripId);
        trip.addToMany(tripDetail, tripId);

        return tripDetail;
    }


    public static Entity addQuestion(Schema schema) {
        Entity question = schema.addEntity("Question");
        question.addIdProperty().primaryKey().notNull();
        question.addStringProperty("description");

        return question;
    }

    public static Entity addAnswer(Schema schema) {
        Entity answer = schema.addEntity("Answer");
        answer.addIdProperty().primaryKey();
        answer.addStringProperty("description");

        return answer;
    }

    public static Entity addQuestionAnswer(Schema schema, Entity question, Entity answer) {
        Entity questionAnswer = schema.addEntity("QuestionAnswer");
        Property questionId = questionAnswer.addLongProperty("questionId").notNull().getProperty();
        Property answerId = questionAnswer.addLongProperty("answerId").notNull().getProperty();

        Index indexQuestionAnswer = new Index();
        indexQuestionAnswer.addProperty(questionId);
        indexQuestionAnswer.addProperty(answerId);

        questionAnswer.addIndex(indexQuestionAnswer);

        question.addToMany(answer, questionAnswer, questionId, answerId);

        return questionAnswer;
    }

    public static Entity addForm(Schema schema) {
        Entity form = schema.addEntity("Form");
        form.addIdProperty().notNull().primaryKey();
        form.addStringProperty("description").notNull();
        return form;
    }

    public static Entity addFormQuestion(Schema schema, Entity form, Entity question) {
        Entity formQuestion = schema.addEntity("FormQuestion");
        Property formId = formQuestion.addLongProperty("formId").notNull().getProperty();
        Property questionId = formQuestion.addLongProperty("questionId").notNull().getProperty();

        Index index = new Index();
        index.addProperty(formId);
        index.addProperty(questionId);

        formQuestion.addIndex(index);

        form.addToMany(question, formQuestion, formId, questionId);

        return formQuestion;
    }

    public static Entity addFormApplied(Schema schema, Entity form) {
        Entity formApplied = schema.addEntity("FormApplied");
        formApplied.addIdProperty().notNull().primaryKey().autoincrement();
        Property formId = formApplied.addLongProperty("formId").getProperty();
        formApplied.addLongProperty("userId").notNull();
        formApplied.addDateProperty("date");
        formApplied.addStringProperty("entityType").notNull();
        formApplied.addLongProperty("entityId").notNull();

        formApplied.addToOne(form, formId);

        return formApplied;
    }

    public static Entity addFormAppliedResult(Schema schema, Entity formApplied, Entity question, Entity answer) {
        Entity formAppliedResult = schema.addEntity("FormAppliedResult");

        Property formAppliedId = formAppliedResult.addLongProperty("formAppliedId").notNull().getProperty();
        Property questionId = formAppliedResult.addLongProperty("questionId").notNull().getProperty();
        Property answerId = formAppliedResult.addLongProperty("answerId").notNull().getProperty();

        formApplied.addToMany(formAppliedResult, formAppliedId);

        return formAppliedResult;
    }

    public static Entity addSheetTrip(Schema schema) {
        Entity sheetTrip = schema.addEntity("SheetTrip");
        sheetTrip.addIdProperty().primaryKey().autoincrement();
        sheetTrip.addDateProperty("date").notNull();
        sheetTrip.addStringProperty("description").notNull();
        sheetTrip.addLongProperty("tripId").notNull();
        sheetTrip.addLongProperty("truckId").notNull();
        sheetTrip.addLongProperty("storeId");
        sheetTrip.addDoubleProperty("latitude");
        sheetTrip.addDoubleProperty("longitude");
        sheetTrip.addLongProperty("activityId");
        sheetTrip.addLongProperty("userId").notNull();
        sheetTrip.addLongProperty("regionId").notNull();

        return sheetTrip;
    }

    public static Entity addShipmentControl(Schema schema) {
        Entity shipmentControl = schema.addEntity("ShipmentControl");
        shipmentControl.addIdProperty().primaryKey().autoincrement();
        shipmentControl.addDateProperty("date");
        shipmentControl.addIntProperty("status");
        shipmentControl.addLongProperty("truckId");

        return shipmentControl;
    }
}
