package io.cadi.souklou.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by arcadius on 23/08/16.
 */
public class DbConstant {

    public static final StorageReference FIREBASE_STORAGE = FirebaseStorage.getInstance().getReferenceFromUrl("gs://souklou-15ea7.appspot.com");
    public static final StorageReference FIREBASE_STORAGE_CHILD = FIREBASE_STORAGE.child("souklou_images/Child");

    //All constant is static and write in uppercase
    //Be careful TABLE is referring a reference in firebase case
    public static final DatabaseReference FIREBASE_DB = FirebaseDatabase.getInstance().getReference("souklouDb");
    public static final String TABLE_ROOT = "souklou";
    public static final String TABLE_PARENT = "parent";
    public static final String TABLE_CHILDREN = "children";
    public static final String TABLE_MARK = "mark";
    public static final String TABLE_COURSE = "course";
    public static final String TABLE_SCHOOL = "school";
    public static final String TABLE_CLASSROOM = "classroom";
    public static final String TABLE_TYPEMARK = "typeMark";
    public static final String TABLE_TYPESESSION = "typeSession";
    public static final String TABLE_METACALENDAR = "metaCalendar";
    public static final String TABLE_AREA = "area";
}
