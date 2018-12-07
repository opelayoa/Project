package com.tiendas3b.almacen.receipt.edit;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.Check;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.fragments.BuyDetailFragment;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;
import com.tiendas3b.almacen.util.NumbersUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by dfa on 06/04/2016.
 */
public class EditBuyDetailRecyclerViewAdapter extends RecyclerView.Adapter<EditBuyDetailRecyclerViewAdapter.BuyDetailHolder> {

    private static final String MILES = "%,d";
    private static final String COCIENTE = "%,.1f ";
    private static final String PLL = "pll";
    private final List<BuyDetail> mValues;
    private final Activity mContext;
    private final EditBuyDetailFragment.OnFragmentInteractionListener mListener;
    private final ArrayAdapter<String> paletizadoAdapter;
    private final ArrayAdapter<String> packingStateAdapter;
    private final GenericSpinnerAdapter<ComplaintCat> complaintAdapter;
    private final GenericSpinnerAdapter<Check> checksAdapter;
    private final Locale locale;
    private int selectedPosition = -1;
//    private final BuyDetailFragment.OnFragmentInteractionListener mListener;

    public EditBuyDetailRecyclerViewAdapter(Activity context, List<BuyDetail> items, EditBuyDetailFragment.OnFragmentInteractionListener listener, List<ComplaintCat> complaints, List<Check> checks) {
        mValues = items;
//        Log.i("SIZE:", mValues.size() + "");
        mContext = context;
        mListener = listener;
        locale = Locale.getDefault();

        paletizadoAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item_table, mContext.getResources().getStringArray(R.array.yes_no));
        paletizadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        packingStateAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item_table, mContext.getResources().getStringArray(R.array.good_bad));
        packingStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        complaintAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, complaints);
        complaintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        checksAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, checks);
        checksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public BuyDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_buy_detail, parent, false);
        return new BuyDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(final BuyDetailHolder holder, final int p) {
//        final int position = holder.getAdapterPosition();
//        Log.i("SIZE:", mValues.size() + " p: " + holder.getAdapterPosition());
        holder.open = selectedPosition == holder.getAdapterPosition();
        holder.mItem = mValues.get(holder.getAdapterPosition());

        final Short expiryMax = holder.mItem.getArticle().getExpiryMax();
        holder.txtExpiryDays.setText(String.valueOf(expiryMax));

        mContext.getApplicationContext();

        final int packingInt = holder.mItem.getArticle().getPacking();

        if (holder.mItem.getBuy().getChecked() == Constants.ARRIVE && !holder.txtLote.equals("")) {
//            holder.btnSaveDetail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onSaveDetail(holder);
//                }
//            });


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        selectedPosition = holder.getAdapterPosition();
                        holder.open = true;

                        ReceiptSheetDetailCapture capture = holder.mItem.getReceiptSheetCapture();
                        if (capture == null) {
                            holder.btnSaveDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mListener.onSaveDetail(holder);
                                }
                            });
//                            holder.btnSaveDetail.setOnClickListener(view -> {
//                                mListener.onSaveDetail(holder);
//////                                if (null != mListener) {
////                                    boolean saved = mListener.onSaveDetail(holder);
////                                    if (saved) {
////                                        selectedPosition = -1;
////                                    }
//////                                }
//                            });

                            //        holder.txtOdcBoxes.setOnClickListener(clickListener);
                            addTextWatcher(holder);
                            addTextWatcher2(holder);
//                addSelectWatcher(holder);
//                addTextWatcherSample(holder);
                            setCalendarListener(holder.txtExpiryDate, expiryMax);
                            holder.txtBillAmount.requestFocus();
