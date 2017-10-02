package au.elegantmedia.com.firebaseapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import au.elegantmedia.com.firebaseapp.R;
import au.elegantmedia.com.firebaseapp.helpers.FirebaseHelper;
import au.elegantmedia.com.firebaseapp.models.UserDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAG = 4;

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_age)
    EditText edtAge;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_img)
    ImageButton btnImage;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    UserDetails userDetails;
    Uri image;

    StorageReference storage;
    private DatabaseReference mDataBase;
    private String sName, sEmail, sAge, sImage;

    @OnClick(R.id.btn_save)
    public void save() {
        mProgressBar.setVisibility(View.VISIBLE);

        storage = FirebaseStorage.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance().getReference();

        final FirebaseHelper firebaseHelper = new FirebaseHelper(mDataBase, storage);

        sName = edtName.getText().toString().trim();
        sAge = edtAge.getText().toString().trim();
        sEmail = edtEmail.getText().toString().trim();

        userDetails = new UserDetails();

        userDetails.setName(sName);
        userDetails.setAge(sAge);
        userDetails.setEmail(sEmail);

        firebaseHelper.uploadImage(this, image, btnImage, userDetails);

    }

    @OnClick(R.id.btn_img)
    public void getImage() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//            Uri media = getMediaFile(MEDIA_IMAGE);
//            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, media);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAG && resultCode == RESULT_OK) {

            image = data.getData();
            Picasso.with(this).load(image).resize(400, 400).centerCrop().into(btnImage);
        }
    }

    //Check EditText empty or not
    protected boolean isCheckED() {
        if (TextUtils.isEmpty(sName) && TextUtils.isEmpty(sAge) && TextUtils.isEmpty(sEmail) && image == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reg_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_user) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
