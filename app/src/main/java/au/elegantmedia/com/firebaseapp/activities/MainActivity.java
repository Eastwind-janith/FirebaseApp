package au.elegantmedia.com.firebaseapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import au.elegantmedia.com.firebaseapp.R;
import au.elegantmedia.com.firebaseapp.adapters.ViewAdapter;
import au.elegantmedia.com.firebaseapp.helpers.FirebaseHelper;
import au.elegantmedia.com.firebaseapp.models.UserDetails;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "tag";
    private static final int PICK_IMAG = 4;
    ListView lvDetails;
    DatabaseReference mDataBase;
    StorageReference mStorage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    LayoutInflater inflater;

    Context context;
    ArrayList<UserDetails> list;
    FirebaseHelper firebaseHepler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(au.elegantmedia.com.firebaseapp.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseHepler = new FirebaseHelper(mDataBase, mStorage);

        //Set Adapter to ListView
        lvDetails = (ListView) findViewById(R.id.lv_details);
        ViewAdapter viewAdapter = new ViewAdapter(MainActivity.this, firebaseHepler.setUserData());
        lvDetails.setAdapter(viewAdapter);

        progressBar.setVisibility(View.INVISIBLE);

        itemSelect();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserIntn = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(addUserIntn);
            }
        });
    }

    private void itemSelect() {

        lvDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);

                list = firebaseHepler.setUserData();
                Log.wtf("tag", String.valueOf(list));
                UserDetails userDetails = list.get(position);
                Log.wtf("tag", String.valueOf(userDetails.getName()));

                Log.wtf("tag", "" + list.get(position).getKey());

                final String key = list.get(position).getKey();

                inflater = getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);

                final ImageButton btnImage = (ImageButton) content.findViewById(R.id.img_put);
                final EditText etName = (EditText) content.findViewById(R.id.et_name);
                final EditText etAge = (EditText) content.findViewById(R.id.et_age);
                final EditText etEmail = (EditText) content.findViewById(R.id.et_email);

                Picasso.with(context).load(String.valueOf(list.get(position).getImage())).into(btnImage);
                etName.setText(list.get(position).getName());
                etAge.setText(list.get(position).getAge());
                etEmail.setText(list.get(position).getEmail());


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(content)
                        .setTitle("Edit Item")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                UserDetails userDetails = new UserDetails();
                                userDetails.setName(etName.getText().toString().trim());
                                userDetails.setAge(etAge.getText().toString().trim());
                                userDetails.setEmail(etEmail.getText().toString().trim());

                                userDetails.setKey(key);
                                firebaseHepler.uploadImage(context, Uri.parse(list.get(position).getImage()), btnImage, userDetails);
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        lvDetails.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                list = firebaseHepler.setUserData();
                Log.wtf("tag", String.valueOf(list));
                UserDetails userDetails = list.get(position);
                Log.wtf("tag", String.valueOf(userDetails.getName()));

                Log.wtf("tag", "" + list.get(position).getKey());

                final String key = list.get(position).getKey();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Delete Item...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                UserDetails userDetails = new UserDetails();

                                userDetails.setKey(key);
                                firebaseHepler.deleteData(context, userDetails);
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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
