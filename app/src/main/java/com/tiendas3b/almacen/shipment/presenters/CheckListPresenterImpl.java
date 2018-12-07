package com.tiendas3b.almacen.shipment.presenters;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.Answer;
import com.tiendas3b.almacen.db.dao.Form;
import com.tiendas3b.almacen.db.dao.Question;
import com.tiendas3b.almacen.db.dao.ShipmentControl;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.shipment.util.DataBaseUtil;
import com.tiendas3b.almacen.shipment.util.ShipmentConstants;
import com.tiendas3b.almacen.shipment.views.ChecklistView;
import com.tiendas3b.almacen.shipment.views.TripDetailView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class CheckListPresenterImpl implements ChecklistPresenter {

    ChecklistView view;
    private Context context;
    private IDatabaseManager databaseManager;
    private GlobalState mContext;
    private long regionId;

    public CheckListPresenterImpl(Context context, ChecklistView view) {
        this.view = view;
        this.mContext = (GlobalState) context;
        this.regionId = mContext.getRegion();
        this.databaseManager = new DatabaseManager(mContext);
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
    public void generateLog(long tripId, String message) {
        ShipmentControl shipmentControl = databaseManager.findShipmentControlByDate(LocalDate.now().toDate());
        DataBaseUtil.insertLog(databaseManager, message, tripId, this.mContext.getRegion(), this.mContext.getUserId(), null, shipmentControl.getTruckId(), ShipmentConstants.ACTIVITY_REVIEW);
    }
}
