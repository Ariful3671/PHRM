package com.troubleshooters.diu.phrm.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.troubleshooters.diu.phrm.EditMedication;
import com.troubleshooters.diu.phrm.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif on 27-03-18.
 */

public class ManualReportAdapter extends RecyclerView.Adapter<ManualReportAdapter.ManualReportAdapterViewHolder>{

    Context context;
    List<ModelReportInfo> reportInfo;

    public ManualReportAdapter(Context context, List<ModelReportInfo> reportInfo) {
        this.context = context;
        this.reportInfo = reportInfo;
    }

    @Override
    public ManualReportAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.report_details_recycler_items,null);
        ManualReportAdapterViewHolder holder=new ManualReportAdapterViewHolder(view,context,reportInfo);
        return holder;
    }

    @Override
    public void onBindViewHolder(ManualReportAdapterViewHolder holder, int position) {

        ModelReportInfo RI=reportInfo.get(position);
        holder.hospitalName.setText("Hospital: "+RI.getHn());
        holder.reportType.setText("Report Type: "+RI.getRT());
        holder.date.setText("Date: "+RI.getDT());

    }

    @Override
    public int getItemCount() {
        return reportInfo.size();
    }


    public void removeItem(int position)
    {
        reportInfo.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ModelReportInfo item,int position)
    {
        reportInfo.add(position,item);
        notifyItemInserted(position);
    }



    public class ManualReportAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hospitalName,reportType,date;
        Context context;
        List<ModelReportInfo> info=new ArrayList<ModelReportInfo>();
        public RelativeLayout viewForeground;
        RelativeLayout viewBackground;

        public ManualReportAdapterViewHolder(View itemView, Context context,List<ModelReportInfo> info) {
            super(itemView);
            this.context=context;
            this.info=info;
            itemView.setOnClickListener(this);
            hospitalName=itemView.findViewById(R.id.hospitalName);
            reportType=itemView.findViewById(R.id.reportType);
            date=itemView.findViewById(R.id.save_date);
            viewBackground=itemView.findViewById(R.id.background_view);
            viewForeground=itemView.findViewById(R.id.foreground_view);
        }
        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();
            ModelReportInfo reportDetailInfo=this.info.get(position);
            File f=reportDetailInfo.getfIle();
            if(f.exists())
            {
                Uri path = FileProvider.getUriForFile(context, "com.troubleshooters.diu.phrm", f);
                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                context.startActivity(pdfOpenintent);
            }
            else{
                Toast.makeText(context, "Not exist", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
