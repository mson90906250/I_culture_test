package tw.tcnr01.I_culture;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventFragment extends Fragment {
    private Intent intent = new Intent();
    private String TAG="tcnr24=>";
    private String Date,Title,Content;



    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;
    //連接MySQL
    String GET_JSON_DATA_HTTP_URL = "https://tcnr1624.000webhostapp.com/android_mysql_connect/courses.php";
    String JSON_EventDate = "Event_Date";
    String JSON_IMAGE_URL = "image_url";
    String JSON_EventTitle = "Event_Title";
    String JSON_EventContent = "Event_Content";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;
    private SQLiteOpenHelper dbHelper ;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.event_fragment,container,false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        GetDataAdapter1 = new ArrayList<>();
        //JSON_DATA_WEB_CALL();
        buildExampleList();



        return v;

    }

    private void buildExampleList() {


        dbHelper = new DBHelper(getContext());
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("event",null,null,null,null,null,null);
        cursor.moveToFirst();
        do {
            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            GetDataAdapter2.setImageServerUrl(cursor.getString(6));

            GetDataAdapter2.setEventDate(cursor.getString(2));

            GetDataAdapter2.setEventTitle(cursor.getString(5));

            GetDataAdapter2.setEventContent(cursor.getString(7));

            GetDataAdapter1.add(GetDataAdapter2);

        }while (cursor.moveToNext());

        cursor.close();

        buildRecylclerView();

        // db.close();
    }

    private void buildRecylclerView(){

        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, getContext());

        recyclerView.setAdapter(recyclerViewadapter);

        //將recyclerViewadapter設監聽
        recyclerViewadapter.setOnItemClickListerner(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {

                //按下後要執行什麽,自行決定
                //Toast.makeText(getApplicationContext(),"hi "+position,Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                Date =((TextView) itemView.findViewById(R.id.item_date_start)).getText().toString();
                Log.d(TAG,"date = "+Date);
                Title =((TextView) itemView.findViewById(R.id.item_title)).getText().toString();
                Log.d(TAG,"title = "+Title);
                Content=((TextView) itemView.findViewById(R.id.item_content)).getText().toString();
                Log.d(TAG,"654654654654 content = "+Content);




                i.setClass(getContext(),Event02.class);
                i.putExtra("JSON_EventDate",Date);//將Date欄位intent
                i.putExtra("JSON_EventTitle",Title);//將Title欄位intent
                i.putExtra("JSON_EventContent",Content);

                startActivity(i);

            }
        });
    }

    /*public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }
    //取得MySQL資料
    public void JSON_PARSE_DATA_AFTER_WEBCALL(final JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json ;
            try {
                //-----將MYSQL資料轉成json
                json = array.getJSONObject(i);
                Log.d(TAG,json.toString());
                ContentValues newRow = new ContentValues();
                // --(1) 自動取的欄位
                // --取出 jsonObject
                // 每個欄位("key","value")-----------------------
                Iterator itt = json.keys();
                while (itt.hasNext()) {
                    String key = itt.next().toString();
                    String value = json.getString(key); // 取出欄位的值
                    if (value == null) {
                        continue;
                    } else if ("".equals(value.trim())) {
                        continue;
                    } else {
                        json.put(key, value.trim());
                    }
                    // ------------------------------------------------------------------
                    newRow.put(key, value.toString()); // 動態找出有幾個欄位
                    Log.d(TAG,key.toString());
                    Log.d(TAG,value.toString());
                    // -------------------------------------------------------------------
                }
                // ---(2) 使用固定已知欄位---------------------------
                newRow.put("Event_Date", json.getString("Event_Date").toString());
                newRow.put("Event_Title", json.getString("Event_Title").toString());
                newRow.put("Event_Content", json.getString("Event_Content").toString());





                GetDataAdapter2.setImageServerUrl(json.getString(JSON_IMAGE_URL));

                GetDataAdapter2.setEventDate(json.getString(JSON_EventDate));

                GetDataAdapter2.setEventTitle(json.getString(JSON_EventTitle));

                GetDataAdapter2.setEventContent(json.getString(JSON_EventContent));








            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);


        }

        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, getContext());

        recyclerView.setAdapter(recyclerViewadapter);

        //將recyclerViewadapter設監聽
        recyclerViewadapter.setOnItemClickListerner(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {

                //按下後要執行什麽,自行決定
                //Toast.makeText(getApplicationContext(),"hi "+position,Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                Date =((TextView) itemView.findViewById(R.id.item_date_start)).getText().toString();
                Log.d(TAG,"date = "+Date);
                Title =((TextView) itemView.findViewById(R.id.item_title)).getText().toString();
                Log.d(TAG,"title = "+Title);
                Content=((TextView) itemView.findViewById(R.id.item_content)).getText().toString();
                Log.d(TAG,"654654654654 content = "+Content);




                i.setClass(getContext(),Event02.class);
                i.putExtra("JSON_EventDate",Date);//將Date欄位intent
                i.putExtra("JSON_EventTitle",Title);//將Title欄位intent
                i.putExtra("JSON_EventContent",Content);

                startActivity(i);

            }
        });


    }*/
}
