package code.myspace.firechat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ChatlistActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fireUser = mAuth.getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    ToneGenerator toneG;

    ProgressDialog mProgressDialog;

    MessageAdapter msgAdapter;
    List<MessageData> msgList;
    ListView listView;
    MessageData msgData;

    String loggedEmail;
    String userEmail;
    EditText msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_layout);
        Intent intent = getIntent();
        TextView name = findViewById(R.id.userName);
        TextView email = findViewById(R.id.userEmail);
        msgText = findViewById(R.id.msgText);

        loggedEmail = fireUser.getEmail();
        userEmail = intent.getStringExtra("email");

        msgList = new ArrayList<MessageData>();
        msgAdapter = new MessageAdapter(this,R.layout.left_bubble,msgList);
        msgAdapter.setLoggedEmail(loggedEmail);
        msgAdapter.setUserEmail(userEmail);
        listView = findViewById(R.id.msgListView);
        listView.setAdapter(msgAdapter);

        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));

        asyncTask task = new asyncTask();
        task.execute(intent.getStringExtra("photoUrl"));

        toneG = new ToneGenerator(AudioManager.RINGER_MODE_NORMAL, 100);

        showMessages();

    }

    public void onSendMsg(View v){
        Toast.makeText(getApplicationContext(),msgText.getText().toString().trim(),Toast.LENGTH_LONG).show();

        if(!msgText.getText().toString().trim().equals("")) {
            databaseReference.child("message_id").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String s) {
                    if (snapshot.exists()) {
                        int msgId = Integer.parseInt(snapshot.getValue().toString()) + 1;
                        //databaseReference.child("message_base").child(String.valueOf(msgId));
                        msgData = new MessageData(loggedEmail, userEmail, msgText.getText().toString(), "");
                        databaseReference.child("message_base").child(String.valueOf(msgId)).setValue(msgData);
                        databaseReference.child("message_id").child("value").setValue(String.valueOf(msgId));
                        msgAdapter.notifyDataSetChanged();
                        toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
                        msgText.setText("");
                        //listView.setSelection(msgAdapter.getCount());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

            });
        }

    }

    private void showMessages() {
        databaseReference.child("message_base").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.exists()){
                    MessageData oneMsg = snapshot.getValue(MessageData.class);
                    msgList.add(new MessageData(oneMsg.getSenderEmail(), oneMsg.getReceiverEmail(),oneMsg.getMsg(),oneMsg.getTime()));
                    msgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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
