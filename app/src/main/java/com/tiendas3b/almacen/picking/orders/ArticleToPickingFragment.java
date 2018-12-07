package com.tiendas3b.almacen.picking.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.OrderDetail;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import net.gotev.speech.GoogleVoiceTypingDisabledException;
import net.gotev.speech.Speech;
import net.gotev.speech.SpeechDelegate;
import net.gotev.speech.SpeechRecognitionNotAvailable;
import net.gotev.speech.SpeechUtil;
import net.gotev.speech.TextToSpeechCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArticleToPickingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ArticleToPickingFragment extends Fragment implements SpeechDelegate {

    private static final String ARG_SIZE = "size";
    private static final String ARG_POSITION = "pos";
    private static final String ARG_CAPTURE_ID = "capture";
    private static final String ARG_DETAIL = "detail";
    private static final String TAG = "PickingFragment";
    private OnFragmentInteractionListener mListener;
    private OrderDetail item;

    private Unbinder unbinder;
    @BindView(R.id.lblDescription)
    TextView lblDescription;
    @BindView(R.id.txtPosition)
    TextInputEditText txtPosition;
    @BindView(R.id.txtFolio)
    TextInputEditText txtFolio;
    @BindView(R.id.txtPacking)
    TextInputEditText txtPacking;
    @BindView(R.id.txtCircuit)
    TextInputEditText txtCircuit;
    @BindView(R.id.txtExistence)
    TextInputEditText txtExistence;
    @BindView(R.id.txtOrder)
    TextInputEditText txtOrder;
    @BindView(R.id.txtDelivery)
    TextInputEditText txtDelivery;
    @BindView(R.id.txtComments)
    TextInputEditText txtComments;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.spnOptions)
    LabelledSpinner spnOptions;
    private int orderQuantity;
    private boolean fragmentResume = false;
    private boolean fragmentVisible = false;
    private boolean fragmentOnCreated = false;
    private GlobalState mContext;
    private View rootView;
    private int position;
    private int size;
    private int packing;
    private String circuit;
    private String desciption;
    private int cancelRepeat;
    private boolean voiceActive;
    private TextToSpeechCallback callback = new TextToSpeechCallback() {
        @Override
        public void onStart() {
            Log.w(TAG, "TTS onStart");
        }

        @Override
        public void onCompleted() {
            Log.w(TAG, "TTS onCompleted");
            listen();
        }

        @Override
        public void onError() {
            Log.w(TAG, "TTS onError");
        }
    };
    private IDatabaseManager db;
    private long iclave;
//    private TextToSpeech tts;
//    private long orderCaptureId;

    public ArticleToPickingFragment() {
    }

    public static ArticleToPickingFragment newInstance(long orderDetailId, int size, int pos) {
        ArticleToPickingFragment fragment = new ArticleToPickingFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DETAIL, orderDetailId);
        args.putInt(ARG_POSITION, pos);
        args.putInt(ARG_SIZE, size);
        fragment.setArguments(args);
        return fragment;
    }

//    public static ArticleToPickingFragment newInstance(OrderDetail sectionNumber, int size, int pos, long orderCaptureId) {
//        ArticleToPickingFragment fragment = new ArticleToPickingFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_DETAIL, sectionNumber);
//        args.putInt(ARG_POSITION, pos);
//        args.putInt(ARG_SIZE, size);
//        args.putLong(ARG_CAPTURE_ID, orderCaptureId);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_picking_capture, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        if (!fragmentResume && fragmentVisible){   //only when first time fragment is created
        init();
//        }
        return rootView;
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume" + iclave);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause" + iclave);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop" + iclave);
        super.onStop();
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {   // only at fragment screen is resumed
            Log.e(TAG, "visible && isResumed()" + iclave);
            fragmentResume = true;
            fragmentVisible = false;
            fragmentOnCreated = true;
            initTts();

            if (item.getChild() && isFull()) {
                saveInZero();
//                mListener.invalidate();
            }


////            init();
////            uiUpdate();
//            if (size == position + 1) {
////                btnNext.setEnabled(false);
////                mListener.showSend();
//            }
        } else if (visible) {        // only at fragment onCreated
            Log.e(TAG, "visible!" + iclave);
            fragmentResume = false;
            fragmentVisible = true;
            fragmentOnCreated = true;
        } else if (fragmentOnCreated) {// only when you go out of fragment screen
            Log.e(TAG, "fragmentOnCreated!" + iclave);
//        else if(!visible && fragmentOnCreated){// only when you go out of fragment screen
            fragmentVisible = false;
            fragmentResume = false;
        }
    }
