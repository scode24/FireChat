package code.myspace.firechat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
    }

    static class DataHolder{
        TextView msgTxt;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            if(msgList.get(position).getSenderEmail().equals(userEmail) && msgList.get(position).getReceiverEmail().equals(loggedEmail)){
                resourceId = R.layout.left_bubble;
                resourceTxtId = R.id.left_msg_text;
            }else if(msgList.get(position).getSenderEmail().equals(loggedEmail) && msgList.get(position).getReceiverEmail().equals(userEmail)){
                resourceId = R.layout.right_bubble;
                resourceTxtId = R.id.right_msg_text;
            }
            convertView = inflater.inflate(resourceId,parent,false);
            holder = new DataHolder();
            holder.msgTxt = convertView.findViewById(resourceTxtId);
            convertView.setTag(holder);
        }else{
            holder = (DataHolder) convertView.getTag();
        }

        MessageData data = msgList.get(position);
        //holder.photo.setImageResource(user.resId);
        holder.msgTxt.setText(data.getMsg());
        return convertView;

    }

 /*   public void refreshEvents(List<UserData> newData) {
        this.userList.clear();
        this.userList.addAll(newData);
        notifyDataSetChanged();
    }*/
}
