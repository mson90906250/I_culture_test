package tw.tcnr01.I_culture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BusFragment extends Fragment implements  LocationListener {
    private static String[][] locations = {
            {"我的位置", "24.0628609,120.5253553"},
            {"互助新村", "24.1450789,120.6563086"},
            {"美村向上路口", "24.1450784,120.6563086"},
            {"向上國中", "24.1450789,120.6563086"},
            {"英才郵局", "24.1450789,120.6563086"},
            {"英才郵局", "24.1450789,120.6563086"},
            {"美村向上北路口", "24.1450789,120.6563086"}};
    private Spinner mSpnLocation;
    private static final String MAP_URL = "file:///android_asset/GoogleMap_Bus.html";
    // 自建的html檔名
    private WebView webView;
    private String Lat;
    private String Lon;
    private String jcontent;// 地名變數
    /*** GPS***/
    private LocationManager locationMgr;
    private String provider; // 提供資料
    private TextView txtOutput;

    private final String TAG = "oldpa=>";
    /*** Navigation **/
    int iSelect;
    private Button bNav;
    String[] sLocation;
    String Navon = "off";
    String Navstart = "24.172127,120.610313"; // 起始點
    String Navend = "24.144671,120.683981"; // 結束點
    //-----------------所需要申請的權限數組---------------
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.SEND_SMS,};
    private List<String> permissionsList = new ArrayList<String>();
    //申請權限後的返回碼
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
//-----------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.bus_fragment,container,false);
        checkRequiredPermission(getActivity());     //  檢查SDK版本, 確認是否獲得權限.
        setupViewComponent(v);//自定義方法

        return v;

    }

    private void setupViewComponent(View v) {
        mSpnLocation = (Spinner) v.findViewById(R.id.spnLocation);
        // ----Location-----------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);

        for (int i = 0; i < locations.length; i++)
            adapter.add(locations[i][0]);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnLocation.setAdapter(adapter);
        mSpnLocation.setOnItemSelectedListener(mSpnLocationOnItemSelLis);
        // ---------------------------------
        webView = (WebView)v. findViewById(R.id.webview);
        txtOutput = (TextView) v.findViewById(R.id.txtOutput);
        //		--導航監聽--
        bNav = (Button) v.findViewById(R.id.Navigation);
        bNav.setOnClickListener(bNavselectOn);
    }

    private void checkRequiredPermission(Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    //	--導航監聽--
    private Button.OnClickListener bNavselectOn = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Navon == "off") {
                bNav.setTextColor(getResources().getColor(R.color.Blue));
                Navon = "on";
                bNav.setText("關閉路徑規劃");
                setMapLocation();
            } else {
                bNav.setTextColor(getResources().getColor(R.color.Red));
                Navon = "off";
                bNav.setText("開啟路徑規劃");
                setMapLocation();
            }
        }
    };
    private AdapterView.OnItemSelectedListener mSpnLocationOnItemSelLis = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View v, int position,
                                   long id) {
            setMapLocation();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    private void setMapLocation() {
        iSelect = mSpnLocation.getSelectedItemPosition();
        sLocation = locations[iSelect][1].split(",");
        Lat = sLocation[0]; // 南北緯
        Lon = sLocation[1]; // 東西經
        jcontent = locations[iSelect][0]; // 地名
//---------增加判斷是否規畫路徑------------------
        if (Navon == "on" && iSelect != 0) {
            Navstart = locations[0][1];
            Navend = locations[iSelect][1];
            final String deleteOverlays = "javascript: RoutePlanning()";
            webView.loadUrl(deleteOverlays);
        }else{
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(BusFragment.this, "AndroidFunction");
            webView.loadUrl(MAP_URL);
        }
    }

    // -------------------------------
    /* 開啟時先檢查是否有啟動GPS精緻定位 */
    @Override
    public void onStart() {
        super.onStart();

        if (initLocationProvider()) {
            nowaddress();
        } else {
            txtOutput.setText("GPS未開啟,請先開啟定位！");
        }
    }

    @Override
    public void onStop() {
        locationMgr.removeUpdates(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /************************************************
     * GPS部份
     ***********************************************/
    /* 檢查GPS 設定GPS服務 */
    private boolean initLocationProvider() {
        locationMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        return false;
    }

    /* 建立位置改變偵聽器 預先顯示上次的已知位置 */
    private void nowaddress() {
        // 取得上次已知的位置
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "No Permission", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "No Permission", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationMgr.getLastKnownLocation(provider);
        updateWithNewLocation(location);

        // 監聽 GPS Listener
        locationMgr.addGpsStatusListener(gpsListener);

        // Location Listener
        long minTime = 5000;// ms
        float minDist = 5.0f;// meter
        locationMgr.requestLocationUpdates(provider, minTime, minDist,
                this);
    }

    GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        /* 監聽GPS 狀態 */
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d(TAG, "GPS_EVENT_STARTED");
                    break;

                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.d(TAG, "GPS_EVENT_STOPPED");
                    break;

                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
                    break;
            }
        }
    };

    private void updateWithNewLocation(Location location) {
        String where = "";
        if (location != null) {

            double lng = location.getLongitude();// 經度
            double lat = location.getLatitude();// 緯度
            float speed = location.getSpeed();// 速度
            long time = location.getTime();// 時間
            String timeString = getTimeString(time);

            where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed + "\n時間: "
                    + timeString + "\nProvider: " + provider;
            // 標記"我的位置"
            locations[0][1] = lat + "," + lng; // 用GPS找到的位置更換 陣列的目前位置
            // --- 呼叫 Map JS
            webView.loadUrl(MAP_URL);
            // ---

        } else {
            where = "*位置訊號消失*";
        }

        // 位置改變顯示
        txtOutput.setText(where);
    }

    private String getTimeString(long timeInMilliseconds) {
        SimpleDateFormat format = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return format.format(timeInMilliseconds);
    }

    /* 位置變更狀態監視 */
    @Override
    public void onLocationChanged(Location location) {
        // 定位改變時
        updateWithNewLocation(location);
        // --- 呼叫 Map JS
        Navstart = locations[0][1];
//        webView.loadUrl(MAP_URL);
        //---------增加判斷是否規畫路徑------------------
        if (Navon == "on" && iSelect != 0) {
            final String deleteOverlays = "javascript: RoutePlanning()";
            webView.loadUrl(deleteOverlays);
        }else{
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(BusFragment.this, "AndroidFunction");
            webView.loadUrl(MAP_URL);
        }
        // ---
        Log.d(TAG, "onLocationChanged");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.v(TAG, "Status Changed: Out of Service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v(TAG, "Status Changed: Temporarily Unavailable");
                break;
            case LocationProvider.AVAILABLE:
                Log.v(TAG, "Status Changed: Available");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        updateWithNewLocation(null);
        Log.d(TAG, "onProviderDisabled");
    }

    // -------html data------------------
    @JavascriptInterface
    public String GetLat() {
        return Lat;
    }

    @JavascriptInterface
    public String GetLon() {
        return Lon;
    }

    @JavascriptInterface
    public String Getjcontent() {
        return jcontent;
    }

    //-----傳送導航資訊-------------------------------
    @JavascriptInterface
    public String Navon() {
        return Navon;
    }

    @JavascriptInterface
    public String Getstart() {
        return Navstart;
    }

    @JavascriptInterface
    public String Getend() {
        return Navend;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getContext(), permissions[i] + "權限申請成功!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "權限被拒絕： " + permissions[i], Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}