//
//    private void uiUpdate() {
//        if(item.getExistence() == 0){
//            showOptions();
//        }
//    }
//
////    @OnClick(R.id.btnOptions)
//    public void showOptions() {
//        spnOptions.getSpinner().performClick();
////        PopupMenu popup = new PopupMenu(getActivity(), btnOptions);
////        popup.getMenuInflater().inflate(R.menu.manu_picking_options, popup.getMenu());
////
////        popup.getMenu().add("Stockout");
////        popup.getMenu().add("Mal empaque");
////        popup.getMenu().add("Otro");
////
////        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
////            public boolean onMenuItemClick(MenuItem item) {
////                Toast.makeText(getActivity(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
////                return true;
////            }
////        });
////
////        popup.show();
//    }

    private void init() {
        mContext = (GlobalState) getActivity().getApplicationContext();
        long orderDetailId = getArguments().getLong(ARG_DETAIL);
        db = DatabaseManager.getInstance(mContext);
        item = db.findOrderDetail(orderDetailId);
        size = getArguments().getInt(ARG_SIZE);
        position = getArguments().getInt(ARG_POSITION);

        voiceActive = mListener.isVoiceActive();

//        orderCaptureId = getArguments().getLong(ARG_CAPTURE_ID);
        VArticle article = item.getArticle();
        if (article == null) {//nuca debería de pasar esto
            lblDescription.setText(String.valueOf(item.getIclave()));
            txtPacking.setText("N/A");
            txtCircuit.setText("N/A");
            Toast.makeText(mContext, "Sincroniza por favor.", Toast.LENGTH_LONG).show();
        } else {
            desciption = article.getDescription();
            iclave = article.getIclave();
            lblDescription.setText(iclave + " - " + desciption + " / " + article.getUnity() + " - " + article.getBarcode());
            packing = article.getPacking();
            txtPacking.setText(String.valueOf(packing));
            circuit = article.getCircuit();
            txtCircuit.setText(circuit);
            String orderQuantityStr = String.valueOf(item.getQuantity() / packing);
            txtOrder.setText(orderQuantityStr);
            orderQuantity = Integer.valueOf(orderQuantityStr);

            if (position == 0) {
                initTts();
            }
        }
        txtPosition.setText((position + 1) + "/" + size);
        txtFolio.setText(String.valueOf(item.getOrder().getPaybill()));
        txtExistence.setText(String.valueOf(item.getExistence()));
//        txtDelivery.setText();
//        if (size == position + 1) {
//            btnNext.setEnabled(false);
//            mListener.showSend();
//        }

        setSpinnerAdapter();

    }

    private void saveInZero() {
        item.setExistence(Integer.valueOf(txtExistence.getText().toString()));
//        packing = item.getArticle().getPacking();
        OrderDetailCapture d = new OrderDetailCapture();
        d.setQuantity(0);
        d.setIclave(item.getIclave());
        d.setType(item.getType());
        d.setChild(item.getChild());
        d.setFatherIclave(item.getFatherIclave());
        d.setPacking(packing);
        d.setOrder(orderQuantity);//tal vez poner en cero...
        spnOptions.getSpinner().setSelection(0);//correcto...
        d.setStatus((String) spnOptions.getSpinner().getSelectedItem());
        d.setArticleId(item.getArticleId());
//        d.setOrderCaptureId(orderCaptureId);
        mListener.moveNextPage(d, item.getOrderId());
    }

    private boolean isFull() {
        List<OrderDetailCapture> children = mListener.findOrderDetailsByFatherIclave(item.getFatherIclave());
        if(children == null){
            return false;
        }
        int sum = 0;
        for (OrderDetailCapture c : children) {
            sum += c.getQuantity();
        }
        if (sum == item.getQuantity()) {
            return true;
        } else {
            return false;
        }
    }

    private void initTts() {
        if (voiceActive) {
//        tts = new TextToSpeech(mContext, status -> {
//            if (status != TextToSpeech.ERROR) {
//                tts.setLanguage(Locale.getDefault());
//                tts.setPitch(0.7F);
//                tts.speak(desciption + ". Localización " + circuit, TextToSpeech.QUEUE_FLUSH, null, circuit);
//            }
//        });
//        Speech.init(mContext, mContext.getPackageName());
//        Logger.setLogLevel(Logger.LogLevel.DEBUG);
            cancelRepeat = 0;
            Speech.getInstance().say(desciption + ". Localización " + circuit + ". Toma " + orderQuantity, callback);
        }
    }

    private void listen() {
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
        } else {
//            RxPermissions.getInstance(this)
//                    .request(Manifest.permission.RECORD_AUDIO)
//                    .subscribe(granted -> {
//                        if (granted) { // Always true pre-M
            onRecordAudioPermissionGranted();
//                        } else {
//                            Toast.makeText(MainActivity.this, R.string.permission_required, Toast.LENGTH_LONG);
//                        }
//                    });
        }
    }

    private void onRecordAudioPermissionGranted() {
        try {
            Speech.getInstance().stopTextToSpeech();
            Speech.getInstance().startListening(this);
            Speech.getInstance().setStopListeningAfterInactivity(6000L);

        } catch (SpeechRecognitionNotAvailable exc) {
            showSpeechNotSupportedDialog();

        } catch (GoogleVoiceTypingDisabledException exc) {
            showEnableGoogleVoiceTyping();
        }
    }

    private void showSpeechNotSupportedDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        SpeechUtil.redirectUserToGoogleAppOnPlayStore(mContext);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Instalar aplicación de google")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, dialogClickListener)
                .setNegativeButton(android.R.string.no, dialogClickListener)
                .show();
    }

    private void showEnableGoogleVoiceTyping() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Habilita reconocimiento de voz")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .show();
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_table, mContext.getResources().getStringArray(R.array.picking_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOptions.setCustomAdapter(adapter);
        spnOptions.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        btnNext.setEnabled(false);
                        break;
                    default:
                        btnNext.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (item.getExistence() == 0) {
            spnOptions.getSpinner().setSelection(1);
        }
    }

