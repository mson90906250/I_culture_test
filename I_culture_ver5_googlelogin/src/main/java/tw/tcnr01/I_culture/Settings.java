package tw.tcnr01.I_culture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.InputStream;


public class Settings extends AppCompatActivity implements View.OnClickListener {
    private Switch Settings_sw01;
    private Button Settings_btn01,Settings_btn02,Settings_btn03;
    private CircleImgView Settings_imgC;
    private Button Settings_chpwd_btn01,Settings_chpwd_btn02;
    private EditText Settings_chpwd_etxt01,Settings_chpwd_etxt02;
    private TextView Settings_chpwd_txt03;
    private final String IMAGE_URL = "imgURL";
    private String mImgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        //取得大頭照網址
        mImgURL = getIntent().getStringExtra(IMAGE_URL);

        setupVIewComponent();
    }

    private void setupVIewComponent() {
        Settings_imgC = (CircleImgView)findViewById(R.id.Settings_imgC);//大頭照
        Settings_sw01 = (Switch)findViewById(R.id.Settings_sw01);//切換是否要接收推播
        //Settings_btn01 = (Button)findViewById(R.id.Settings_btn01);//修改密碼
        //  Settings_btn02 = (Button)findViewById(R.id.Settings_btn02);//更換大頭照

        //將抓取的圖片url放入execute()裡
        new DownloadImageTask((ImageView) findViewById(R.id.Settings_imgC))
                .execute(mImgURL);


//        Settings_btn01.setOnClickListener(this);
        //     Settings_btn02.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.Settings_btn01://修改密碼
                //彈出一個有修改密碼的dialog
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.settings_changepwd_layout);
                Window window = dialog.getWindow();//如果setContentView()內容的容器是LinearLayout，就不用加，是因為activity_dialog這個layout檔我用的是constraintLayout
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);//讓使用者點選背景或上一頁沒有用
                Settings_chpwd_btn01 = (Button)dialog.findViewById(R.id.Settings_chpwd_btn01);//取消
                Settings_chpwd_btn02 = (Button)dialog.findViewById(R.id.Settings_chpwd_btn02);//確認
                Settings_chpwd_txt03 = (TextView)dialog.findViewById(R.id.Settings_chpwd_txt03);//顯示密碼與修改密碼比對後的結果

                Settings_chpwd_btn01.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        //按下取消時,離開dialog
                        dialog.cancel();
                    }
                });
                Settings_chpwd_btn02.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Settings_chpwd_etxt01 = (EditText)dialog.findViewById(R.id.Settings_chpwd_etxt01);
                        Settings_chpwd_etxt02 = (EditText)dialog.findViewById(R.id.Settings_chpwd_etxt02);
                        String pwd = Settings_chpwd_etxt01.getText().toString();
                        String chkpwd = Settings_chpwd_etxt02.getText().toString();
                        //確認欄位是否為空
                        if (!pwd.equals("")&&!chkpwd.equals("")){
                            //確認密碼是否符合規定
                            if (check(pwd)){
                                //再確認密碼與確認密碼是否相符
                                if (pwd.equals(chkpwd)){
                                    Intent it = new Intent(Settings.this,Account.class);
                                    startActivity(it);
                                    dialog.cancel();
                                    Settings.this.finish();
                                }else{
                                    Settings_chpwd_txt03.setText(R.string.Settings_chpwd_txt03);
                                }
                            }else{
                                Settings_chpwd_txt03.setText(R.string.Settings_chdpw_txt05);
                            }
                        }else{
                            Settings_chpwd_txt03.setText(R.string.Settings_chpwd_txt04);
                        }

                    }
                });
                dialog.show();
                break;*/

            /*case R.id.Settings_btn02://更換大頭照

                break;*/

            /*case R.id.Settings_btn03://離開
                Intent it = new Intent(Settings.this,Account.class);
                startActivity(it);
                Settings.this.finish();
                break;*/

        }
    }
    //密碼是否符合規定
    private boolean check(String chk ) {
        //確認密碼的字數
        if(chk.length()>= 8 &&chk.length()<=20){
            //字數符合後,再判斷有無非數字的字
            //將字串中的數字替換成"",導致僅留下非數字的字,再算其長度是否大於零
            if (chk.replaceAll("\\d+","").length()>0){
                //一切都符合後,retrun true;
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    //從外部網路抓圖下來
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case  R.id.back:
                Settings.this.finish();
                break;
        }

        /*  // Handle action bar item clicks here. The action bar will
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
