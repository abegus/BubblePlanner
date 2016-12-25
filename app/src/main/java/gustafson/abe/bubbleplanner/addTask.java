package gustafson.abe.bubbleplanner;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;

public class addTask extends AppCompatActivity {

    private static final int REQ_CODE = 123;

    public static String filename = "MySharedInt";
    public SharedPreferences someData;
    //SQLiteDatabase db = this.openOrCreateDatabase( "bubbleDB", MODE_PRIVATE,null);
    SQLiteDatabase db;
    File database;

    /* these will hold the information for the database things */
    private Button[] buttons;
    private Button backButton;
    private int[] primaryKeys;
    private String[] keyNames;
    private int[] previousKeys;
    private String[] previousNames;
    private int[] buttonColors;
    private int tier1Size;
    int buttonCount = 9;

    /*this will hold the information of the previous buttons and states so that
    multiple calls to the db dont need to be made. just keeps track of previous
    level
     */
    private int previousLevel;
    int currentLevel = 0;
    int categoryLevel = -1;
    int subCategoryLevel = -1;
    int taskLevel = -1;
    int currentColor = -1;


    //private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        primaryKeys = new int[buttonCount];
        keyNames = new String[buttonCount];
        previousKeys = new int[buttonCount];
        previousNames = new String[buttonCount];
        buttons = new Button[buttonCount];
        buttonColors = new int[buttonCount];
        backButton = (Button)this.findViewById(R.id.backButton);
        buttons[0] = (Button)this.findViewById(R.id.button1);
        buttons[1] = (Button)this.findViewById(R.id.button2);
        buttons[2] = (Button)this.findViewById(R.id.button3);
        buttons[3] = (Button)this.findViewById(R.id.button4);
        buttons[4] = (Button)this.findViewById(R.id.button5);
        buttons[5] = (Button)this.findViewById(R.id.button6);
        buttons[6] = (Button)this.findViewById(R.id.button7);
        buttons[7] = (Button)this.findViewById(R.id.button8);
        buttons[8] = (Button)this.findViewById(R.id.button9);
        //buttons[11] = (Button)this.findViewById(R.id.button13);
        //buttons[9] = (Button)this.findViewById(R.id.button11);
        //buttons[10] = (Button)this.findViewById(R.id.button12);

        //MainActivity main = new MainActivity();
        //db = main.db;

