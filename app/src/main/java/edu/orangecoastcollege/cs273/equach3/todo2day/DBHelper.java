package edu.orangecoastcollege.cs273.equach3.todo2day;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelper extends SQLiteOpenHelper {

    //TASK 1: DEFINE THE DATABASE VERSION, NAME AND TABLE NAME
    private static final String DATABASE_NAME = "ToDo2Day";
    static final String DATABASE_TABLE = "Tasks";
    private static final int DATABASE_VERSION = 1;


    //TASK 2: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLE
    private static final String KEY_FIELD_ID = "id";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_IS_DONE = "is_done";


    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIELD_DESCRIPTION + " TEXT, "
                + FIELD_IS_DONE + " INTEGER" + ")";
        database.execSQL (table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    public void addTask(Task newTask)
    {
        // Step 1: Create a reference to our database
        // this refers to DBHelper, which is a SQLiteOpenHelper.
        // DBHelper already has information about the database itself (i.e. ToDo2Day)
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 2: Create a key-value pair for each value you want to insert
        ContentValues values = new ContentValues();
        values.put(FIELD_DESCRIPTION, newTask.getDescription());
        values.put(FIELD_IS_DONE, newTask.getIsDone());

        // Step 3: Insert the value into the database
        db.insert(DATABASE_TABLE, null, values);

        // Step 4: Always remember to close the database!
        db.close();
    }

    // Creating a method to get all tasks in database:
    public ArrayList<Task> getAllTasks()
    {
        // Step 1: Create reference to database
        // Note that we use getReadableDatabase() since we aren't modifying records
        SQLiteDatabase db = this.getReadableDatabase();

        // Step 2: Make a new empty ArrayList
        ArrayList<Task> allTasks = new ArrayList<>();

        // Step 3: Query the database for all records (all rows) and all fields (all columns)
        // The return type of a query is Cursor
        Cursor results = db.query(DATABASE_TABLE,
                null, null, null, null, null, null);
        // Uh... You can use null as a return type if you don't want to restrict anything.
        // The first null is which fields we want to get back, sent as a String array.
        // Will have to look into this later.

        // Step 4: Loop through results, creating Task objects, and adding to ArrayList;
        // moveToFirst() checks if the first entry is there (i.e. if entries exist at all)
        if (results.moveToFirst())
        {
            do {
                // getType(int i) returns a value of a row, taking in the column index as the param.
                int id = results.getInt(0);
                String description = results.getString(1);
                int isDone = results.getInt(2);

                allTasks.add(new Task(id, description, isDone));
            } while (results.moveToNext());
        }

        // Step 4: Close the database!
        db.close();

        return allTasks;
    }

    public void updateTask (Task existingTask)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIELD_DESCRIPTION, existingTask.getDescription());
        values.put(FIELD_IS_DONE, existingTask.getIsDone());

        // update() takes four parameters. In order:
        // The table that's being edited, the values that should be changed (ContentValues),
        // the record that needs to be modified (identified by primary key for that row),
        // and the primary keys, sent in as a String array.
        db.update(DATABASE_TABLE,
                values,
                KEY_FIELD_ID + "=?",
                new String[] {String.valueOf(existingTask.getId())});

        db.close();
    }

    public void deleteAllTasks ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }
}