//                            holder.txtMissingAmount.setText(String.format(locale, MILES, holder.mItem.getBalance()));
                        } else if(capture.getBillAmount() == null) {
                            holder.txtReceivedAmount.setText(NumbersUtil.thousandsSeparator(capture.getReceivedAmount() / packingInt));

                            holder.txtExpiryDate.setText(capture.getExpiryDate());
                            holder.txtLote.setText(capture.getLote());
                            holder.txtPaletizadoCount.setText(NumbersUtil.thousandsSeparator(capture.getPaletizadoCount()));
                            holder.txtBillAmount.setText(NumbersUtil.thousandsSeparator(capture.getReceivedAmount() / packingInt));
                            holder.txtMissingAmount.setText(String.format(locale, MILES, (holder.mItem.getArticleAmount() - capture.getReceivedAmount()) / packingInt));


                            holder.btnSaveDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mListener.onSaveDetail(holder);
                                }
                            });
                            addTextWatcher(holder);
                            addTextWatcher2(holder);
                            setCalendarListener(holder.txtExpiryDate, expiryMax);
                            holder.txtBillAmount.requestFocus();
                        } else {
                            holder.txtReceivedAmount.setText(NumbersUtil.thousandsSeparator(capture.getReceivedAmount() / packingInt));
//                            holder.txtMissingAmount.setText(String.format(locale, MILES, holder.mItem.getReceiptSheetCapture()));
//                            holder.txtMissingAmount.setText(NumbersUtil.thousandsSeparator(capture.get));
                            Integer billAmount = capture.getBillAmount();
                            holder.txtBillAmount.setText(NumbersUtil.thousandsSeparator(billAmount));

                            holder.txtMissingAmount.setText(String.format(locale, MILES, (holder.mItem.getArticleAmount() - capture.getReceivedAmount()) / packingInt));

                            holder.spnPaletizado.getSpinner().setSelection(capture.getPaletizado());
                            holder.txtPaletizadoCount.setText(NumbersUtil.thousandsSeparator(capture.getPaletizadoCount()));
                            holder.spnCheck.getSpinner().setSelection(checksAdapter.getPosition(capture.getCheck()));
                            holder.txtSample.setText(NumbersUtil.thousandsSeparator(capture.getSample()));
                            holder.spnPacking.getSpinner().setSelection(capture.getPacking());
                            holder.spnRejectionReason.getSpinner().setSelection(complaintAdapter.getPosition(capture.getRejectionReason()));
                            holder.txtExpiryDate.setText(capture.getExpiryDate());
                            holder.txtLote.setText(capture.getLote());
                            holder.txtComments.setText(capture.getComments());

                            holder.txtBillAmount.setFocusable(false);
                            holder.txtReceivedAmount.setFocusable(false);
                            holder.txtPaletizadoCount.setFocusable(false);
                            holder.txtSample.setFocusable(false);
                            holder.txtExpiryDate.setFocusable(false);
                            holder.txtLote.setFocusable(false);
                            holder.txtComments.setFocusable(false);
                            holder.spnPaletizado.getSpinner().setEnabled(false);
                            holder.spnCheck.getSpinner().setEnabled(false);
                            holder.spnPacking.getSpinner().setEnabled(false);
                            holder.spnRejectionReason.getSpinner().setEnabled(false);


                            holder.btnSaveDetail.setText("Cerrar");
                            holder.btnSaveDetail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.open = false;
                                    holder.llCaptureSheetDetail.setVisibility(View.GONE);
                                    KeyboardUtil.hide(mContext);
                                    selectedPosition = -1;
                                }
                            });
                        }


                        mListener.onFragmentInteraction(holder, holder.getAdapterPosition());
                        holder.txtBillAmount.requestFocus();
//                    KeyboardUtil.show(mContext, holder.txtBillAmount);
                    }
                }
            };

            holder.mView.setOnClickListener(clickListener);
            holder.txtBarcode.setOnClickListener(clickListener);
            holder.txtOdcAmount.setOnClickListener(clickListener);
