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

public class leadWalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_wallet);

        EditText input=findViewById(R.id.lead_input);
        Button byMnemonic=findViewById(R.id.leadByMnemonic);
        Button byPrivateKey=findViewById(R.id.leadByPrivateKey);
        TextView address_TextView=findViewById(R.id.lead_Address);

        byMnemonic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mnemonic=input.getText().toString();
                //创建钱包
                try {
                    Bip39Wallet wallet = EthWalletUtil.leadWalletByMnemonic(Mnemonic);
                    if(wallet==null){
                        //非法输入
                        Toast.makeText(getApplicationContext(), "助记词输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                    }else{
                        //生成成功,显示信息
                        address_TextView.setText("生成私钥："+EthWalletUtil.getPrivateKey());
                    }
                } catch (Exception e){
                    //Display an Error
                    System.out.println("Warn!!!createWallet:"+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        byPrivateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String privateKey=input.getText().toString();
                System.out.println(privateKey);
                try{
                    Bip39Wallet wallet=EthWalletUtil.leadWalletByPrivateKey(privateKey);
                    if(wallet==null){
                        //私钥为空
                        Toast.makeText(getApplicationContext(), "私钥不正确", Toast.LENGTH_SHORT).show();
                    }else{
                        //生成成功,显示信息
                        Toast.makeText(getApplicationContext(), "导入成功", Toast.LENGTH_SHORT).show();
                        address_TextView.setText("生成私钥："+EthWalletUtil.getPrivateKey());
                    }
                }catch (Exception e){
                    //Display an Error
                    System.out.println("Warn!!!leadWalletByPrivateKey:"+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
