package au.elegantmedia.com.firebaseapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import au.elegantmedia.com.firebaseapp.R;
import au.elegantmedia.com.firebaseapp.adapter.ViewAdapter;
import au.elegantmedia.com.firebaseapp.helper.FirebaseHelper;
import au.elegantmedia.com.firebaseapp.helper.UserDetails;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "tag";
    ListView lvDetails;
    DatabaseReference mDataBase;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(au.elegantmedia.com.firebaseapp.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        FirebaseHelper firehepler = new FirebaseHelper(mDataBase,mStorage);

        //Set Adapter to ListView
        lvDetails = (ListView) findViewById(R.id.lv_details);
        ViewAdapter viewAdapter = new ViewAdapter(MainActivity.this, firehepler.setUserData());
        lvDetails.setAdapter(viewAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserIntn = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(addUserIntn);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (id == R.id.action_edit) {
            return true;
        } else if (id == R.id.action_delete) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
