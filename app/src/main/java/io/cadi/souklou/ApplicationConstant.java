package io.cadi.souklou;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by arcadius on 23/08/16.
 */
public class ApplicationConstant {

    //all constant is static and write in uppercase
    //public static String
    public static final String SHARE_PREFERENCE_NAME = "cadi.io";
    public static final DatabaseReference FIREBASE_DB = FirebaseDatabase.getInstance().getReference("souklouDb");
}
