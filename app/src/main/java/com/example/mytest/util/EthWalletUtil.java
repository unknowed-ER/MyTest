package com.example.mytest.util;

import android.widget.Toast;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.Bip44WalletUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EthWalletUtil {

    static private Bip39Wallet wallet;
    static private String password="";
    static private Credentials credentials;
    static private Web3j web3;
    static private String walletPath="/data/user/0/com.example.mytest/files/wallet";

    //连接以太坊网络
    public static void getHttp() throws Exception{
        EthWalletUtil.web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/78254100b01a47619840e65fe52e8d04"));//连接以太坊地址，当前为测试网络
        Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
        if (!clientVersion.hasError()) {
            //Connected
            System.out.println("connect Http:"+clientVersion.toString());
        } else {
            //Show Error
            System.out.println("connect Http False");
        }
    }

    //创建Bip44钱包
    //为了方便与其他钱包的导入导出，密码为空
    //String walletPath = getFilesDir().getAbsolutePath()+"/wallet";
    public static Bip39Wallet createWallet() throws Exception{
        //检测钱包路径
        File walletDir  = new File(walletPath);
        if(!walletDir.exists()){
            walletDir.mkdirs();
            walletDir  = new File(walletPath);
        }
        //创建钱包
        wallet =  Bip44WalletUtils.generateBip44Wallet(password,walletDir);
        //用密码和助记词解锁账户
        credentials= WalletUtils.loadBip39Credentials(password, wallet.getMnemonic());
        //显示信息
        System.out.println("WalletName:"+wallet.getFilename());
        System.out.println("Wallet PublicKey:"+getPublicKey());
        return wallet;
    }

    //获得当前钱包助记词
    public static String getMnemonic(){
        return wallet.getMnemonic();
    }

    //获得当前钱包私钥
    public static String getPrivateKey(){
        return credentials.getEcKeyPair().getPrivateKey().toString(16);
    }

    //获得当前钱包地址
    public  static String getAddress(){
        return credentials.getAddress();
    }

    //获得当前钱包公钥
    public  static String getPublicKey(){
        return credentials.getEcKeyPair().getPublicKey().toString(16);
    }

    //获得当前账户余额，单位ether
    public static BigInteger getSurplus() throws Exception {
        System.out.println("Address:"+EthWalletUtil.getAddress());
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
        EthGetBalance ethGetBalance = web3.ethGetBalance(EthWalletUtil.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger wei = ethGetBalance.getBalance();
        return wei;
    }

    //通过转入地址和转账金额进行转账
    public static TransactionReceipt transfer(String address, String value)throws Exception{
        //账户余额不足
        if(new BigDecimal(getSurplus()).subtract(BigDecimal.valueOf(Double.valueOf(value))).compareTo(new BigDecimal("0")) <= 0)
            return null;
        //地址非法
        if(!WalletUtils.isValidAddress(address))
            return null;
        TransactionReceipt transactionReceipt = Transfer.sendFunds(web3, credentials, address, BigDecimal.valueOf(Double.valueOf(value)), Convert.Unit.ETHER).send();
        return  transactionReceipt;
    }

    //通过助记词生成钱包
    public static Bip39Wallet leadWalletByMnemonic(String Mnemonic) throws Exception{
        //参数非法
        if(Mnemonic==null)
            return null;
        String[] split=Mnemonic.split(" ");
        List<String> list= Arrays.asList(split);
        if(list.size()!=12)
            return null;
        //检测钱包路径
        File walletDir  = new File(walletPath);
        if(!walletDir.exists()){
            walletDir.mkdirs();
            walletDir  = new File(walletPath);
        }
        credentials=Bip44WalletUtils.loadBip39Credentials(password,Mnemonic);
        String walletFile = WalletUtils.generateWalletFile(password, credentials.getEcKeyPair(), walletDir, true);

        wallet = new Bip39Wallet(walletFile, Mnemonic);
        System.out.println("Lead:"+getMnemonic());
        return wallet;
     }

    //通过私钥生成钱包
    //31e01ef070fa38e998612edf9b67bc507e6cf52bc0d36258f3e6ca10f4199682
    public static Bip39Wallet leadWalletByPrivateKey(String privateKey)throws Exception{
        //参数非法
        if(!WalletUtils.isValidPrivateKey(privateKey))
            return null;
        //检测钱包路径
        File walletDir  = new File(walletPath);
        if(!walletDir.exists()){
            walletDir.mkdirs();
            walletDir  = new File(walletPath);
        }
        BigInteger bigInteger=new BigInteger(privateKey,16);
        ECKeyPair ecKeyPair=ECKeyPair.create(bigInteger);
        System.out.println("ECKeyPair"+ecKeyPair.toString());
        String walletName=WalletUtils.generateWalletFile(password,ecKeyPair,walletDir,false);
        System.out.println("FileName:"+walletName);
        wallet= new Bip39Wallet(walletName,null);
        credentials=WalletUtils.loadCredentials(password,walletPath+"/"+walletName);
        return wallet;
    }
}
