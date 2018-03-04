package code.myspace.firechat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by myspace on 12/18/2017.
 */

public class MessageAdapter extends ArrayAdapter<MessageData> {

    Context context;
    int resourceId;
    int resourceTxtId;
    List<MessageData> msgList;
    DataHolder holder;

    private String userEmail;
    private String loggedEmail;
    //private LayoutInflater mInflater;

    public String getLoggedEmail() {
        return loggedEmail;
    }

    public void setLoggedEmail(String loggedEmail) {
        this.loggedEmail = loggedEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<MessageData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceId = resource;
        this.msgList = objects;
        //mInflater = (LayoutInflater) context.getSystemService();
    }

    static class DataHolder{
        TextView msgTxt;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Toast.makeText(getContext(),msgList.get(position).getSenderEmail()+"   "+userEmail+"     "+loggedEmail,Toast.LENGTH_LONG).show();

        if(convertView == null){

            if (msgList.get(position).getSenderEmail().equals(loggedEmail)) {
                resourceId = R.layout.right_bubble;
                resourceTxtId = R.id.right_msg_text;
            }

            if (msgList.get(position).getSenderEmail().equals(userEmail)) {
                resourceId = R.layout.left_bubble;
                resourceTxtId = R.id.left_msg_text;
            }

        }else{
            holder = (DataHolder) convertView.getTag();
        }

        convertView = LayoutInflater.from(context).inflate(resourceId,null);
        holder = new DataHolder();
        holder.msgTxt = convertView.findViewById(resourceTxtId);
        convertView.setTag(holder);

        ViewGroup.LayoutParams lp = holder.msgTxt.getLayoutParams();


        MessageData data = msgList.get(position);
        holder.msgTxt.setText(data.getMsg());
        return convertView;


    }

}
