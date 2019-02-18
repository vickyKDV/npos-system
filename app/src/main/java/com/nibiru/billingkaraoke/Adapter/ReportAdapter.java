package com.nibiru.billingkaraoke.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.nibiru.billingkaraoke.Model.ReportModel;
import com.nibiru.billingkaraoke.R;
import com.nibiru.billingkaraoke.utils.ImageServiceLoader;
import com.nibiru.billingkaraoke.utils.Server;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private String mHeaderTitle;
    private String mFooterTitle;

    private OnHeaderClickListener mHeaderClickListener;
    private OnFooterClickListener mFooterClickListener;

    private Context mContext;
    private ArrayList<ReportModel> modelList;

    private OnItemClickListener mItemClickListener;


    public ReportAdapter(Context context, ArrayList<ReportModel> modelList, String headerTitle, String footerTitle) {
        this.mContext = context;
        this.modelList = modelList;
        this.mHeaderTitle = headerTitle;
        this.mFooterTitle = footerTitle;
    }

    public void updateList(ArrayList<ReportModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
            return new FooterViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_list, parent, false);
            return new ViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.txtTitleHeader.setText(mHeaderTitle);

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;

            footerHolder.txtFooter.setText(mFooterTitle);

        } else if (holder instanceof ViewHolder) {
            final ReportModel model = getItem(position - 1);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txt_namaproduk.setText(model.getProduct_name());
            genericViewHolder.txt_harganet.setText(model.getHarga_net());
            genericViewHolder.txt_qty.setText(model.getQty());
            genericViewHolder.txt_total.setText(model.getTotal_harga());
            ImageServiceLoader.LoadImage(mContext,Server.imgMenu + model.getImg_produk(),R.drawable.noimg,((ViewHolder) holder).img_produk);


        }
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == modelList.size() + 1;
    }


    @Override
    public int getItemCount() {

        return modelList.size() + 2;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnHeaderClickListener(final OnHeaderClickListener headerClickListener) {
        this.mHeaderClickListener = headerClickListener;
    }

    public void SetOnFooterClickListener(final OnFooterClickListener footerClickListener) {
        this.mFooterClickListener = footerClickListener;
    }

    private ReportModel getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ReportModel model);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View view, String headerTitle);
    }

    public interface OnFooterClickListener {
        void onFooterClick(View view, String headerTitle);
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView txtFooter;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            this.txtFooter = (TextView) itemView.findViewById(R.id.txtFooter);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mFooterClickListener.onFooterClick(itemView, mFooterTitle);
                }
            });

        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleHeader;

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            this.txtTitleHeader = (TextView) itemView.findViewById(R.id.txt_header);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mHeaderClickListener.onHeaderClick(itemView, mHeaderTitle);
                }
            });

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_produk;
        private TextView txt_namaproduk;
        private TextView txt_harganet;
        private TextView txt_qty;
        private TextView txt_total;

        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.img_produk = (ImageView) itemView.findViewById(R.id.img_produk);
            this.txt_namaproduk = (TextView) itemView.findViewById(R.id.txt_namaproduk);
            this.txt_harganet = (TextView) itemView.findViewById(R.id.txt_harganet);
            this.txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);
            this.txt_total = (TextView) itemView.findViewById(R.id.txt_total);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition() - 1));


                }
            });

        }
    }

}

