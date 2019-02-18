package com.nibiru.billingkaraoke.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.nibiru.billingkaraoke.Model.MenuModel;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.Server;
import com.nibiru.billingkaraoke.utils.ImageServiceLoader;

import net.cpacm.moneyview.MoneyTextView;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<MenuModel> modelList;

    private OnItemClickListener mItemClickListener;


    public MenuAdapter(Context context, ArrayList<MenuModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<MenuModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_listmenu, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final MenuModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txt_namamenu.setText(model.getProduct_name());
            genericViewHolder.txt_hargajual.setText(model.getProduct_harga());
            genericViewHolder.txt_diskon.setText("Disc : "+model.getProduct_diskon()+"%");
            genericViewHolder.txt_harganet.setText(model.getHarga_net());
            genericViewHolder.cid = model.getCid();
            ImageServiceLoader.LoadImage(mContext,Server.imgMenu + model.getProduct_image(),R.drawable.noimg,((ViewHolder) holder).imgUser);
        if (model.getProduct_diskon().matches("0")){
            genericViewHolder.txt_hargajual.setVisibility(View.GONE);
            genericViewHolder.txt_diskon.setVisibility(View.GONE);
        }else{
            genericViewHolder.txt_hargajual.setVisibility(View.VISIBLE);
            genericViewHolder.txt_diskon.setVisibility(View.VISIBLE);
        }

        }

    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private MenuModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, MenuModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView txt_namamenu,txt_diskon;
        private MoneyTextView txt_hargajual,txt_harganet;
        public  String stock;
        public  String cid;
        public  String idnya;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.imgUser        =  itemView.findViewById(R.id.img_user);
            this.txt_namamenu   =  itemView.findViewById(R.id.txt_namamenu);
            this.txt_hargajual  =  itemView.findViewById(R.id.txt_hargajual);
            this.txt_harganet   =  itemView.findViewById(R.id.txt_harganet);
            this.txt_diskon     =  itemView.findViewById(R.id.txt_diskon);
            this.stock          = "";
            this.cid            = "";

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

}

