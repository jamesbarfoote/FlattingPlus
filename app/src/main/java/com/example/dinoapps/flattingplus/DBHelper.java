package com.example.dinoapps.flattingplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by James on 11/17/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FlattingPlusDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "_id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_FLAT_GROUP = "flatgroup";
    public static final String USER_COLUMN_PICTURE = "pic";

    public static final String NOTE_TABLE_NAME = "notes";
    public static final String NOTE_COLUMN_ID = "_id";
    public static final String NOTE_COLUMN_USER_ID = "userID";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_CONTENT = "content";
    public static final String NOTE_COLUMN_CREATION_TIME = "created";
    public static final String NOTE_COLUMN_FLAT_GROUP = "flatgroup";

    public static final String FLATGROUP_TABLE_NAME = "fgroup";
    public static final String FLATGROUP_COLUMN_ID = "_id";
    public static final String FLATGROUP_COLUMN_GROUP_ID = "groupID";
    public static final String FLATGROUP_COLUMN_GROUP_NAME = "groupname";
    public static final String FLATGROUP_COLUMN_SHOPPINGLIST = "shoppingList";
    public static final String FLATGROUP_COLUMN_CALENDAR = "calendar";
    public static final String FLATGROUP_COLUMN_MONEY= "money";
    public static final String FLATGROUP_COLUMN_TODO_LIST= "todoList";
    public static final String FLATGROUP_COLUMN_PASS= "groupPass";
    public static final String FLATGROUP_COLUMN_OWNER_ID= "ownerID";
    public String email;
    public String todoList = "";

    private static final String createFlatGroup = "CREATE TABLE " + FLATGROUP_TABLE_NAME + "(" +
            FLATGROUP_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            FLATGROUP_COLUMN_GROUP_ID + " INTEGER, " +
            FLATGROUP_COLUMN_GROUP_NAME + " TEXT, " +
            FLATGROUP_COLUMN_SHOPPINGLIST + " TEXT," +
            FLATGROUP_COLUMN_CALENDAR + " TEXT," +
            FLATGROUP_COLUMN_MONEY + " TEXT," +
            FLATGROUP_COLUMN_TODO_LIST + " TEXT," +
            FLATGROUP_COLUMN_PASS + " TEXT," +
            FLATGROUP_COLUMN_OWNER_ID + " TEXT" + ")";

    private static final String createUser = "CREATE TABLE " + USER_TABLE_NAME + "(" +
            USER_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            USER_COLUMN_NAME + " TEXT, " +
            USER_COLUMN_EMAIL + " TEXT, " +
            USER_COLUMN_PICTURE + " TEXT," +
            USER_COLUMN_FLAT_GROUP + " TEXT" + ")";

    private static final String createNotes = "CREATE TABLE " + NOTE_TABLE_NAME + "(" +
            NOTE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
            NOTE_COLUMN_USER_ID + " INTEGER, " +
            NOTE_COLUMN_TITLE + " TEXT, " +
            NOTE_COLUMN_CONTENT + " TEXT," +
            NOTE_COLUMN_CREATION_TIME + " TEXT, " +
            NOTE_COLUMN_FLAT_GROUP + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUser);

        Log.v("Users table created", "");
        db.execSQL(createFlatGroup);
        Log.v("Group table created", "Group table created");

        db.execSQL(createNotes);
        Log.v("Notes table created", "Notes table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FLATGROUP_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNote(String userID, String title, String content, String flatGroup, String creationTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_USER_ID, userID);
        contentValues.put(NOTE_COLUMN_TITLE, title);
        contentValues.put(NOTE_COLUMN_CONTENT, content);
        contentValues.put(NOTE_COLUMN_CREATION_TIME, creationTime);
        contentValues.put(NOTE_COLUMN_FLAT_GROUP, flatGroup);
        db.insert(NOTE_TABLE_NAME, null, contentValues);
        return true;
    }

//    public boolean insertNote(String title, String content) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(NOTE_COLUMN_TITLE, title);
//        contentValues.put(NOTE_COLUMN_CONTENT, content);
//        db.insert(NOTE_TABLE_NAME, null, contentValues);
//        return true;
//    }

    public Cursor getNotes()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getNotesCount() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        long cnt  = DatabaseUtils.queryNumEntries(db, NOTE_TABLE_NAME);
