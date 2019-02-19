package tw.tcnr01.I_culture;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Shop02Fragment extends Fragment {

    private Intent intent=new Intent();

    private Integer[] shop={
            R.drawable.shop001,R.drawable.shop002,R.drawable.shop003
    };
    private String[] shop_Array,shop_introduce_array;

    private Integer[] b={
            R.drawable.s1,R.drawable.s2,
            R.drawable.s3,R.drawable.s4,
            R.drawable.s5,R.drawable.s6
    };
    private String TAG="tcnr21=>";
    String sqlctl;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExampleAdapter mAdapter;
    private ArrayList<ExampleItem> mExampleList;

    private String uri = "https://tcnr1621.000webhostapp.com/I_culture/shop/ANDROIDTEST.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        String kind = getArguments().getString("Kind");

        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());
//---------------------------------------------------------------------

        sqlctl = "SELECT * FROM shop WHERE Kind=\""+kind+"\"  ORDER BY Shop_ID ASC";
        Mysqlsel(sqlctl);
        Log.d(TAG,sqlctl);

        shop_Array=getResources().getStringArray(R.array.shop_Array);
        shop_introduce_array=getResources().getStringArray(R.array.shop_introduce_array);

        createExampleList();


        View v = inflater.inflate(R.layout.shop02_fragment,container,false);

        buildRecyclerView(v);


        return v;

    }

    private void buildRecyclerView(View v) {
        mRecyclerView =(RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);//"true"使recyclerview不會因item的多寡而改變大小
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter =  new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //************對mAdaptor設監聽
        mAdapter.setOnItemClickListerner(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position,"Clicked");
            }
        });
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

    private void createExampleList() {
        //************可用巨集做(將從資料庫抓來的資料丟入個別的陣列後,再做巨集,        所以資料的命名最好有規則性  ex:   img01,img02.........)
        mExampleList = new ArrayList<>();

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
                String Name=jsonData.getString("Name");
                String Introduce=jsonData.getString("Introduce");
                String URL =jsonData.getString("URL");
                mExampleList.add(new ExampleItem(b[0],Name,Introduce));
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

                    }Log.d(TAG,jsonData.toString());


                    // --
//                    Log.d(TAG, "i:" + i + " key:" + key + " value:" + value);
                    // --
                }

            }
        } catch (Exception e) {
            // Log.d(TAG, e.toString());
        }



//        for(int i=0;i<b.length;i++){
//            mExampleList.add(new ExampleItem(b[i],shop_Array[i],shop_introduce_array[i]));
//        }

        //*********************************
    }

    //改變item上的data
    private void changeItem(int position,String text){
        intent.setClass(getContext(), Shop03.class);
        intent.putExtra("shop_id",Integer.toString(position+1 ));
        startActivity(intent);
    }


}
