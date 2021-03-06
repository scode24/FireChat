package code.myspace.firechat;

import android.app.Activity;
import android.app.ProgressDialog;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by myspace on 12/18/2017.
 */

public class CustomAdapter extends ArrayAdapter<UserData> {

    Context context;
    int resourceId;
    List<UserData> userList;
    DataHolder holder;

    ProgressDialog mProgressDialog;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<UserData> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resourceId = resource;
        this.userList = objects;


    }

    static class DataHolder{
        ImageView photo;
        TextView name;
        TextView email;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resourceId,parent,false);
            holder = new DataHolder();
            holder.photo = (ImageView)convertView.findViewById(R.id.photoImg);
            holder.name = (TextView)convertView.findViewById(R.id.nameTxt);
            holder.email = (TextView)convertView.findViewById(R.id.emailTxt);
            convertView.setTag(holder);
        }else{
            holder = (DataHolder) convertView.getTag();
        }

        UserData user = userList.get(position);
        asyncTask task = new asyncTask();
        try {
            holder.photo.setImageBitmap(task.execute(user.photoUrl).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        holder.name.setText(user.name);
        holder.email.setText(user.email);
        return convertView;

    }

    private class asyncTask extends AsyncTask<String,Void,Bitmap> {

        private String resp;
        Bitmap img;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                img = getImageBitmap(strings[0]);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            holder.photo.setImageBitmap(bitmap);
            //hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            //showProgessBar("Loading data.Please wait.");
        }
    }


    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("TAG", "Error getting bitmap", e);
        }
        return bm;
    }

}
