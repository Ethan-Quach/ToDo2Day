package edu.orangecoastcollege.cs273.equach3.todo2day;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper database;
    private List<Task> taskList;
    private TaskListAdapter taskListAdapter;

    private EditText taskEditText;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DBHelper(this);

        // Filling in the list with tasks from the database
        taskList = database.getAllTasks();

        // Creating a custom task list adapter for the task list
        // Recall that ListAdapters need a context, a layout format, and the list itself
        taskListAdapter = new TaskListAdapter(this, R.layout.task_item, taskList);

        // Connecting the ListView to the layout in .xml
        taskListView = (ListView) findViewById(R.id.taskListView);

        taskEditText = (EditText) findViewById(R.id.taskEditText);

        // Associating adapter with ListView
        taskListView.setAdapter(taskListAdapter);
    }

    public void addTask (View view)
    {
        String description = taskEditText.getText().toString();
        if (description.isEmpty())
        {
            Toast.makeText(this, "Task description cannot be empty, you dingus.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Making a new Task
            Task newTask = new Task (description, 0);
            // Adding newTask to the database
            database.addTask(newTask);
            // Adding newTask to the Adapter
            taskListAdapter.add(newTask);

            taskEditText.setText("");

            taskListAdapter.notifyDataSetChanged();
        }
    }

    public void clearAllTasks (View view)
    {
        // Clear the List
        taskList.clear();

        // Clear the database table records
        database.deleteAllTasks();

        taskListAdapter.notifyDataSetChanged();
    }

    public void changeTaskStatus (View view)
    {
        if (view instanceof CheckBox) // Checking if view is actually a CheckBox
        {
            CheckBox selectedCheckBox = (CheckBox) view;
            Task selectedTask = (Task) selectedCheckBox.getTag();

            selectedTask.setIsDone(selectedCheckBox.isChecked() ? 1 : 0);
            database.updateTask(selectedTask);

            taskListAdapter.notifyDataSetChanged();
        }
    }
}