//            holder.txtOdcStatus.setOnClickListener(clickListener);
        }

        VArticle article = holder.mItem.getArticle();

        holder.vOwnBrand.setText(String.valueOf(holder.getAdapterPosition() + 1));
        GradientDrawable bg = (GradientDrawable) holder.vOwnBrand.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId()), mContext.getTheme()));
        } else {
            bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId())));
        }
        String bc = article.getBarcode();
        holder.txtBarcode.setText(bc/*.substring(bc.length() - 5, bc.length())*/);
        int pieces = holder.mItem.getArticleAmount();
        holder.txtOdcAmount.setText(String.format(locale, MILES, pieces));
//        String unit = article.getUnit().getAbbreviation();

        final float packing = article.getPacking().floatValue();

//        Float amountUnit = holder.mItem.getArticleAmount() / packing;
        int amountUnit = (int) (pieces / packing);
        holder.txtOdcBuyUnit.setText(String.format(locale, MILES, amountUnit));
        holder.txtPiecesByBox.setText(String.format(locale, MILES, (int) packing));
        holder.txtLocation.setText(article.getCircuit());

//        if(PLL.equals(unit)){
//            holder.txtPaletizadoCount.setText(String.format(locale, MILES, amountUnit.intValue()));
//        }

//        holder.txtOdcBuyUnit.setHint(article.getUnit().getAbbreviation());
//        holder.txtOdcStatus.setText(holder.mItem.getb);
//        holder.txtOdcBoxes.setText(String.format(locale, "%.2f", holder.mItem.getArticleAmount() / holder.mItem.getMsc().floatValue()));
        holder.lblDescription.setText(article.getIclave() + "-" + article.getDescription().concat(" ").concat(article.getUnity()));
        holder.txtMissingAmount.setText(String.format(locale, COCIENTE, holder.mItem.getBalance() / packing));
//        holder.txtSample.setText("0");//TODO AQUI SE COMENTO 0
//        holder.txtBarcode.setOnClickListener(clickListener);
//        holder.txtOdcAmount.setOnClickListener(clickListener);

