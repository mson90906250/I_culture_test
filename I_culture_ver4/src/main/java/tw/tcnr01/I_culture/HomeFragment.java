package tw.tcnr01.I_culture;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Integer[] imgArrs = {
            R.drawable.home_city1, R.drawable.home_city2,
            R.drawable.home_city3, R.drawable.home_city4,
            R.drawable.home_city5, R.drawable.home_city6,
    };

    private  Integer [] images = {R.drawable.home_slider1, R.drawable.home_slider2,
                                                             R.drawable.home_slider3, R.drawable.home_slider4,
                                                             R.drawable.home_slider5};

    private ListView list001;
    private ArrayAdapter<String> adapter1;
    private ArrayList<Map<String, Object>> mList;
    private TextView t_title;
    private TableRow tab01;
    private String check_t="臺中市",SiteName_t="西屯";

    private GridView gridview;
    ViewPager viewPager;
    SliderViewPagerAdapter adapter;
    LinearLayout sliderDots;
    private int dotCounts;
    private ImageView[] dots;

    List<GetDataAdapter> GetDataAdapter1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;
    //連接MySQL
    String GET_JSON_DATA_HTTP_URL = "https://tcnr1624.000webhostapp.com/android_mysql_connect/courses.php";
    String JSON_EventDate = "Event_Date";
    String JSON_IMAGE_URL = "image_url";
    String Json_EventTitle = "Event_Title";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    private TextView t005;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.home_fragment,container,false);
        gridview = (GridView)v. findViewById(R.id.m0703_g001);

        //設置gridview大小

        //為gridview設置監聽器
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //設置一個彈窗的吐司，顯示被短擊的條目
//                Toast.makeText(getContext(),"點選位置:"+position,Toast.LENGTH_SHORT).show();
                Intent it = new Intent();
                it.setClass(getContext(),Shop03.class);
                it.putExtra("shop_id",Integer.toString(position+1));
                startActivity(it);
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //設定一個彈窗Toast,顯示被短點選的條目
                Toast.makeText(getContext(),"長按位置:"+position,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        setupViewComponent(v);

        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView)v. findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(recyclerViewlayoutManager);


        JSON_DATA_WEB_CALL();

        mRecyclerView =(RecyclerView) v. findViewById(R.id.recyclerview1);
        mRecyclerView.setHasFixedSize(true);//"true"使recyclerview不會因item的多寡而改變大小
        mLayoutManager = new LinearLayoutManager(getContext());


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //將縮圖填入 GridView
        setGridView();

        //----------------------幻燈片----------------------
        viewPager =(ViewPager) v. findViewById(R.id.viewPager);
        adapter = new SliderViewPagerAdapter(getContext(),images);
        viewPager.setAdapter(adapter);
        sliderDots = (LinearLayout) v.findViewById(R.id.SliderDots);
        dotCounts = adapter.getCount();
        dots = new ImageView[dotCounts];
        for(int i=0;i<dotCounts;i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.slidershow_nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 4, 16, 0);
            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.slidershow_dot));
        //----------------addOnPageChangeListener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0;i<dotCounts;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.slidershow_nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.slidershow_dot));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),3000,6000);

        return v;

    }

    private void setupViewComponent(View v) {
        list001 = (ListView)v. findViewById(R.id.listView1);
        //按鈕監聽
        t005 = (TextView)v.findViewById(R.id.home_t005);
        t005.setOnClickListener(More);
        //----------------------------------------
        try {
            String Task_opendata = new TransTask().execute("http://opendata.epa.gov.tw/ws/Data/AQI/?$format=json").get();
            List<Map<String, Object>> mList;
            mList = new ArrayList<Map<String, Object>>();
            // 解析 json
            JSONArray jsonArray = new JSONArray(Task_opendata);
            //------JSON 排序-
            jsonArray = sortJsonArray(jsonArray);
            //jsonArray=sortJsonArray( jsonArray);
            //-----開始逐筆轉換-----
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Map<String, Object> item = new HashMap<String, Object>();
                String SiteName = jsonData.getString("SiteName");
                String AQI = jsonData.getString("AQI");
                if (SiteName.equals(SiteName_t)) {
                    item.put("SiteName", SiteName);
                    item.put("AQI", AQI);
                    mList.add(item);
                } else {
                    //check_t = County;
                };
            }
            //=========設定listview========
            SimpleAdapter adapter1 = new SimpleAdapter(
                    getContext(),
                    mList,
                    R.layout.home_list,
                    new String[]{"SiteName", "AQI"},
                    new int[]{R.id.t002, R.id.t004}
            );
            list001.setAdapter(adapter1);
            //設定list內的AQI的文字顏色和背景顏色
            adapter1.setViewBinder(new SimpleAdapter.ViewBinder() {

                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view.getId() == R.id.t004) {
                        //PM2.5
                        TextView AQI = (TextView) view;
                        AQI.setText(data.toString());
                        if (checkIfDataIsInt(data.toString())) {
                            int AQInum = Integer.parseInt(data.toString());
                            //-判斷細懸浮微粒(PM2.5)指標對照
                            if (AQInum < 50) {
                                AQI.setTextColor(getResources().getColor(R.color.Black));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Lime1));
                            } else if(AQInum >= 50 &&AQInum < 100) {
                                AQI.setTextColor(getResources().getColor(R.color.Black));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Yellow));
                            } else if (AQInum >= 101 &&AQInum < 150) {
                                AQI.setTextColor(getResources().getColor(R.color.Black));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Orange));
                            } else if (AQInum >= 151 && AQInum <= 200) {
                                AQI.setTextColor(getResources().getColor(R.color.Black));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Red));
                            } else if (AQInum >= 201 && AQInum <= 300) {
                                AQI.setTextColor(getResources().getColor(R.color.Blue));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Purple));
                            } else if (AQInum > 300) {
                                AQI.setTextColor(getResources().getColor(R.color.Red));
                                AQI.setBackgroundColor(getResources().getColor(R.color.Maroon));
                            } else {
                                AQI.setTextColor(getResources().getColor(R.color.Black));
                                AQI.setBackgroundColor(getResources().getColor(R.color.White));
                            }
                        }
                    }
                    return false;
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private TextView.OnClickListener More = new TextView.OnClickListener(){

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(),"前往更多消息",Toast.LENGTH_SHORT).show();
        }
    };

    private void JSON_DATA_WEB_CALL() {

    }


    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            //判斷fragement是否仍attach Main
            if(isAdded()){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(viewPager.getCurrentItem()==0){
                            viewPager.setCurrentItem(1);
                        }else if(viewPager.getCurrentItem()==1){
                            viewPager.setCurrentItem(2);
                        }else if(viewPager.getCurrentItem()==2){
                            viewPager.setCurrentItem(3);
                        }else if(viewPager.getCurrentItem()==3){
                            viewPager.setCurrentItem(4);
                        }else{
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }

    private void setGridView() {
        int size = imgArrs.length; //找出需放幾張圖
        int length = 150; //縮圖的寬度

        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 1) * density*0.92); //整個水平選單的寬度
        int itemWidth = (int) (length * density*1.0); //每個縮圖佔的寬度

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(itemWidth);
        gridview.setHorizontalSpacing(1); // 間距
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size); //

        gridview.setAdapter(new Gridadapter(getContext(), imgArrs));

    }

    private boolean checkIfDataIsInt(String data) {
        if (data.equals("") || data.isEmpty()) {
            return false;
        } else {
            try {
                int i = Integer.parseInt(data);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private JSONArray sortJsonArray(JSONArray array) {
        ArrayList<JSONObject> jsons = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject t1, JSONObject t2) {
                String lid = "";
                String rid = "";
                try {
                    lid = t1.getString("County");
                    rid = t2.getString("County");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);

    }

    //====================================================
    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("s", "s:" + s);
            parseJson(s);
        }

        private void parseJson(String s) {

        }
    }


}

