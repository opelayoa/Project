package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.db.dao.ReceiptSheet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ReceiptSheetTableDataAdapter extends TableDataAdapter<ReceiptSheet> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = new DecimalFormat("#0.00");


    public ReceiptSheetTableDataAdapter(Context context, List<ReceiptSheet> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ReceiptSheet item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getProvider().getId() + item.getProvider().getName());
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getOdc()));
                break;
            case 2:
                String fact = item.getFacturaRef();
                int factLimit = fact.length() < 5 ? fact.length() : 5;
                renderedView = renderString(fact.substring(0, factLimit));
                break;
            case 3:
                String folio = item.getFolio().toString();
                int limit = folio.length() < 5 ? folio.length() : 5;
                renderedView = renderString(folio.substring(0, limit));
                break;
            case 4:
                renderedView = renderString(item.getDateTime());
                break;
            case 5:
                renderedView = renderString(item.getArriveTime());
                break;
            case 6:
                renderedView = renderString(item.getDeliveryTime());
                break;
            case 7:
                renderedView = renderString(item.getDepartureTime());
                break;
//            case 8:
//                renderedView = renderString(item.getArrive() ? "Sí" : "No");
//                break;
//            case 9:
//                renderedView = renderString(item.getNumReceivers().toString());
//                break;
//            case 10:
//                renderedView = renderString(item.getWithoutDate().toString());
//                break;
            case 8:
                Integer pal = item.getPaletizado();
                renderedView = renderString(pal == null ? "-" : pal.toString());
                break;
            case 9:
                renderedView = renderPrice(item.getAmountFact());
                break;
            case 10:
                renderedView = renderPrice(item.getAmountEm());
                break;
//            case 14:
//                renderedView = renderString(item.getRfcProvider());
//                break;
//            case 15:
//                renderedView = renderPrice(item.getIva());
//                break;
//            case 16:
//                renderedView = renderPrice(item.getIeps());
//                break;
            case 11:
                Integer pla = item.getPlatform();
                renderedView = renderString(pla == null ? "-" : pla.toString());
                break;
        }

        return renderedView;
    }

    private View renderPrice(Float s) {
        String priceString = PRICE_FORMATTER.format(s == null ? .0f : s);

        TextView textView = new TextView(getContext());
        textView.setText(priceString);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

//        if (car.getAmountFact() < 50000) {
//            textView.setTextColor(0xFF2E7D32);
//        } else if (car.getAmountFact() > 100000) {
//            textView.setTextColor(0xFFC62828);
//        }

        return textView;
    }

//    private View renderPower(ReceiptSheet car, ViewGroup parentView) {
//        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
////        TextView kwView = (TextView) view.findViewById(R.id.kw_view);
////        TextView psView = (TextView) view.findViewById(R.id.ps_view);
////
////        kwView.setText(car.getKw() + " kW");
////        psView.setText(car.getPs() + " PS");
//
//        return view;
//    }

//    private View renderCatName(ReceiptSheet car) {
//        return renderString(car.getFolio().toString());
//    }

//    private View renderProducerLogo(ReceiptSheet car, ViewGroup parentView) {
////        View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
////        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
////        imageView.setImageResource(car.getProducer().getLogo());
////        return view;
//        return new View(getContext());
//    }

    private View renderString(String value) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

}