//        holder.txtOdcStatus.setText(article.getActive().getDescription());
//        holder.txtBillAmount.setText("0");
//        holder.txtReceivedAmount.setText("0");

        holder.spnPaletizado.setCustomAdapter(paletizadoAdapter);
        holder.spnPacking.setCustomAdapter(packingStateAdapter);
        holder.spnRejectionReason.setCustomAdapter(complaintAdapter);
        holder.spnCheck.setCustomAdapter(checksAdapter);


        if (holder.open) {
            holder.llCaptureSheetDetail.setVisibility(View.VISIBLE);
        } else {
            holder.llCaptureSheetDetail.setVisibility(View.GONE);
//            holder.txtBillAmount.removeTextChangedListener();
        }

    }

    private void addTextWatcherSample(final BuyDetailHolder holder) {
        holder.txtSample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString().replace(",", "");
                if (amount.isEmpty() || "0".equals(amount)) {
//                    holder.txtFillRate.setText("0%");
                } else {
                    holder.txtSample.removeTextChangedListener(this);
                    holder.txtSample.setText(String.format(locale, MILES, Integer.parseInt(amount)));
                    holder.txtSample.setSelection(holder.txtSample.getText().toString().length());
                    holder.txtSample.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable view) {
            }
        });

        holder.txtSample.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    KeyboardUtil.hide(mContext);
                    textView.clearFocus();
                    holder.spnCheck.getSpinner().requestFocus();
                    holder.spnCheck.getSpinner().performClick();
                }
                return true;
            }
        });
    }

    private void addSelectWatcher(BuyDetailHolder holder) {
//        holder.spnPaletizado.getSpinner().setNextFocusDownId(R.id.txtSample);
        holder.spnPaletizado.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
//                holder.txtPaletizadoCount.requestFocus();
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
//        holder.spnCheck.getSpinner().setNextFocusDownId(R.id.sp);
    }

    private void addTextWatcher(final BuyDetailHolder holder) {
        final EditText txt = holder.txtBillAmount;
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString().replace(",", "");
                if (amount.isEmpty() || "0".equals(amount) || amount.contains("-")) {
                    holder.txtReceivedAmount.setText("0");
                } else {
                    txt.removeTextChangedListener(this);
//                    holder.txtMissingAmount.setText(getDifference(holder.txtOdcAmount.getText().toString(), amount));
                    String text = String.format(locale, MILES, Integer.parseInt(amount));
                    txt.setText(text);
                    holder.txtReceivedAmount.setText(text);
                    txt.setSelection(txt.getText().toString().length());
                    txt.addTextChangedListener(this);

//                    if (textAndPrint.equals(holder.txtOdcAmount.getText().toString())) {
//                        holder.tilBillAmount.setError(null);
//                    } else {
//                        holder.tilBillAmount.setError("Diferente a ODC");
//                    }

//                    holder.txtEmAmount.removeTextChangedListener(this);
//                    holder.txtFillRate.setText(fillRate(amount, holder.mItem).concat("%"));
//                    holder.txtEmAmount.setText(String.format(locale, MILES, Long.parseLong(amount)));
//                    holder.txtEmAmount.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable view) {
            }
        });
    }

    private void addTextWatcher2(final BuyDetailHolder holder) {
        final EditText txt = holder.txtReceivedAmount;
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String amount = s.toString().replace(",", "");
                if (amount.isEmpty() || "0".equals(amount)) {
                    holder.txtMissingAmount.setText(String.format(locale, MILES, holder.mItem.getBalance() / holder.mItem.getArticle().getPacking()));
                } else {
                    txt.removeTextChangedListener(this);
                    holder.txtMissingAmount.setText(String.format(locale, MILES, getDifference(holder.txtOdcBuyUnit.getText().toString(), amount)));
                    txt.setText(String.format(locale, MILES, Integer.parseInt(amount)));
                    txt.setSelection(txt.getText().toString().length());
                    txt.addTextChangedListener(this);
//                    holder.txtEmAmount.removeTextChangedListener(this);
//                    holder.txtFillRate.setText(fillRate(amount, holder.mItem).concat("%"));
//                    holder.txtEmAmount.setText(String.format(locale, MILES, Long.parseLong(amount)));
//                    holder.txtEmAmount.addTextChangedListener(this);
                }

            }

            @Override
            public void afterTextChanged(Editable view) {
            }
        });

        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    KeyboardUtil.hide(mContext);
                    textView.clearFocus();
                    holder.spnPaletizado.getSpinner().requestFocus();
                    holder.spnPaletizado.getSpinner().performClick();
                }
                return true;
            }
        });
    }

    private int getDifference(String balance, String receivedAmount) {
        int max = Integer.parseInt(balance.replace(",", ""));
        int min = Integer.parseInt(receivedAmount);
        return max - min;
    }

    private void setCalendarListener(final EditText txtExpiryDate, final Short expiryMax) {
        if (expiryMax == 0) {
            txtExpiryDate.setText(DateUtil.getTodayStr());
        } else {
            txtExpiryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    Log.e("adapter", "onFocusChange");
                    if (hasFocus) {
                        if (null != mListener) {
                            mListener.showCalendar(txtExpiryDate, expiryMax);
                        }
                    }
                }
            });
            txtExpiryDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("adapter", "setOnClickListener");
                    if (null != mListener) {
                        mListener.showCalendar(txtExpiryDate, expiryMax);
                    }

//                if (view.hasFocus()) {
//                    showClock(txtExpiryDate);
//                }
                }
            });
        }
    }

//    private final Calendar c = Calendar.getInstance();

