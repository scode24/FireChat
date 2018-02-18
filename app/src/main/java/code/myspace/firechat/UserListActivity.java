package code.myspace.firechat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class UserListActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fireUser = mAuth.getCurrentUser();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    List<UserData> userList;
    CustomAdapter customAdapter;
    ListView listView;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Users user_data = new Users();
        user_data.setUserName(fireUser.getDisplayName());
        user_data.setPhotoUrl(fireUser.getPhotoUrl().toString());

        // this is for setting value in database
        databaseReference.child("users").child(fireUser.getEmail().replace("@gmail.com","").replace(".","(&)")).setValue(user_data);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = (ListView) findViewById(R.id.userListView);
        userList = new ArrayList<UserData>();
        customAdapter = new CustomAdapter(this,R.layout.user_row,userList);
        listView.setAdapter(customAdapter);

        //userList.add(new UserData("test","Loading users...",""));
        /*userList.add(new UserData(R.mipmap.ic_launcher_round,"Soumya Sankar","s@gmail.com"));
        userList.add(new UserData(R.mipmap.ic_launcher_round,"Pratik Das","pratworld@gmail.com"));*/

        /*
        TextView name = v.findViewById(R.id.userName);
                TextView email = v.findViewById(R.id.userEmail);
                ImageView img = v.findViewById(R.id.userPhoto);
                Users user = (Users)model;
                name.setText(user.getUserName());
                email.setText("test");


         */
        //customAdapter = new CustomAdapter(this,R.layout.user_row,userList);
        //listView.setAdapter(customAdapter);

        showProgessBar("Loading users...");
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String s) {
                if (snapshot.exists()){
/*                    if (!fireUser.getEmail().equals(snapshot.getKey().replace("(&)", ".") + "@gmail.com")) {
                        Users oneUser = snapshot.getValue(Users.class);
                        userList.add(new UserData(oneUser.getPhotoUrl(), oneUser.getUserName(), snapshot.getKey().replace("(&)", ".") + "@gmail.com"));
                    }*/
                    Users oneUser = snapshot.getValue(Users.class);
                    userList.add(new UserData(oneUser.getPhotoUrl(), oneUser.getUserName(), snapshot.getKey().replace("(&)", ".") + "@gmail.com"));
                    customAdapter.notifyDataSetChanged();
                    mProgressDialog.dismiss();
                } else {
                    mProgressDialog.dismiss();
                }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),userList.get(i).name,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ChatlistActivity.class);
                intent.putExtra("email",userList.get(i).email);
                intent.putExtra("name",userList.get(i).name);
                intent.putExtra("photoUrl",userList.get(i).photoUrl);
                startActivity(intent);

            }
        });

        TextView userName = (TextView)findViewById(R.id.userName);
        TextView userEmail = (TextView)findViewById(R.id.userEmail);
        userName.setText(fireUser.getDisplayName());
        userEmail.setText(fireUser.getEmail());

        asyncTask at = new asyncTask();
        at.execute(fireUser.getPhotoUrl().toString());

        //getDataFromServer();

    }

    @SuppressLint("NewApi")
    private class asyncTask extends AsyncTask<String,Void,Bitmap>{

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
            mProgressDialog = new ProgressDialog(UserListActivity.this);
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