//    @OnClick(R.id.btnOptions)
//    public void options(){
//        showOptions();
//    }

    @OnClick(R.id.btnAdd)
    public void add() {
        String quantityStr = txtDelivery.getText().toString();
        int quantity = Integer.valueOf(quantityStr) + 1;
        txtDelivery.setText(String.valueOf(quantity));
        int existence = Integer.valueOf(txtExistence.getText().toString()) - packing;
        txtExistence.setText(String.valueOf(existence));
        if (quantity == orderQuantity) {
            btnAdd.setEnabled(false);
//            Speech.getInstance().shutdown();
//            if(tts !=null){
//                tts.stop();
//                tts.shutdown();
//            }
            if (!"Correcto".equals(spnOptions.getSpinner().getSelectedItem())) {
//            } else {
                spnOptions.getSpinner().setSelection(0);
//                Snackbar.make(rootView, "Opción debe de ser 'Correcto'", Snackbar.LENGTH_LONG).show();
            }
            save();
        } else {
            say("Toma " + (orderQuantity - quantity) + " más");
        }
    }

    private void add(int pickedBoxes) {
        String quantityStr = txtDelivery.getText().toString();
        int quantity = Integer.valueOf(quantityStr) + pickedBoxes;
        if (quantity < orderQuantity) {
            txtDelivery.setText(String.valueOf(quantity));
            int existence = Integer.valueOf(txtExistence.getText().toString()) - packing;
            txtExistence.setText(String.valueOf(existence));
            say("Toma " + (orderQuantity - quantity) + " más");
        } else if (quantity == orderQuantity) {
            txtDelivery.setText(String.valueOf(quantity));
            int existence = Integer.valueOf(txtExistence.getText().toString()) - packing;
            txtExistence.setText(String.valueOf(existence));
            btnAdd.setEnabled(false);
            if (!"Correcto".equals(spnOptions.getSpinner().getSelectedItem())) {
//            } else {
                spnOptions.getSpinner().setSelection(0);
//                Snackbar.make(rootView, "Opción debe de ser 'Correcto'", Snackbar.LENGTH_LONG).show();
            }
            save();
        } else if (quantity > orderQuantity) {
            say("No puedes pickear " + pickedBoxes + ", es más de lo pedido. Intenta otra vez.");
        } else {
            say("Toma " + (orderQuantity - quantity) + " más");
        }
    }

    private void say(String text) {
        if (voiceActive) {
            Speech.getInstance().say(text, callback);
        }
    }


    public void add(@NonNull String barcode) {
        Log.d(TAG, "add:" + barcode);
        if (barcode.equals(item.getArticle().getBarcode())) {
            Log.d(TAG, "add");
            add();
        } else {
            Log.d(TAG, "article not matched");
            final MediaPlayer mp = MediaPlayer.create(mContext, R.raw.error_alert);
            mp.start();
            Snackbar.make(rootView, "Producto escaneado no corresponde a producto por actual.", Snackbar.LENGTH_LONG).show();
        }
//        mListener.requestFocusScan();
    }

    private void save() {
        item.setExistence(Integer.valueOf(txtExistence.getText().toString()));
//        packing = item.getArticle().getPacking();
        OrderDetailCapture d = new OrderDetailCapture();
        d.setQuantity(Integer.parseInt(txtDelivery.getText().toString()) * packing);
        d.setIclave(item.getIclave());
        d.setType(item.getType());
        d.setChild(item.getChild());
        d.setFatherIclave(item.getFatherIclave());
        d.setPacking(packing);
        d.setOrder(orderQuantity);
        d.setStatus((String) spnOptions.getSpinner().getSelectedItem());
        d.setArticleId(item.getArticleId());
//        d.setOrderCaptureId(orderCaptureId);
        mListener.moveNextPage(d, item.getOrderId());
    }


    @OnClick(R.id.btnNext)
    public void next() {
        Log.d(TAG, "next");
        btnNext.setEnabled(false);
        if (item.getExistence() <= 0) {
//            if(!"Correcto".equals(spnOptions.getSpinner().getSelectedItem())){
            save();
//            } else {
//                Snackbar.make(rootView, "Opción no puede ser correcto", Snackbar.LENGTH_LONG).show();
//            }
        } else {
            if ("No apto para venta".equals(spnOptions.getSpinner().getSelectedItem())) {
                save();
            } else {
                if ("Otro".equals(spnOptions.getSpinner().getSelectedItem())) {
                    if (txtComments.getText().length() == 0) {
                        Snackbar.make(rootView, "Faltan comentarios ", Snackbar.LENGTH_LONG).show();
                    } else {
                        save();
                    }
                } else {
                    say("Pedido incompleto");
                    Snackbar.make(rootView, "Pedido incompleto. Si no es el caso, seleccionar alguna opción", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {
    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i(TAG, "onSpeechResult:" + result);
        if (result.isEmpty()) {
            if (cancelRepeat++ < 8) {
                say("Repetir por favor");
            } else {
                Speech.getInstance().say("Desactivando reconocimiento de audio", null);
            }
        } else if ("siguiente".equals(result.toLowerCase())) {
            next();
        } else if (result.matches("[1-9]|1[0-9]|2[0-9]|3[0-9]]")) {
            int pickedBoxes = Integer.parseInt(result);
            Log.i(TAG, "pickedBoxes:" + pickedBoxes);
            add(pickedBoxes);
        } else {
            Speech.getInstance().say("Se detectó:" + result);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void moveNextPage(OrderDetailCapture d, long orderId);

        void requestFocusScan();

        boolean isVoiceActive();

        List<OrderDetailCapture> findOrderDetailsByFatherIclave(int fatherIclave);

        void invalidate();

//        void showSend();
    }
}