//        db.close();
//        return cnt;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NOTE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
//        int cnt = 0;
//        if(cursor != null) {
//            cnt = cursor.getCount();
//        }

       // cursor.close();

        return cursor;
    }

    public boolean insertUser(String name, String email, String pic, String flatGroup) {
        clearUserTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_PICTURE, pic);
        contentValues.put(USER_COLUMN_FLAT_GROUP, flatGroup);
        db.insert(USER_TABLE_NAME, null, contentValues);
        this.email = email;
        return true;
    }

    public boolean updateUser(String name, String email, String pic, String flatGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_EMAIL, email);
        contentValues.put(USER_COLUMN_PICTURE, pic);
        contentValues.put(USER_COLUMN_FLAT_GROUP, flatGroup);
        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_ID + " = ? ", new String[]{email});
        return true;
    }

    public Cursor getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public String getGroup()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FLATGROUP_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            Log.d("in if", "");
            return cursor.getString(cursor.getColumnIndex("groupname"));

        }
        return "";
    }

    public String getEmail()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            Log.d("in if", "");
            return cursor.getString(cursor.getColumnIndex("email"));

        }
        return "";
    }

//    public int getUserEmail()
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor userC = db.rawQuery("SELECT userID FROM " + USER_TABLE_NAME + " WHERE " +
//                USER_COLUMN_ID + "=?", new String[] { Integer.toString(id) });
//
//        return userC.getInt(0);
//    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);
        return res;
    }

    public Cursor getAllGroup() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + FLATGROUP_TABLE_NAME, null);
        return res;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME,
                USER_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public boolean insertGroup(String groupName, String shoppingList, String calendar, String money, String todoList, String pass)
    {
        clearGroupTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLATGROUP_COLUMN_GROUP_NAME, groupName);
        contentValues.put(FLATGROUP_COLUMN_SHOPPINGLIST, shoppingList);
        contentValues.put(FLATGROUP_COLUMN_CALENDAR, calendar);
        contentValues.put(FLATGROUP_COLUMN_MONEY, money);
        contentValues.put(FLATGROUP_COLUMN_TODO_LIST, todoList);
        contentValues.put(FLATGROUP_COLUMN_PASS, pass);
        db.insert(FLATGROUP_TABLE_NAME, null, contentValues);

//        addGroupToUser(groupName);

        Cursor cursor = db.rawQuery("select todoList from fgroup", null);
        if(cursor.moveToFirst()){
            this.todoList = cursor.getString(0);
            Log.v("Stuff", "Stuff " + todoList);}

        return true;
    }

    public boolean updateGroup(String groupName, String shoppingList, String calendar, String money, String todoList, String pass)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLATGROUP_COLUMN_GROUP_NAME, groupName);
        contentValues.put(FLATGROUP_COLUMN_SHOPPINGLIST, shoppingList);
        contentValues.put(FLATGROUP_COLUMN_CALENDAR, calendar);
        contentValues.put(FLATGROUP_COLUMN_MONEY, money);
        contentValues.put(FLATGROUP_COLUMN_TODO_LIST, todoList);
        contentValues.put(FLATGROUP_COLUMN_PASS, pass);


        db.update(FLATGROUP_TABLE_NAME, contentValues, FLATGROUP_COLUMN_ID + " = ? ", new String[]{Integer.toString(1)});
        return true;
    }

    public boolean updateNotes(String notes)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLATGROUP_COLUMN_TODO_LIST, notes);
        db.update(FLATGROUP_TABLE_NAME, contentValues, FLATGROUP_COLUMN_ID + " = ? ", new String[]{Integer.toString(1)});
        return true;
    }

    public ArrayList<String> getGroupData()
    {
        ArrayList<String> groupData = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from fgroup", null);
        if ( cursor.moveToFirst()){
            do {
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    groupData.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return groupData;
    }

    public boolean groupExists(String gName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query ="SELECT * FROM fgroup";

        Cursor cursor = db.rawQuery(query,null);

        ArrayList<String> contents = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            contents.add(cursor.getString(0));
            cursor.moveToNext();
        }

        if(contents.contains(gName))
        {
            return true;
        }

        return false;
    }

    public void addGroupToUser(String groupname, int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_FLAT_GROUP, groupname);
        db.update(USER_TABLE_NAME, contentValues, "_id="+id, null);
//        db.update(USER_TABLE_NAME, contentValues, USER_COLUMN_FLAT_GROUP + " = ? ", new String[]{Integer.toString(0)});

    }


    public void clearUserTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(USER_TABLE_NAME, null,null);
        db.execSQL("delete from "+ USER_TABLE_NAME);
    }

    public void clearNotesTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME, null,null);
    }

    public void clearGroupTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FLATGROUP_TABLE_NAME, null,null);
    }

}