        openDatabase();
        setup();
    }

    public void buttonPressed(View view){
        switch (view.getId()) {
            case R.id.button1:
                loadStuff(0);
                //Toast.makeText(this, keyNames[0], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                loadStuff(1);
                //Toast.makeText(this, keyNames[1], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                loadStuff(2);
                //Toast.makeText(this, keyNames[2], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                loadStuff(3);
                //Toast.makeText(this, keyNames[3], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5:
                loadStuff(4);
                //Toast.makeText(this, keyNames[4], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6:
                loadStuff(5);
                //Toast.makeText(this, keyNames[5], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button7:
                loadStuff(6);
                //Toast.makeText(this, keyNames[6], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button8:
                loadStuff(7);
                //Toast.makeText(this, keyNames[7], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button9:
                loadStuff(8);
                //Toast.makeText(this, keyNames[8], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button10:
                loadStuff(9);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            /*
            case R.id.button11:
                Toast.makeText(this, keyNames[10], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button12:
                Toast.makeText(this, keyNames[11], Toast.LENGTH_SHORT).show();
                break;*/

        }
        //Toast.makeText(this, "" + x, Toast.LENGTH_SHORT).show();
    }

    /* this is for when you click on one of the buttons and it goes to the lower level below*/
    private void loadStuff(int id){
        int[] tempKeys = new int[12];
        String[] tempNames = new String[12];

        //this means that it is currently categories level
        if(currentLevel == 0){
            currentColor = id+1;
            Cursor cursor = db.rawQuery("SELECT * FROM SubCategories WHERE catID = " + primaryKeys[id],null);
            int idColumn = cursor.getColumnIndex("subID");
            int nameColumn = cursor.getColumnIndex("subName");
            int counter = 0;
            cursor.moveToFirst();

            // Verify that we have results
            if (cursor != null && (cursor.getCount() > 0)) {

                do {
                    // Get the results and store them in a String
                    int subid = cursor.getInt(idColumn);
                    String name = cursor.getString(nameColumn);
                    //replace the _ with a space if there is one
                    name = name.replaceAll("_"," ");


                    buttons[counter].setText(name);
                    buttons[counter].setVisibility(View.VISIBLE);
                    //keyNames[counter] = name;
                    //primaryKeys[counter] = id;
                    tempKeys[counter] = subid;
                    tempNames[counter] = name;


                    counter++;
                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else {

                Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
                //contactListEditText.setText("");

            }
            /* increment level and make other buttons invisible*/
            for(int i = 0; i < buttons.length; i++){

                if( currentColor == 1){
                    buttons[i].setBackgroundResource(R.drawable.round_button);
                }
                else if( currentColor == 2){
                    buttons[i].setBackgroundResource(R.drawable.round_button_green);
                }
                else if( currentColor == 3){
                    buttons[i].setBackgroundResource(R.drawable.round_button_orange);
                }
                else if( currentColor == 4){
                    buttons[i].setBackgroundResource(R.drawable.round_button_purple);
                }
                else if( currentColor == 5){
                    buttons[i].setBackgroundResource(R.drawable.round_button_red);
                }
                else if( currentColor == 6){
                    buttons[i].setBackgroundResource(R.drawable.round_button_yellow);
                }
                else if( currentColor == 7){
                    buttons[i].setBackgroundResource(R.drawable.round_button_pink);
                }
                else if( currentColor == 8){
                    buttons[i].setBackgroundResource(R.drawable.round_button_lime);
                }
                else if( currentColor == 9){
                    buttons[i].setBackgroundResource(R.drawable.round_button_blue);
                }
                //then make the button invisible
                if(counter <= i)
                    buttons[i].setVisibility(View.INVISIBLE);
            }
            currentLevel++;
            //make the tempkeys and tempNames the primaries

            primaryKeys = tempKeys;
            keyNames = tempNames;
            //set the categoriesLevel to the respective id
            categoryLevel = id+1;
            //show the back button
            backButton.setVisibility(View.VISIBLE);

            cursor.close();
        }




        //this means that it is currently subcategories level
        else if(currentLevel == 1){
            int spot = id + 1;
            Cursor cursor = db.rawQuery("SELECT * FROM Tasks WHERE subID = " + primaryKeys[id],null);
            System.out.println(spot);
            //Cursor cursor = db.rawQuery("SELECT * FROM Tasks WHERE subID = " + spot,null);
            int idColumn = cursor.getColumnIndex("taskID");
            int nameColumn = cursor.getColumnIndex("taskName");
            int counter = 0;
            cursor.moveToFirst();

            // Verify that we have results
            if (cursor != null && (cursor.getCount() > 0)) {

                do {
                    // Get the results and store them in a String
                    int subid = cursor.getInt(idColumn);
                    String name = cursor.getString(nameColumn);
                    //replace the _ with a space if there is one
                    name = name.replaceAll("_"," ");

                    buttons[counter].setText(name);
                    buttons[counter].setVisibility(View.VISIBLE);
                    //keyNames[counter] = name;
                    //primaryKeys[counter] = id;
                    tempKeys[counter] = subid;
                    tempNames[counter] = name;

                    counter++;
                    // Keep getting results as long as they exist
                } while (cursor.moveToNext());
            } else {

                Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
                //contactListEditText.setText("");

            }
            /* increment level */
            for(int i = counter; i < buttons.length; i++){
                buttons[i].setVisibility(View.INVISIBLE);
            }
            currentLevel++;
            //make the tempkeys and tempNames the primary
            previousKeys = primaryKeys;
            previousNames = keyNames;
            primaryKeys = tempKeys;
            keyNames = tempNames;
            //set the categoriesLevel to the respective id
            categoryLevel = id+1;
            backButton.setVisibility(View.INVISIBLE);


            cursor.close();
        }


        //this means that it is a task level, and ready to be added into the schedule
        else{
            //do an intent to taskDetails to add to the Schedule object
             /* go to the addTask class, ** STILL NEED TO SEND INFORMATION */
            //Intent intent = new Intent(addTask.this,taskDetails.class);
           Intent intent = new Intent(this,taskDetails.class);

            //EditText editText = (EditText) findViewById(R.id.edit_message);
            int taskKey = primaryKeys[id];
            intent.putExtra("taskKey", taskKey);
            intent.putExtra("taskKeyName",keyNames[id]);
            intent.putExtra("Color",currentColor);
            startActivityForResult(intent, REQ_CODE);
        }
    }

    //THIS IS WHERE THE ACTIVITY GETS THE RESULTING INFORMATION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode == REQ_CODE){
            Intent returnIntent = new Intent();

            //came back from taskDetails
            String tName = intent.getStringExtra("taskName");
            int tKey = intent.getIntExtra("taskKey", -1);
            int tDuration = intent.getIntExtra("duration",-1);
            int isChecked = intent.getIntExtra("isChecked",-1);
            String taskDescription = intent.getStringExtra("Description");
            currentColor = intent.getIntExtra("Color",-1);
            returnIntent.putExtra("taskKey",tKey);
            returnIntent.putExtra("duration",tDuration);
            returnIntent.putExtra("isChecked",isChecked);
            returnIntent.putExtra("taskName",tName);
            returnIntent.putExtra("Color",currentColor);
            returnIntent.putExtra("Description",taskDescription);

            //if isChecked = 1, then get start time and end time
            if(isChecked == 1){
                int startTime = intent.getIntExtra("startTime",-1);
                int endTime = intent.getIntExtra("endTime",-1);
                returnIntent.putExtra("startTime",startTime);
                returnIntent.putExtra("endTime",endTime);
            }
            setResult(Activity.RESULT_OK,returnIntent);
            db.close();
            finish();
        }
    }

    public void goBack(View view){
        //if this is going to set it back to original state, then reset the whole thing
        if(currentLevel == 1){
           setup();

            currentLevel --;
            backButton.setVisibility(View.INVISIBLE);
        }

        //else go back to the subCategory view
        else if(currentLevel == 2){
            int i = 0;
            String value = previousNames[0];
            while(value != null){
                buttons[i].setText(value);
                i++;
                value = previousNames[i];
            }
            currentLevel--;
        }
    }

    public void setup(){
        Cursor cursor = db.rawQuery("SELECT * FROM Categories",null);
        int idColumn = cursor.getColumnIndex("catID");
        int nameColumn = cursor.getColumnIndex("catName");
        int counter = 0;
        cursor.moveToFirst();

        // Verify that we have results
        if (cursor != null && (cursor.getCount() > 0)) {

            do {
                // Get the results and store them in a String
                int id = cursor.getInt(idColumn);
                String name = cursor.getString(nameColumn);

                buttons[counter].setText(name);
                buttons[counter].setVisibility(View.VISIBLE);
                keyNames[counter] = name;
                primaryKeys[counter] = id;
                if( counter == 0){
                    buttons[counter].setBackgroundResource(R.drawable.round_button);
                }
                else if( counter == 1){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_green);
                }
                else if( counter == 2){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_orange);
                }
                else if( counter == 3){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_purple);
                }
                else if( counter == 4){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_red);
                }
                else if( counter == 5){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_yellow);
                }
                else if( counter == 6){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_pink);
                }
                else if( counter == 7){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_lime);
                }
                else if( counter == 8){
                    buttons[counter].setBackgroundResource(R.drawable.round_button_blue);
                }
                //String email = cursor.getString(emailColumn);

                //contactList = contactList + id + " : " + name + " : " +  "\n";


                counter++;
                // Keep getting results as long as they exist
            } while (cursor.moveToNext());
        } else {

            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
            //contactListEditText.setText("");

        }
        cursor.close();

    }

    public void taskDetails(View view){
        /* go to the addTask class, ** STILL NEED TO SEND INFORMATION */
        Intent intent = new Intent(addTask.this,taskDetails.class);
        startActivity(intent);
    }

    public void goSubCategory(){
        int id = 0;
        // Delete matching id in database
        db.execSQL("DELETE FROM contacts WHERE id = " + id + ";");
    }

    public void getContacts(View view) {


        // A Cursor provides read and write access to database results
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);

        // Get the index for the column name provided
        int idColumn = cursor.getColumnIndex("catID");
        int nameColumn = cursor.getColumnIndex("catName");
        int counter = 0;
        //int emailColumn = cursor.getColumnIndex("email");

        // Move to the first row of results
        cursor.moveToFirst();

        String contactList = "";

        // Verify that we have results
        if (cursor != null && (cursor.getCount() > 0)) {

            do {
                // Get the results and store them in a String
                int id = cursor.getInt(idColumn);
                String name = cursor.getString(nameColumn);

                buttons[counter].setText(name);
                keyNames[counter] = name;
                primaryKeys[counter] = id;
                //String email = cursor.getString(emailColumn);

                //contactList = contactList + id + " : " + name + " : " +  "\n";


                counter++;
                // Keep getting results as long as they exist
            } while (cursor.moveToNext());

            //contactListEditText.setText(contactList);

        } else {

            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
            //contactListEditText.setText("");

        }
        cursor.close();
    }



    /* shouldnt need this because we already have public database
    private void openDatabase(){
        try{

            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            db = this.openOrCreateDatabase("MyContacts.db", MODE_PRIVATE, null);

            // Execute an SQL statement that isn't select
            db.execSQL("CREATE TABLE IF NOT EXISTS contacts " +
                    "(id integer primary key, name VARCHAR, email VARCHAR);");

            // The database on the file system
            database = getApplicationContext().getDatabasePath("MyContacts.db");

            // Check if the database exists
            if (database.exists()) {
                Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e){

            Log.e("CONTACTS ERROR", "Error Creating Database");

        }
    }*/
    private void openDatabase(){
        try{
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            db = this.openOrCreateDatabase("MyContacts.db", MODE_PRIVATE, null);
            //create Categories table
            db.execSQL("CREATE TABLE IF NOT EXISTS Categories " +
                    "(catID integer primary key, catName VARCHAR);");
            //create SubCategories table
            db.execSQL("CREATE TABLE IF NOT EXISTS SubCategories " +
                    "(subID integer primary key, subName VARCHAR, catID integer," +
                    "FOREIGN KEY (catID) REFERENCES Categories(catID));");
            //create Tasks table
            db.execSQL("CREATE TABLE IF NOT EXISTS Tasks " +
                    "(taskID integer primary key, taskName VARCHAR, subID integer," +
                    "FOREIGN KEY (subID) REFERENCES SubCategories(subID));");
            /*db.execSQL("CREATE TABLE IF NOT EXISTS Tasks " +
                    "(taskID integer primary key, taskName VARCHAR, avgTime integer, subID integer," +
                    "FOREIGN KEY (subID) REFERENCES SubCategories(subID));");*/
            //create Schedule table, for holding schedule information
            /*db.execSQL("CREATE TABLE IF NOT EXISTS Schedule " +
                    "(subID integer primary key, subName VARCHAR, catID integer," +
                    "FOREIGN KEY (catID) REFERENCES Categories(catID));");*/

            // The database on the file system
            database = getApplicationContext().getDatabasePath("MyContacts.db");

            // Check if the database exists
            if (database.exists()) {
                //Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e){
            Log.e("CONTACTS ERROR", "Error Creating Database");
            e.printStackTrace();
        }
    }
}
