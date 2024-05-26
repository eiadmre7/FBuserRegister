package com.example.fbuserregister;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Firebase {
    private static final String TAG = " Firebase Comm";
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private boolean isCreated=false;
    public static final String UsersCollection = "new_Users";

    public static final String NameKey = "user_name";
    public static final String EmailKey = "user_email";
    public static final String PasswordKey = "user_password";
    public static final String ImageUriKey = "imageUri";
    public static final String PointsKey = "user_points";
    public static final String AdminKey = "admin";
    public static final String ImageFolder = "images";

    private static Map<String, Object> prepareData2Save(User user)
    {
        Map<String, Object> userHashMap = new HashMap<String, Object>();
        userHashMap.put(NameKey, user.getName());
        userHashMap.put(EmailKey, user.getEmail());
        userHashMap.put(PasswordKey, user.getPassword());
        userHashMap.put(ImageUriKey, user.getUserPic());
        userHashMap.put(PointsKey, user.getPoints());
        userHashMap.put(AdminKey, user.isAdmin());

        return userHashMap;
    }

    public static String add2Firebase(User user, CollectionReference mColctRef, final Context context)
    {
        Map<String, Object> data2save = prepareData2Save(user);

        DocumentReference document = mColctRef.document();
        String id = document.getId();
        document.set(data2save).
                addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d("Firebase-Save new", "Docment was saved successfuly");
                            Toast.makeText(context, "Docment was saved successfuly", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.w("Firebase-Save new", "Oy vey", task.getException());
                            Toast.makeText(context, "Oy vey:\n" +  task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return id;
    }
    public static void updateInFirebase(User user, DocumentReference mDocRef, final Context context)
    {
        Map<String, Object> data2save = prepareData2Save(user);
        mDocRef.set(data2save).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Log.d("Firebase-Save existing", "Docment was saved successfuly");
                    Toast.makeText(context, "Docment was saved successfuly", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.w("Firebase-Save existing", "Oy vey", task.getException());
                    Toast.makeText(context, "Oy vey:\n" +  task.getException(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //FireStoreCom
    // Utility functions

    public static FirebaseAuth getAuth() {
        if (AUTH == null)
            AUTH = FirebaseAuth.getInstance();
        return AUTH;
    }

    public static FirebaseFirestore getFisrestore() {
        if (FIRESTORE == null)
            FIRESTORE = FirebaseFirestore.getInstance();

        return FIRESTORE;
    }

    public CollectionReference getCollectionReference(String collection) {
        return getFisrestore().collection(collection);
    }


    public static boolean isUserSignedIn() {

        return getAuth().getCurrentUser() != null;

    }

    public static String authUserEmail() {
        return getAuth().getCurrentUser().getEmail();


    }

    // Methods for Authentication

    public void loginUser(String email, String password) {

        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: login success ");
                                } else {
                                    Log.d(TAG, "onComplete: login failed ");
                                }
                            }
                        });
    }

    public boolean createUser(String mail, String password) {
        getAuth().createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete:  register success");
                            isCreated=true;
                        } else
                            Log.d(TAG, "onComplete: " + task.getException());
                    }
                });
        return isCreated;
    }
    // set data in a specific document or create one.
    public void setFireStoreDocument(String collectionName, String documentName, Map<String, Object> map) {
        DocumentReference docRef = getCollectionReference(collectionName).document(documentName);
        // Note there are options for update & Set with Merge Flag
        // Also for a single element
        // shown here ->  set this item whether new or replace existing
        docRef.set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "onComplete:  added to document success");
                        else
                            Log.d(TAG, "onComplete:  added to document failed");
                    }
                });
    }

}
