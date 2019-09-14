package com.monteiro.google_purchase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.monteiro.google_purchase.Adapter.MyProductAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;

    Button loadProduct;
    RecyclerView recyclerProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBillingClient();

        loadProduct = findViewById(R.id.btn_load_product);
        recyclerProduct = findViewById(R.id.recycler_product);
        recyclerProduct.setHasFixedSize(true);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));

        loadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(billingClient.isReady()){
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("purchase_100_coins", "purchase_500_coins"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();

                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                loadProductToRecyclerView(skuDetailsList);
                            } else {
                                Toast.makeText(MainActivity.this, "Cannot query product", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "Billing client not ready.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductToRecyclerView(List<SkuDetails> skuDetailsList) {
        MyProductAdapter adapter = new MyProductAdapter(this, skuDetailsList, billingClient);
        recyclerProduct.setAdapter(adapter);
    }

    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    Toast.makeText(MainActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, ""+billingResult.getResponseCode(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Toast.makeText(MainActivity.this, "Purchase items:"+purchases.size(), Toast.LENGTH_LONG).show();
    }
}
