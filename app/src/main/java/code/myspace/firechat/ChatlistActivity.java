package code.myspace.firechat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ChatlistActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_layout);
        Intent intent = getIntent();
        TextView name = findViewById(R.id.userName);
        TextView email = findViewById(R.id.userEmail);
        //ImageView imgView = findViewById(R.id.userPhoto);//intent.getStringExtra()

        Toast.makeText(getApplicationContext(),intent.getStringExtra("name"),Toast.LENGTH_LONG).show();

        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));

        asyncTask task = new asyncTask();
        task.execute(intent.getStringExtra("photoUrl"));

    }


    @SuppressLint("NewApi")
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
            ImageView userPhoto = (ImageView)findViewById(R.id.userPhoto);
            userPhoto.setImageBitmap(bitmap);
            hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            showProgessBar("Loading data.Please wait.");

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


    private void showProgessBar(String msg){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ChatlistActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
