package com.tiendas3b.almacen.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tiendas3b.almacen.R;

import java.util.List;

public class EditReceiptCommentsRVAdapter extends RecyclerView.Adapter<EditReceiptCommentsRVAdapter.ViewHolder>{

    private List<String> commentsList;
    private int layout;
    private OnItemClickListener itemClickListener;

    public EditReceiptCommentsRVAdapter(List<String> commentsList, int layout, OnItemClickListener itemClickListener) {
        this.commentsList = commentsList;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(commentsList.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        EditText comments;

        public ViewHolder(View itemView) {
            super(itemView);
            this.comments = itemView.findViewById(R.id.txtComments);
        }

        public void bind(final String comment, final OnItemClickListener listener){
            this.comments.setText(comment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(comment, getAdapterPosition());
                }
            });
        }

    }

    public interface OnItemClickListener{
        void OnItemClick(String comment, int position);
    }


}
