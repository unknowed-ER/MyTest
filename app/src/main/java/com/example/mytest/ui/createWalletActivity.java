package com.example.mytest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.util.EthWalletUtil;

import org.web3j.crypto.Bip39Wallet;

public class createWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        //加载控件
        Button getWallet=findViewById(R.id.getWallet);
        TextView Mnemonic_TextView=findViewById(R.id.Mnemonic);
        TextView PrivateKey_TextView=findViewById(R.id.PrivateKey);
        TextView PublicKey_TextView=findViewById(R.id.PublicKey);
        TextView Address_TextView=findViewById(R.id.Address);

        getWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建钱包
                try {
                    Bip39Wallet wallet = EthWalletUtil.createWallet();
                } catch (Exception e){
                    //Display an Error
                    System.out.println("Warn!!!createWallet:"+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
                //显示信息
                Mnemonic_TextView.setText("助记词："+EthWalletUtil.getMnemonic());
                PrivateKey_TextView.setText("私钥："+EthWalletUtil.getPrivateKey());
                PublicKey_TextView.setText("公钥："+EthWalletUtil.getPublicKey());
                Address_TextView.setText("地址："+EthWalletUtil.getAddress());
            }
        });

    }
}
