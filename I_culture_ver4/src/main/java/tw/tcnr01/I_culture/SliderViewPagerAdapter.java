package tw.tcnr01.I_culture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private  Integer [] images;
    private  ArrayList<String> img_url;
    //private  Integer [] images = {R.drawable.home_slider1, R.drawable.home_slider2, R.drawable.home_slider3, R.drawable.home_slider4, R.drawable.home_slider5};

    public SliderViewPagerAdapter(Context context,Integer [] images) {
        this.context = context;
        this.images = images;
    }

    public SliderViewPagerAdapter(Context context, ArrayList<String> img_url) {
        this.context = context;
        this.img_url = img_url;
    }

    @Override
    public int getCount() {
        return((images != null)?images.length:img_url.size());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(images != null){
            View view = layoutInflater.inflate(R.layout.home_custom_layout,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(images[position]);
            ViewPager vp = (ViewPager) container;
            vp.addView(view,0);
            return view;
        }else {
            ImageView imageView = new ImageView(context);
            Picasso.with(context).load(img_url.get(position))
                    .fit()
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);


            //return view;//原imageview的設定
            return imageView;
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}

