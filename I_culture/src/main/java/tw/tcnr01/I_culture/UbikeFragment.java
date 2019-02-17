package tw.tcnr01.I_culture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UbikeFragment extends Fragment implements View.OnClickListener {

    private ImageView b001,b002,b003,b004;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.ubike_fragment,container,false);
        b001=v.findViewById(R.id.ubike_b002);
        b002=v.findViewById(R.id.ubike_b003);
        b003=v.findViewById(R.id.ubike_b004);
        b004=v.findViewById(R.id.ubike_b005);
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        b003.setOnClickListener(this);
        b004.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ubike_b002:
                Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(it);
                getActivity().onBackPressed();
            case R.id.ubike_b003:
                Uri uri1 = Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
                Intent it1 = new Intent(Intent.ACTION_VIEW, uri1);

                startActivity(it1);
                getActivity().onBackPressed();
            case R.id.ubike_b004:
                Uri uri2 = Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
                Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);

                startActivity(it2);
                getActivity().onBackPressed();
            case R.id.ubike_b005:
                Uri uri3 = Uri.parse("http://maps.google.com/maps?f=d&saddr=startLat%20startLng&daddr=endLat%20endLng&hl=en");
                Intent it3 = new Intent(Intent.ACTION_VIEW, uri3);

                startActivity(it3);
                getActivity().onBackPressed();
        }
    }
}
