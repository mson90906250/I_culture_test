package tw.tcnr01.I_culture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class Product extends AppCompatActivity {
    private ImageView img01;
    private Dialog productDlg;
    private String sqlctl;
    private String shop;
    private String TAG="tcnr21=>";
    private TextView tx6   ,tx9;
    private String uri = "https://tcnr1621.000webhostapp.com/I_culture/shop/ANDROIDTEST.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_layout);

        Intent it =getIntent();
        shop=it.getStringExtra("shop_id");

        sqlctl = "SELECT * FROM shop_product WHERE Shop_ID=\""+shop+"\"  ORDER BY Shop_ID ASC";


        tx6=(TextView)findViewById(R.id.textView6);
        tx9=(TextView)findViewById(R.id.textView9);


        setupViewComponent();
    }

    private void setupViewComponent() {

    img01=(ImageView)findViewById(R.id.product_img01);

        try {
            String result = DBConnector.executeQuery(sqlctl,uri);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // ---
            Log.d(TAG, jsonArray.toString());
            // --
            for (int i = 0; i < 1; i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                // // 取出 jsonObject 中的字段的值的空格
                Iterator itt = jsonData.keys();

                final String Name=jsonData.getString("Product_name");
                final String Introduce=jsonData.getString("Product_msg");
                String URL =jsonData.getString("URL");

//            設置產品
                img01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productDlg = new Dialog(Product.this);
                        productDlg .setCancelable(false);
                        productDlg .setContentView(R.layout.product_dialog);
                        ImageButton btncancel=(ImageButton)productDlg.findViewById(R.id.product_dialog_btncancel);
                        btncancel.setOnClickListener(productbtncancel);
                        tx6.setText(Name);
                        tx9.setText(Introduce);
                        productDlg .show();

                    }
                    private Button.OnClickListener productbtncancel= new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productDlg.cancel();
                        }
                    };
                });

//				tr.setGravity(Gravity.CENTER_HORIZONTAL);
                while (itt.hasNext()) {
                    String key = itt.next().toString();
                    String value = jsonData.getString(key);
                    if (value == null) {
                        continue;
                    } else if ("".equals(value.trim())) {
                        continue;
                    } else {
                        jsonData.put(key, value.trim());
                    }
                    // --
                    Log.d(TAG, "i:" + i + " key:" + key + " value:" + value);
                    // --
                }
            }
        } catch (Exception e) {
            // Log.d(TAG, e.toString());
        }

    }



    private void Mysqlsel(String sqlctl) {
        try {
            String result = DBConnector.executeQuery(sqlctl,uri);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // ---
            Log.d(TAG, jsonArray.toString());
            // --
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                // // 取出 jsonObject 中的字段的值的空格
                Iterator itt = jsonData.keys();
//				tr.setGravity(Gravity.CENTER_HORIZONTAL);
                while (itt.hasNext()) {
                    String key = itt.next().toString();
                    String value = jsonData.getString(key);
                    if (value == null) {
                        continue;
                    } else if ("".equals(value.trim())) {
                        continue;
                    } else {
                        jsonData.put(key, value.trim());
                    }
                    // --
                    Log.d(TAG, "i:" + i + " key:" + key + " value:" + value);
                    // --
                }
            }
        } catch (Exception e) {
            // Log.d(TAG, e.toString());
        }
    }



}
