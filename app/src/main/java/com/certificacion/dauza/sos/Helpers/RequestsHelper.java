package com.certificacion.dauza.sos.Helpers;

/**
 * Created by Ignacio on 12/17/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.certificacion.dauza.sos.R;
import com.certificacion.dauza.sos.Models.EmergencyServiceRequest;

import java.util.ArrayList;
import java.util.Date;


public class RequestsHelper extends RecyclerView.Adapter<RequestsHelper.ViewHolder> {
//
    private ArrayList<EmergencyServiceRequest> data;
    private Context context;

    public RequestsHelper(Context context) {
        data = new ArrayList<EmergencyServiceRequest>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EmergencyServiceRequest p = data.get(position);
        int serviceTypeI = p.getEmergencyServiceType();
        String serviceTypeS = "Hospital Central";
        String serviceTypeURL = "http://cliparts.co/cliparts/6Ty/5EG/6Ty5EG4Rc.png";
            if(serviceTypeI == 1){
                serviceTypeS = "Policia";
                serviceTypeURL = "https://png.icons8.com/color/1600/policeman-male";
            } else if(serviceTypeI == 2){
               serviceTypeS = "Bomberos";
                serviceTypeURL = "www.myiconfinder.com/uploads/iconsets/256-256-d885c0e59e7ed9763f71a3368f52a3bd-firefighter.png";
            }

        Glide.with(context).load(serviceTypeURL).into(holder.serviceTypeImageView);
        holder.serviceTypeTextView.setText(serviceTypeS);

        Glide.with(context).load("http://3.bp.blogspot.com/-KCTCIdR7djA/Tyzrk7-WPZI/AAAAAAAAAWM/64LkjNJz29k/s1600/Map_pin1.png").into(holder.locationView);

        Date date = new Date(p.getDateOfService());
        holder.dateOfServiceTextView.setText(date.toString());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void llenarDatos(ArrayList<EmergencyServiceRequest> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addEmergencyServiceRequest(EmergencyServiceRequest c) {
        data.add(c);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceTypeImageView;
        TextView serviceTypeTextView;
        TextView dateOfServiceTextView;
        ImageView locationView;


        public ViewHolder(View itemView) {
            super(itemView);

            serviceTypeImageView = (ImageView) itemView.findViewById(R.id.serviceTypeImageView);
            serviceTypeTextView = (TextView) itemView.findViewById(R.id.serviceTypeTextView);
            dateOfServiceTextView = (TextView) itemView.findViewById(R.id.dateOfServiceTextView);
            locationView = (ImageView) itemView.findViewById(R.id.locationImageView);


        }
    }
//
}
