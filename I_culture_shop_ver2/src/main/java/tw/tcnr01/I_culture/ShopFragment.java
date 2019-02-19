package tw.tcnr01.I_culture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ShopFragment extends Fragment implements View.OnClickListener {
    private ImageView img01,img02;
    private Intent intent=new Intent();

    private FragmentTransaction mTrasaction;
    private Shop02Fragment shop02Fragment ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.shop_fragment,container,false);
        img01=(ImageView) v.findViewById(R.id.shop01_img01);
        img02=(ImageView)v.findViewById(R.id.shop01_img02);

        img01.setOnClickListener(this);
        img02.setOnClickListener(this);


        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.shop01_img01:

                //用fragment的做法
                shop02Fragment = new Shop02Fragment();
                mTrasaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("class_title",getString(R.string.app_name));
                bundle.putString("Kind","f");
                //fragment傳值
                shop02Fragment.setArguments(bundle);
                mTrasaction.replace(R.id.container,shop02Fragment).commit();

                //由於改為fragment intent的方法就不能用了
                /*intent.putExtra("class_title", getString(R.string.app_name));
               intent.setClass(shop01.this, shop02.class);
                intent.putExtra("Kind", "f");*/
                break;
            case R.id.shop01_img02:

                //用fragment的做法
                shop02Fragment = new Shop02Fragment();
                mTrasaction = getActivity().getSupportFragmentManager().beginTransaction();
                bundle = new Bundle();
                bundle.putString("class_title",getString(R.string.app_name));
                bundle.putString("Kind","c");
                //fragment傳值
                shop02Fragment.setArguments(bundle);
                mTrasaction.replace(R.id.container,shop02Fragment).commit();

               /* intent.putExtra("class_title", getString(R.string.app_name));
                intent.setClass(shop01.this, shop02.class);
                intent.putExtra("Kind", "c");*/
                break;
        }
       // startActivity(intent);
    }
}
