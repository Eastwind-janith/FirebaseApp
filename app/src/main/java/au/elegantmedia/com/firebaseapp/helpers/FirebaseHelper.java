package au.elegantmedia.com.firebaseapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import au.elegantmedia.com.firebaseapp.activities.MainActivity;
import au.elegantmedia.com.firebaseapp.models.UserDetails;

/**
 * Created by Nisala on 9/21/17.
 */


public class FirebaseHelper {

    private static final String TAG = "tag";
    DatabaseReference db;
    StorageReference storage;
    Uri downloadUrl;
    Boolean save;

    String key;
    ArrayList<UserDetails> userDetailList = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db, StorageReference storage) {
        this.db = db;
        this.storage = storage;
    }

    //Write Data
    public boolean save(UserDetails userDetails) {

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
                fetchData(dataSnapshot);
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
            userDetails.setKey(ds.getKey());
            userDetailList.add(userDetails);
        }
    }

    public void updateDate(UserDetails userDetails) {
        DatabaseReference newDb = db.child("UserDetails").child(userDetails.getKey());
//
//        newDb.child("name").removeValue();
//        newDb.child("age").removeValue();
//        newDb.child("")
        newDb.setValue(userDetails);

    }


    public void uploadImage(final Context context, Uri image, ImageButton btnImage, final UserDetails userDetails) {

        if (image != null && btnImage != null) {

            StorageReference filepath = storage.child("picture").child(image.getLastPathSegment());

            //btnImage.setDrawingCacheEnabled(true);
            //btnImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) btnImage.getDrawable()).getBitmap();
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
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    userDetails.setImage(String.valueOf(downloadUrl));


                    if (userDetails.getKey() != null) {

                        updateDate(userDetails);
                    } else {

                        if (save(userDetails)) {

                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                }

            });
        } else {
            Toast.makeText(context, "Image null", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(Context context,UserDetails userDetails) {

        DatabaseReference mDb = db.child("UserDetails").child(userDetails.getKey());
        mDb.removeValue();

    }
}
