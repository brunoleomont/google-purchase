package com.monteiro.google_purchase.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.monteiro.google_purchase.Interface.IProductClickListener;
import com.monteiro.google_purchase.MainActivity;
import com.monteiro.google_purchase.R;

import java.util.List;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {

    MainActivity mainActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    public MyProductAdapter(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.mainActivity = mainActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View iteView = LayoutInflater.from(mainActivity.getBaseContext())
                .inflate(R.layout.layout_prodoct_item, viewGroup, false);
        return new MyViewHolder(iteView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.txt_product.setText(skuDetailsList.get(i).getTitle());

        //Product Click
        myViewHolder.setiProductClickListener(new IProductClickListener() {
            @Override
            public void onProductClickListener(View view, int position) {
                //Launch Billing flow
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(i)).build();
                billingClient.launchBillingFlow(mainActivity, billingFlowParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_product;

        IProductClickListener iProductClickListener;

        public IProductClickListener getiProductClickListener() {
            return iProductClickListener;
        }

        public void setiProductClickListener(IProductClickListener iProductClickListener) {
            this.iProductClickListener = iProductClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product = itemView.findViewById(R.id.txt_product_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            iProductClickListener.onProductClickListener(view, getAdapterPosition());
        }
    }
}
