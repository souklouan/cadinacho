package io.cadi.souklou;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by arcadius on 23/08/16.
 */
public class AppConstant {

    //all constant is static and write in uppercase
    //public static String
    public static final String SHARE_PREFERENCE_NAME = "cadi.io";
    public static final DatabaseReference FIREBASE_DB = FirebaseDatabase.getInstance().getReference("souklouDb");

    public static final String PREF_AUTH_INFO = "authInfo";
    public static final String PREF_FAMILY_NAME = "familyName";
    public static final String PREF_PARENT_NAME = "givenName";
    public static final String PREF_AUTH_TYPE = "authType";//type : 0 for sms and 1 for google
}
