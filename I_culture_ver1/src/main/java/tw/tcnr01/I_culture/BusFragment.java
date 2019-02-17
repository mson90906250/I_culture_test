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
import android.widget.Button;

public class BusFragment extends Fragment implements View.OnClickListener {
    private Button b001;
    private Uri uri;
    private Intent it = new Intent();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //系統原生用不到
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.bus_fragment,container,false);
        b001 =v. findViewById(R.id.bus_b001);
        b001.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())

        {
            case R.id.bus_b001:
                uri = Uri.parse("http://citybus.taichung.gov.tw/ibus/realroute.aspx");
                it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                getActivity().onBackPressed();
        }
    }


}
