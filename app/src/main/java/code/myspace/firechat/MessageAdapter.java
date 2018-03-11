package code.myspace.firechat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_bubble,null);
            holder = new DataHolder();
            holder.msgTxt = convertView.findViewById(R.id.left_msg_text);
            convertView.setTag(holder);

        }else{
            holder = (DataHolder) convertView.getTag();
        }

        LayoutParams lp = (LayoutParams) holder.msgTxt.getLayoutParams();

        if (msgList.get(position).getSenderEmail().equals(loggedEmail)) {
            lp.gravity = Gravity.RIGHT;
            holder.msgTxt.setBackgroundColor(Color.BLUE);
        }

        if (msgList.get(position).getSenderEmail().equals(userEmail)) {
            lp.gravity = Gravity.LEFT;
        }

        holder.msgTxt.setLayoutParams(lp);

        MessageData data = msgList.get(position);
        holder.msgTxt.setText(data.getMsg());
        return convertView;


    }

}
