package com.musiquitaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musiquitaapp.R;
import com.musiquitaapp.models.MySession;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {

    private Context context;
    private List<MySession> sessionList;

    public SessionAdapter (Context context, List<MySession> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.session_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull MyViewHolder holder, int position) {
        holder.sessionName.setText(sessionList.get(position).sessionName);
        holder.ownerName.setText(sessionList.get(position).ownerName);

        holder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                // TODO que se conecte a la sesión
            }
        });
    }

    @Override
    public int getItemCount () {
        return sessionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sessionName;
        TextView ownerName;
        RelativeLayout mainContainer;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            sessionName = itemView.findViewById(R.id.sessionName);
            ownerName = itemView.findViewById(R.id.ownerName);
            mainContainer = itemView.findViewById(R.id.mainContainer);
        }
    }
}
