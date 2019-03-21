package tw.tcnr01.I_culture;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tw.tcnr01.I_culture.backup.DBConnector;


public class Shop03 extends AppCompatActivity implements View.OnClickListener  ,NavigationView.OnNavigationItemSelectedListener{
    private Button btnproduct,btnmap,btnmessage;
    private Intent intent=new Intent();
    private Dialog mapDlg;
    private Dialog messagedlg;

    private Integer[] b={
            R.drawable.s1, R.drawable.s2,
            R.drawable.s3, R.drawable.s4,
            R.drawable.s5, R.drawable.s6
    };
    private String[] shop_Array,shop_introduce_array;
    private ArrayList<Map<String, Object>> mList;
    private ListView list;
    private TextView shop03_t001,shop03_t002;
    private String TAG="tcnr21=>";
    private String sqlctl;
    private String sqlctl_message;
    private String shop;

    private String uri = "https://tcnr1621.000webhostapp.com/I_culture/shop/ANDROIDTEST.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop03_layout);

        Intent it =getIntent();
        shop=it.getStringExtra("shop_id");

        sqlctl = "SELECT * FROM shop WHERE Shop_ID=\""+shop+"\"  ORDER BY Shop_ID ASC";
        sqlctl_message = "SELECT * FROM shop_message WHERE Shop_ID=\""+shop+"\"  ORDER BY Messahe_date ASC";
        Log.d(TAG,sqlctl);

//        Log(TAG,str);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        ImageView imageView =new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
//        Listview
        list=(ListView)findViewById(R.id.shop03_list);
        shop_Array=getResources().getStringArray(R.array.shop_Array);
        shop_introduce_array=getResources().getStringArray(R.array.shop_introduce_array);
        shop03_t001=(TextView)findViewById(R.id.shop03_t001);
        shop03_t002=(TextView)findViewById(R.id.shop03_t002);
        mList=new ArrayList<Map<String,Object>>();


//        MYSQL
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
                shop03_t001.setText(Name);
                shop03_t002.setText(Introduce);
                // // 取出 jsonObject 中的字段的值的空格
                Iterator itt = jsonData.keys();
//				tr.setGravity(Gravity.CENTER_HORIZONTAL);
                while (itt.hasNext()){
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




//MYSQL_LIST

        try {
            String result = DBConnector.executeQuery(sqlctl_message,uri);
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
                String Name=jsonData.getString("User_name");
                String Message=jsonData.getString("Message");
//                String URL =jsonData.getString("URL");
                // // 取出 jsonObject 中的字段的值的空格
                Iterator itt = jsonData.keys();
                Map<String,Object> item=new HashMap<String, Object>(); //hash map 用put放資料
                item.put("img",b[0]);
                item.put("shop",Name);
                item.put("shop_introduce",Message);
                mList.add(item);


//				tr.setGravity(Gravity.CENTER_HORIZONTAL);
                while (itt.hasNext()){
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

        for (int i=0;i<shop_Array.length;i++){
//            Map<String,Object> item=new HashMap<String, Object>(); //hash map 用put放資料
//            item.put("img",b[i]);
//            item.put("shop",shop_Array[i]);
//            item.put("shop_introduce",shop_introduce_array[i]);
//            mList.add(item);
        }
        SimpleAdapter adapter=new SimpleAdapter(this,mList,
                R.layout.shop03_layout_item,
                new String[]{"img","shop","shop_introduce"}, //取得KEY欄位的值
                new int[]{R.id.imgView, R.id.txtView, R.id.m0704_t001}); //LAYOUT欄位IP


        list.setAdapter(adapter);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
//設定ListView高度
        list.getLayoutParams().height = (int)(size.y*0.9);





        setupViewComponent();
    }

    private void setupViewComponent() {
        btnproduct=(Button)findViewById(R.id.shop03_btn01);
        btnmap=(Button)findViewById(R.id.shop03_btn02);
        btnmessage=(Button)findViewById(R.id.shop03_btn03);

        btnproduct.setOnClickListener(product_intent);
        btnmap.setOnClickListener(this);
        btnmessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.shop03_btn02:
                mapDlg = new Dialog(Shop03.this);
                mapDlg .setCancelable(false);
                mapDlg .setContentView(R.layout.shop_map_dialog);
                ImageButton btncancel=(ImageButton)mapDlg.findViewById(R.id.shop_map_dialog_btncancel);
                btncancel.setOnClickListener(productbtncancel);
                mapDlg .show();

                break;
            case R.id.shop03_btn03:
                messagedlg=new Dialog(Shop03.this);
                messagedlg.setCancelable(false);
                messagedlg.setContentView(R.layout.message_dialog);
                Button btnmessagecancel=(Button)messagedlg.findViewById(R.id.message_b001);
                btnmessagecancel.setOnClickListener(messagebtncancel);
                messagedlg.show();


                break;

            case R.id.shop03_btn04:
                Toast.makeText(getApplicationContext(), R.string.shop02_m001,
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
    private Button.OnClickListener productbtncancel= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            mapDlg.cancel();
        }
    };

    private Button.OnClickListener messagebtncancel= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            messagedlg.cancel();
        }
    };

    private Button.OnClickListener product_intent= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("class_title", getString(R.string.app_name));
            intent.setClass(Shop03.this, Product.class);
            intent.putExtra("shop_id",shop);
            startActivity(intent);
        }
    };



    @Override
    public void onBackPressed() {
            Shop03.this.finish();
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back) {
            Shop03.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
