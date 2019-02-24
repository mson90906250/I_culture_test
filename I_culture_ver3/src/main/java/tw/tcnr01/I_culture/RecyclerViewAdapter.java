package tw.tcnr01.I_culture;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by JUNED on 6/16/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    RecyclerView recyclerView;

    Context context;

    List<GetDataAdapter> getDataAdapter;

    ImageLoader imageLoader1;

    private  OnItemClickListener mListener; //記得是要用自己的而不是系統的

    //*************自定義一個OnItemClickListener的interface
    public interface OnItemClickListener{
        void onItemClick(int position, View itemView);
    }

    //*************自定義setOnItemClickListener的方法
    public void setOnItemClickListerner(OnItemClickListener listerner){
        mListener = listerner;
    }

    public RecyclerViewAdapter(List<GetDataAdapter> getDataAdapter, Context context){

        super();
        this.getDataAdapter = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(v,mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        GetDataAdapter getDataAdapter1 =  getDataAdapter.get(position);

        imageLoader1 = ServerImageParseAdapter.getInstance(context).getImageLoader();

        imageLoader1.get(getDataAdapter1.getImageServerUrl(),
                ImageLoader.getImageListener(
                        Viewholder.networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );
//Viewholder設定
        Viewholder.networkImageView.setImageUrl(getDataAdapter1.getImageServerUrl(), imageLoader1);

        Viewholder.DateView.setText(getDataAdapter1.getEventDate());

        //Viewholder.Date_startView.setText(getDataAdapter1.getDate_start());

        //Viewholder.Date_endView.setText(getDataAdapter1.getDate_end());

        Viewholder.TitleView.setText(getDataAdapter1.EventTitle);

        Viewholder.ContentView.setText(getDataAdapter1.EventContent);

    }

    @Override
    public int getItemCount() {

        return getDataAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView DateView,TitleView,ContentView,Date_startView,Date_endView;
        public NetworkImageView networkImageView ;

        public ViewHolder(final View itemView, final OnItemClickListener listener) {

            super(itemView);
//設定欄位相關的View
            DateView = (TextView) itemView.findViewById(R.id.item_date_start) ;
            //Date_startView = (TextView) itemView.findViewById(R.id.item_date_start) ;
            //Date_endView = (TextView) itemView.findViewById(R.id.item_date_end) ;


            TitleView = (TextView) itemView.findViewById(R.id.item_title) ;

            ContentView = (TextView) itemView.findViewById(R.id.item_content) ;

            networkImageView = (NetworkImageView) itemView.findViewById(R.id.VollyNetworkImageView1) ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        //取得該item的index值
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position,itemView);
                        }


                    }
                }
            });

        }
    }
}
