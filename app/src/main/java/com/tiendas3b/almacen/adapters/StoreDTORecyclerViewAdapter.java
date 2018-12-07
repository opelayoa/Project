package com.tiendas3b.almacen.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.dto.StoreDTO;
import com.tiendas3b.almacen.fragments.StoreFragment.OnListFragmentInteractionListener;
//import com.tiendas3b.almacen.fragments.dummy.DummyContent.DummyItem;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class StoreDTORecyclerViewAdapter extends RecyclerView.Adapter<StoreDTORecyclerViewAdapter.ViewHolder> implements  ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_CALL_PHONE = 1;
    private final List<StoreDTO> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Activity mContext;

    public StoreDTORecyclerViewAdapter(Activity context, List<StoreDTO> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext/*parent.getContext()*/).inflate(R.layout.fragment_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.lblStoreName.setText(mValues.get(position).getName());
        holder.lblRegion.setText(mValues.get(position).getRegionName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
//                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = 40.714728;
                double longitude = -73.998672;
                String label = "ABC Label";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                v.getContext().startActivity(intent);
            }
        });

        holder.btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "Obtener_mail@de.tienda");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Remplazar por asunto en strings");
                intent.putExtra(Intent.EXTRA_TEXT, "Texto default");
                context.startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(v);
            }

        });
    }

    private void call(View v) {
        final Activity context = (Activity) v.getContext();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:014737351058"));//TODO cambiar por número de la tienda
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            context.startActivity(callIntent);
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(callIntent);
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, CALL_PHONE)) {
            Snackbar.make(v, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(context, new String[]{CALL_PHONE}, REQUEST_CALL_PHONE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(context, new String[]{CALL_PHONE}, REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView lblStoreName;
        public final TextView lblRegion;
        public final TextView lblVintage;
        public final TextView lblDays;
        public final ImageButton btnLocation;
        public final ImageButton btnMail;
        public final ImageButton btnCall;
        public StoreDTO mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            lblStoreName = (TextView) view.findViewById(R.id.lblStoreName);
            lblRegion = (TextView) view.findViewById(R.id.lblRegion);
            lblVintage = (TextView) view.findViewById(R.id.lblVintage);
            lblDays = (TextView) view.findViewById(R.id.lblDays);
            btnLocation = (ImageButton) view.findViewById(R.id.btnLocation);
            btnMail = (ImageButton) view.findViewById(R.id.btnMail);
            btnCall = (ImageButton) view.findViewById(R.id.btnCall);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + lblRegion.getText() + "'";
        }
    }
}