//    private void showClock(final EditText txtExpiryDate) {
//////        KeyboardUtil.hide(mContext);
////        DialogUtil.showCalendarTodayMim(mContext, c, (datePicker, year, month, day) -> {
////            txtExpiryDate.setText(DateUtil.getDateStr(year, month, day));
////        });
//    }

    private void collapseAll() {
//        for (int i = mValues.size() - 1; i >= 0; i--) {
//            final Provider model = mValues.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
    }

    private int getColor(Integer type) {
        if (type == 8) {
            return R.color.own_brand;
        }
        return R.color.comercial_brand;
    }

    private String fillRate(String aceptedAmount, BuyDetail mItem) {
//        if (aceptedAmount.isEmpty()) {
//            return "0";
//        }
        return String.format(locale, "%.2f", Integer.valueOf(aceptedAmount) * 100 / mItem.getArticleAmount().floatValue());
    }

//    public void enabledClickListener() {
//        if(holder.mItem.getBuy().getChecked() != Constants.NOT_ARRIVE) {
//            holder.mView.setOnClickListener(clickListener);
//            holder.txtBarcode.setOnClickListener(clickListener);
//            holder.txtOdcAmount.setOnClickListener(clickListener);
//            holder.txtOdcStatus.setOnClickListener(clickListener);
//        }
//    }

    public class BuyDetailHolder extends RecyclerView.ViewHolder {
        public boolean open;
        public final View mView;
        public final LinearLayout llCaptureSheetDetail;
        public final EditText txtBarcode;
        public final EditText txtOdcAmount;
        public final EditText txtOdcBuyUnit;
        public final EditText txtPiecesByBox;
        public final EditText txtExpiryDate;
        public final EditText txtBillAmount;
        public final EditText txtReceivedAmount;
        public final EditText txtMissingAmount;
        public final EditText txtLocation;
        public final EditText txtPaletizadoCount;
        public final EditText txtSample;
        public final EditText txtExpiryDays;
        public final EditText txtLote;
        public final EditText txtComments;
        public final Button btnSaveDetail;
        //        public final EditText txtOdcBoxes;
//        public final EditText txtFillRate;
        public final TextView lblDescription;
        public final TextView vOwnBrand;
        public final LabelledSpinner spnPaletizado;
        public final LabelledSpinner spnPacking;
        public final LabelledSpinner spnRejectionReason;
        public final LabelledSpinner spnCheck;
        public final TextInputLayout tilBillAmount;
        public BuyDetail mItem;

        public BuyDetailHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.llBuyDetail);
            llCaptureSheetDetail = view.findViewById(R.id.capture_sheet_detail);
            vOwnBrand = view.findViewById(R.id.vOwnBrand);
            lblDescription = view.findViewById(R.id.lblDescription);
            txtBarcode = view.findViewById(R.id.txtBarcode);
            txtOdcAmount = view.findViewById(R.id.txtOdcAmount);
            txtOdcBuyUnit = view.findViewById(R.id.txtOdcBuyUnit);
            txtPiecesByBox = view.findViewById(R.id.txtPzsXcaja);
            txtBillAmount = view.findViewById(R.id.txtBillAmount);
            txtReceivedAmount = view.findViewById(R.id.txtReceivedAmount);
            txtMissingAmount = view.findViewById(R.id.txtMissingAmount);
            txtLocation = view.findViewById(R.id.txtLocation);
            spnPaletizado = view.findViewById(R.id.spnPaletizado);
            txtPaletizadoCount = view.findViewById(R.id.txtPaletizadoCount);
            txtSample = view.findViewById(R.id.txtSample);
            spnPacking = view.findViewById(R.id.spnPacking);
            spnCheck = view.findViewById(R.id.spnCheck);
            spnRejectionReason = view.findViewById(R.id.spnRejectionReason);
            txtExpiryDays = view.findViewById(R.id.txtExpiryDays);
            txtExpiryDate = view.findViewById(R.id.txtExpiryDate);
            txtLote = view.findViewById(R.id.txtLote);
            txtComments = view.findViewById(R.id.txtComments);
            btnSaveDetail = view.findViewById(R.id.btnSaveDetail);
            tilBillAmount = view.findViewById(R.id.tilBillAmount);
        }
    }
}
