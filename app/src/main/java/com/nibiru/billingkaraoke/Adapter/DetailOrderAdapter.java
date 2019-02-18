package com.nibiru.billingkaraoke.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nibiru.billingkaraoke.Model.DetailOrderModel;
import com.nibiru.billingkaraoke.R;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class DetailOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DetailOrderModel> modelList;

    private OnItemClickListener mItemClickListener;


    public DetailOrderAdapter(Context context, ArrayList<DetailOrderModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<DetailOrderModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_cart_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final DetailOrderModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

//        private String trx_cid;
//        private String product_cid;
//        private AutofitTextView product_name;
//        private TextView qty;
//        private String harga;
//        private String diskon;
//        private String harga_net;
//        private String total_harga;


            genericViewHolder.product_name.setText(model.getProduct_name());
            genericViewHolder.qty.setText(model.getQty());
            genericViewHolder.trx_cid = model.getTrx_cid();
            genericViewHolder.product_cid = model.getProduct_cid();
            genericViewHolder.harga = model.getHarga();
            genericViewHolder.diskon = model.getDiskon();
            genericViewHolder.harga_net = model.getHarga_net();
            genericViewHolder.total_harga = model.getTotal_harga();

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private DetailOrderModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, DetailOrderModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        private String trx_cid;
        private String product_cid;
        private AutofitTextView product_name;
        private TextView qty;
        private String harga;
        private String diskon;
        private String harga_net;
        private String total_harga;





        public ViewHolder(final View itemView) {
            super(itemView);
            this.trx_cid        = "";
            this.product_cid    = "";
            this.product_name   =  itemView.findViewById(R.id.txtnama_menudetailorder);
            this.qty            =  itemView.findViewById(R.id.txt_qtydetailorder);
            this.harga          = "";
            this.diskon         = "";
            this.harga_net      = "";
            this.total_harga    = "";

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
//
//
//                }
//            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                    return false;
                }
            });

        }
    }

}

