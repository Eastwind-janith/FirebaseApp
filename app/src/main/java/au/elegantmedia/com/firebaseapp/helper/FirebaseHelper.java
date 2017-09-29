package au.elegantmedia.com.firebaseapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import au.elegantmedia.com.firebaseapp.activity.RegisterActivity;

/**
 * Created by Nisala on 9/21/17.
 */


public class FirebaseHelper {

    private static final String TAG = "tag";
    DatabaseReference db;
    StorageReference storage;
    Boolean save;
    ArrayList<UserDetails> userDetailList = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db,StorageReference storage) {
        this.db = db;
        this.storage=storage;
    }

    //Write Data
    public boolean Save(UserDetails userDetails) {

        if (userDetails == null) {
            save = false;
        } else {
            try {
                db.child("UserDetails").push().setValue(userDetails);
                save = true;
            } catch (DatabaseException e) {
                Log.i(TAG, String.valueOf(e));
                save = false;
            }
        }
        return save;
    }

//    public ArrayList<UserDetails> secMethod() {
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                fetchData(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return userDetailList;
//    }

    //retrieve data
    public ArrayList<UserDetails> setUserData() {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
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
        return userDetailList;
    }

    //fetchData
    public void fetchData(DataSnapshot dataSnapshot) {

        userDetailList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserDetails userDetails = ds.getValue(UserDetails.class);
            userDetailList.add(userDetails);
        }
    }

    public void uploadImage(final Context context, Uri image, ImageButton btnImage) {

        if (image != null && btnImage!=null) {

            StorageReference filepath = storage.child("picture").child(image.getLastPathSegment());

            btnImage.setDrawingCacheEnabled(true);
            btnImage.buildDrawingCache();
            Bitmap bitmap = btnImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(context, "Can't upload...", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    UserDetails userDetails = new UserDetails();
                    userDetails.setImage(String.valueOf(downloadUrl));
                }
            });
        } else {
            Toast.makeText(context, "Image null", Toast.LENGTH_SHORT).show();
        }
    }
}
