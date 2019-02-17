package tw.tcnr01.I_culture;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class Account extends AppCompatActivity implements View.OnClickListener {
    private EditText Account_etxt01, Account_etxt02;
    private TextView Account_txt03;
    private Button Account_btn01, Account_btn02, Account_btn03;
    private String input_account, input_password;
    private String test_account, test_pwd, test_mail;//用來模擬抓取註冊後新增的值
    private String TAG = "tcnr01=>";
    private Button test;
    private String sqlctl;
    private String mAccount,mPwd,mEmail,mImageURL;
    private final String JSON_USERNAME = "Username";
    private final String JSON_PASSWORD ="Password";
    private final String JSON_EMAIL = "Email";
    private final String JSON_IMAGE_URL = "Headshot";
    private static Main main ;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account);
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


        setupViewComponent();

    }

    private void setupViewComponent() {
        Account_etxt01 = (EditText) findViewById(R.id.Account_etxt01);//帳號
        Account_etxt02 = (EditText) findViewById(R.id.Account_etxt02);//密碼
        Account_txt03 = (TextView) findViewById(R.id.Account_txt03);//忘記密碼
        //  Account_btn01 = (Button) findViewById(R.id.Account_btn01);//註冊
        Account_btn02 = (Button) findViewById(R.id.Account_btn02);//登入
        Account_btn03 = (Button) findViewById(R.id.Account_btn03);//使用google帳號登入
        //test = (Button) findViewById(R.id.test);


        Account_txt03.setOnClickListener(this);
//        Account_btn01.setOnClickListener(this);
        Account_btn02.setOnClickListener(this);
        Account_btn03.setOnClickListener(this);
       // test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Account_txt03://忘記密碼
                input_account = Account_etxt01.getText().toString();
                //先確認帳號有無輸入
                if (!input_account.equals("")) {
                    //再確認輸入的帳號是否存在
                    if (checkAccount(input_account)) {
                        //確認有此帳號後,顯示dialog,並在後台將密碼寄到註冊信箱
                        //呼叫getDialog()回傳一個空的dialog
                        getDialog().setMessage(R.string.Account_forgetpwd).show();
                    } else {
                        //確認無此帳號後,顯示dialog
                        getDialog().setMessage(R.string.Account_account_check_error).show();
                    }
                } else {
                    getDialog().setMessage(R.string.Account_account_input_notext).show();
                }
                break;

           /* case R.id.Account_btn01://註冊
                //到註冊的頁面
                Intent it = new Intent(Account.this, Register.class);
                startActivity(it);
                Account.this.finish();
                break;*/

            case R.id.Account_btn02://登入
                //取得輸入的帳號及密碼
                input_account = Account_etxt01.getText().toString();
                input_password = Account_etxt02.getText().toString();

                if (check(input_account, input_password)) {
                    //輸入正確的話,跳出對應的dialog
                    AlertDialog.Builder d = new AlertDialog.Builder(this);
                    d.setTitle("通知!")
                            .setMessage(input_account + getString(R.string.Account_welcome))
                            .setCancelable(false);
                    d.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //按下確定後離開登入頁面,並回到前一頁面
                            main.setAccount(mAccount,mPwd,mEmail,mImageURL);
                            finish();
                        }
                    });
                    d.show();
                } else {
                    //輸入錯誤的話,跳出對應的dialog
                    getDialog().setMessage(R.string.Account_error).show();
                }

                break;

            case R.id.Account_btn03://使用google帳號登入
                break;

            /*case R.id.test://到設定的頁面
                Intent itn = new Intent(Account.this, Settings.class);
                startActivity(itn);
                Account.this.finish();
                break;*/

        }
    }
    private AlertDialog.Builder getDialog() {

        AlertDialog.Builder f = new AlertDialog.Builder(this);
        f.setTitle("通知!")
                .setCancelable(false);
        f.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return f;
    }

    private boolean checkAccount(String input_account) {
        String account = "owner";
        if (account.equals(input_account)) {
            return true;
        } else {
            return false;
        }

    }

    //將輸入的帳號及密碼送到後台做判斷
    private boolean check(String input_account, String input_password) {
        //不能解決大小寫 因為ci代表case insensitive(大小寫不敏感) 反之cs為case sensitive 但有些server并沒有utf8_general_cs 所以請將資料庫的編碼改成utf8_bin
       sqlctl = "SELECT * FROM account WHERE Username ="+"\'"+input_account+"\'"+"AND Password = "+"\'"+input_password+"\'";


        try {
            String result = DBConnector.executeQuery(sqlctl);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //幾筆資料  理論上只會有一筆 因為是用來確認帳號密碼的
            JSONArray jsonArray = new JSONArray(result);
            // ---
            //幾個欄位
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                // // 取出 jsonObject 中的字段的值的空格

                mAccount = jsonData.getString(JSON_USERNAME);
                mPwd = jsonData.getString(JSON_PASSWORD);
                mEmail = jsonData.getString(JSON_EMAIL);
                mImageURL = jsonData.getString(JSON_IMAGE_URL);
                Log.d(TAG,mAccount+mEmail+mPwd);

                //由於key值為已知 所以不須使用下面方法
                /*             Iterator itt = jsonData.keys();
//    tr.setGravity(Gravity.CENTER_HORIZONTAL);
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

                }*/
            }
            return true;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return  false;
        }

       /* //模擬用的
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null) {
            Log.d(TAG, "account=" + bundle.getString("account"));
            Log.d(TAG, "pwd=" + bundle.getString("pwd"));
            if (bundle.get("account")!=null&&bundle.get("pwd")!=null) {
                if (bundle.getString("account").equals(input_account) && bundle.getString("pwd").equals(input_password)) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }

        } else {
            return false;
        }*/
        //--------------
    /*  String account = "owner01";
      String pwd = "a123456789";

      if (input_account.equals(account)&&input_password.equals(pwd)){
          return true;
      }else {
          return false;
      }*/


    }

    public static void setContext(Object context) {
        main = (Main) context;
    }


    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.back:
                Account.this.finish();
                break;
        }

        /*      // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
