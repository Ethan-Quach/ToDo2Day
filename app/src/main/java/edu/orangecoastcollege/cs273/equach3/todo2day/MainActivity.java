package edu.orangecoastcollege.cs273.equach3.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FOR NOW (TEMPORARY), delete the old database, then create a new one
        this.deleteDatabase(DBHelper.DATABASE_TABLE);

        DBHelper db = new DBHelper(this);

        // Creating a new task and adding it to the database:
        db.addTask(new Task(1, "Get Mechanic character to Level 100", 0));
        // (Hooray for anonymous inner classes!)
        db.addTask(new Task(2, "Get Wild Hunter character to Level 100", 0));
        db.addTask(new Task(3, "Maybe actually look at the CS273 book", 0));
        db.addTask(new Task(4, "Get the Halloween Mercy skin", 0));
        db.addTask(new Task(5, "Write Mahou Shoujo no 420", 0));

        // Getting all the tasks from the database and printing them using Log.i
        ArrayList<Task> allTasks = db.getAllTasks();
        // Loop through each task and print to Log.i
        for (Task t : allTasks) {
            Log.i("DATABASE TASK", t.toString());
        }
    }
}
