package com.br.mreza.musicplayer.p2020.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.br.mreza.musicplayer.R;

public abstract class MainFragBaseRecycler_adapter<VH extends MainFragBaseRecycler_adapter.BaseHolder> extends RecyclerView.Adapter<MainFragBaseRecycler_adapter.BaseHolder> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;
    static final int TYPE_FOOT = 2;

    private int itemCount;

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseHolder holder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                holder = new HeaderHolder(layoutInflater.inflate(R.layout.list_header_2020, parent, false), viewType);
                break;
            case TYPE_FOOT:
                holder = new FootHolder(layoutInflater.inflate(R.layout.list_foot_2020, parent, false), viewType);
                break;
            case TYPE_ITEM:
                holder = initiateHolder(layoutInflater.inflate(R.layout.album_list_item_layout_2020, parent, false), viewType);
                break;
        }

        //noinspection ConstantConditions
        return holder;
    }

    protected abstract VH initiateHolder(View view, int viewType);

    protected abstract String getHeaderText();

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        if (position == getItemCount() - 1) return TYPE_FOOT;
        return TYPE_ITEM;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemCount + 2;
    }

    protected abstract void onFootClick();

    protected abstract void onHeaderClick();

    public abstract static class BaseHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public BaseHolder(@NonNull View itemView, int type) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                onClick();
            });
            if (type == TYPE_ITEM) {
                imageView = itemView.findViewById(R.id.item_pic);
                textView = itemView.findViewById(R.id.text_view_item_name);
                imageView.setClipToOutline(true);
            }
        }


        protected abstract void onClick();

        protected abstract void onBind();
    }

    public class HeaderHolder extends BaseHolder {

        HeaderHolder(@NonNull View itemView, int type) {
            super(itemView, type);
            TextView textView = itemView.findViewById(R.id.header_text_view);
            textView.setText(getHeaderText());

            ImageView imageView = itemView.findViewById(R.id.header_image);
            imageView.setClipToOutline(true);
        }


        @Override
        protected void onClick() {
            onHeaderClick();
        }

        @Override
        protected void onBind() {

        }
    }

    public class FootHolder extends BaseHolder {
        FootHolder(@NonNull View itemView, int type) {
            super(itemView, type);
        }

        @Override
        protected void onClick() {
            onFootClick();
        }

        @Override
        protected void onBind() {

        }
    }
}
