package com.example.mytest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytest.R;
import com.example.mytest.util.EthWalletUtil;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

public class transferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //http请求,不能在主线程使用

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //加载控件
        Button transfer=findViewById(R.id.transfer);
        Button surplus=findViewById(R.id.surplus);
        EditText password_EditText=findViewById(R.id.password_transfer);
        EditText value_EditText=findViewById(R.id.Value);
        EditText Address_EditText=findViewById(R.id.aimAddress);
        TextView surplus_TextView=findViewById(R.id.surplus_textview);

        //点击余额按钮
        surplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示信息
                try {
                    BigInteger suplus=EthWalletUtil.getSurplus();
                    if(surplus!=null)
                        surplus_TextView.setText(suplus.toString()+"Wei");
                } catch (Exception e){
                    //Display an Error
                    System.out.println("Warn!!!getSurplus:"+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        //点击转账按钮
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=Address_EditText.getText().toString();
                String value=value_EditText.getText().toString();
                String password=password_EditText.getText().toString();

                try {
                    TransactionReceipt transactionReceipt=EthWalletUtil.transfer(address,value);
                    if(transactionReceipt==null)
                        Toast.makeText(getApplicationContext(), "交易失败", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(getApplicationContext(), "交易成功", Toast.LENGTH_LONG).show();
                        System.out.println("交易哈希："+transactionReceipt.getTransactionHash());//显示交易哈希
                    }
                }catch (Exception e){
                    //Display an Error
                    System.out.println("Warn!!!transfer:"+e.toString());
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
