package tw.tcnr01.I_culture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Event02 extends AppCompatActivity {
    ViewPager viewPager;
    SliderViewPagerAdapter adapter;
    LinearLayout sliderDots;
    private int dotCounts;
    private ImageView[] dots;
    private TextView text01,text02,text03,text04;
    private ScrollView scroview;
    private ImageView imageview;
    private Integer[] images = {R.drawable.event02_slider2, R.drawable.event02_slider7, R.drawable.event02_slider8};
    private ArrayList<String> img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event02_layout);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//顯示返回圖示

        Intent it = getIntent();
        String Date = it.getStringExtra("JSON_EventDate");//取得putExtra的date
        String Title = it.getStringExtra("JSON_EventTitle");//取得putExtra的title
        String Content = it.getStringExtra("JSON_EventContent");
        img_url = it.getStringArrayListExtra("img_url");

        text01=(TextView)findViewById(R.id.Event_Date);
        text01.setText(Date);
        text02=(TextView)findViewById(R.id.Event_Title);
        text02.setText(Title);
        text03=(TextView)findViewById(R.id.Content);
        text03.setText(Content);

        //Picasso.with(this).load("https://tcnr1624.000webhostapp.com/image/image01.jpg").into(imageview);



        scroview = (ScrollView) findViewById(R.id.scroview);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new SliderViewPagerAdapter(this,img_url);
        viewPager.setAdapter(adapter);


        sliderDots =(LinearLayout) findViewById(R.id.SliderDots);


        dotCounts = adapter.getCount();
        dots = new ImageView[dotCounts];

        for(int i=0;i<dotCounts;i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slidershow_nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 2, 8, 0);

            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slidershow_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0;i<dotCounts;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slidershow_nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slidershow_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);


    }
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Event02.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }else if(viewPager.getCurrentItem()==2){
                        viewPager.setCurrentItem(3);
                    }
                    else if(viewPager.getCurrentItem()==3){
                        viewPager.setCurrentItem(4);
                    }else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();//先做判斷 是否←有被點擊
//        }

        switch (item.getItemId()) {
            case android.R.id.home:////主键id 一定要寫這樣
                Event02.this.finish();
 //               onBackPressed();//按返回图标直接回退上个界面
                return true;
            //break;
        }
        return super.onOptionsItemSelected(item);
    }

}
