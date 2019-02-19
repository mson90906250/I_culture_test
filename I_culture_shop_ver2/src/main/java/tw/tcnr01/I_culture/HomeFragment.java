package tw.tcnr01.I_culture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    private Integer[] imgArrs = {
            R.drawable.home_img01, R.drawable.home_img02,
            R.drawable.home_img03, R.drawable.home_img04,
            R.drawable.home_img05, R.drawable.home_img06,
            R.drawable.home_img07, R.drawable.home_img08,
            R.drawable.home_img09, R.drawable.home_img10

    };

    private  Integer [] images = {R.drawable.home_slider1, R.drawable.home_slider2,
                                                             R.drawable.home_slider3, R.drawable.home_slider4,
                                                             R.drawable.home_slider5};

    private GridView gridview;
    ViewPager viewPager;
    SliderViewPagerAdapter adapter;
    LinearLayout sliderDots;
    private int dotCounts;
    private ImageView[] dots;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.home_fragment,container,false);
        gridview = (GridView) v.findViewById(R.id.m0703_g001);
        setGridView();

        //----------------------幻燈片----------------------
        viewPager =(ViewPager) v.findViewById(R.id.viewPager);
        adapter = new SliderViewPagerAdapter(getActivity(),images);
        viewPager.setAdapter(adapter);
        sliderDots = (LinearLayout) v.findViewById(R.id.SliderDots);
        dotCounts = adapter.getCount();
        dots = new ImageView[dotCounts];
        for(int i=0;i<dotCounts;i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slideshow_nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 2, 8, 0);
            sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slideshow_dot));
        //----------------addOnPageChangeListener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0;i<dotCounts;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slideshow_nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.home_slideshow_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),3000,6000);

        return v;

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
        int length = 100; //縮圖的寬度

        DisplayMetrics dm = new DisplayMetrics(); //找出使用者手機的寬高
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 2) * density*1.0); //整個水平選單的寬度
        int itemWidth = (int) (length * density*0.8); //每個縮圖佔的寬度

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        gridview.setLayoutParams(params);
        gridview.setColumnWidth(itemWidth);
        gridview.setHorizontalSpacing(5); // 間距
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size); //

        gridview.setAdapter(new Gridadapter(getActivity(), imgArrs));

    }
}

