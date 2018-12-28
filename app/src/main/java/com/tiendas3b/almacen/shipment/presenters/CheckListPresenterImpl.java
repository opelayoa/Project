package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;
import android.location.Location;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.Question;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.ChecklistView;

import org.joda.time.LocalDate;

import java.util.List;

public class CheckListPresenterImpl implements ChecklistPresenter {

    ChecklistView view;
    private IDatabaseManager databaseManager;
    private GlobalState context;
    private long regionId;

    private long tripId;
    private String message;
    private Double odometer;

    public CheckListPresenterImpl(Context context, ChecklistView view) {
        this.view = view;
        this.context = (GlobalState) context;
        this.regionId = this.context.getRegion();
        this.databaseManager = new DatabaseManager(context);
    }


    @Override
    public void getForm() {

        List<Form> form = databaseManager.listAll(Form.class);
        System.out.println(form);

        List<Question> questions = this.databaseManager.listAll(Question.class);
        System.out.println(questions);

        view.showCheckList(this.databaseManager.findFormById(1));
    }

    @Override
    public void generateLog(long tripId, String message, Double odometer, Location location) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, message, tripId, context.getRegion(), context.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_REVIEW, odometer, location);
    }
}
