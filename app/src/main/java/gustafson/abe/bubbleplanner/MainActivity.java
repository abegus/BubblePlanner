package gustafson.abe.bubbleplanner;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE = 112;

    public static String filename = "MySharedInt";
    public SharedPreferences someData;
    EditText contactListEditText;
    //SQLiteDatabase db = this.openOrCreateDatabase( "bubbleDB", MODE_PRIVATE,null);
    private SQLiteDatabase db = null;
    private File database;
    private Button addTaskButton;
    // make this be stored when the app closes and reloaded, if false, populate tables, esle dont
    private boolean hasCreated = false;

    Button[] buttons;
    int nextButtonIndex = 0;


    // this is the schedule object that will hold the schedule information
    Schedule schedule;

    //this will be for the bubbles
    int[] openButtons;
    //int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        someData = getSharedPreferences(filename,0);
        addTaskButton = (Button)this.findViewById(R.id.addTask);
        contactListEditText = (EditText) findViewById(R.id.contactListEditText);
    //initialize the buttons
        buttons = new Button[16];
        openButtons = new int[16];
        buttons[0] = (Button)this.findViewById(R.id.button0);
        buttons[1] = (Button)this.findViewById(R.id.button1);
        buttons[2] = (Button)this.findViewById(R.id.button2);
        buttons[3] = (Button)this.findViewById(R.id.button3);
        buttons[4] = (Button)this.findViewById(R.id.button4);
        buttons[5] = (Button)this.findViewById(R.id.button5);
        buttons[6] = (Button)this.findViewById(R.id.button6);
        buttons[7] = (Button)this.findViewById(R.id.button7);
        buttons[8] = (Button)this.findViewById(R.id.button8);
        buttons[9] = (Button)this.findViewById(R.id.button9);
        buttons[10] = (Button)this.findViewById(R.id.button10);
        buttons[11] = (Button)this.findViewById(R.id.button11);
        buttons[12] = (Button)this.findViewById(R.id.button12);
        buttons[13] = (Button)this.findViewById(R.id.button13);
        buttons[14] = (Button)this.findViewById(R.id.button14);
        buttons[15] = (Button)this.findViewById(R.id.button15);
        //hide all of the buttons
        for(int i = 0; i< buttons.length; i++){
            buttons[i].setVisibility(View.INVISIBLE);
            openButtons[i] = 0;
        }


        getSupportActionBar().hide();



        //create the schedule if this is the first time?
        schedule = new Schedule();

        openDatabase();

        /* check if database exists on phone. if not, it creates it, else it just opens it
        try {
            db = SQLiteDatabase.openDatabase("MyContacts.db", null,
                    SQLiteDatabase.OPEN_READWRITE);
            db.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        */
        //if null, then it doesn't exist
        if(db == null){
            //Toast.makeText(this,"Database Doesn't exist",Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(this,"Database does exist",Toast.LENGTH_SHORT).show();
            //openDatabase();

            // this was crossed out and openDatabase was above try
            //db = this.openOrCreateDatabase("MyContacts.db", MODE_PRIVATE, null);
        }


       // CHECK IF THERE ARE THINGS IN THE TABLES, IF NOT THEN CALL READINDATABASE
        Toast.makeText(this,"Start by adding a Task",Toast.LENGTH_LONG).show();

    }
    public void onSaveInstanceState(Bundle saveState){
        super.onSaveInstanceState(saveState);
        saveState.putSerializable("schedule", schedule);
    }

    public void onResume(Bundle inState) {
        super.onRestoreInstanceState(inState);
        schedule = (Schedule) inState.getSerializable("schedule");
    }

    public void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        schedule = (Schedule)inState.getSerializable("schedule");
        //updateButtons();

        //int name1 = inState.getInt("currentPicture");
        //System.out.println(name1 + " this is what i got");
        //loadData();
    }

    //THIS IS WHERE THE ACTIVITY GETS THE RESULTING INFORMATION
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == REQ_CODE){

            //came back from taskDetails
            String tName = intent.getStringExtra("taskName");
            int tKey = intent.getIntExtra("taskKey", -1);
            int tDuration = intent.getIntExtra("duration",-1);
            int isChecked = intent.getIntExtra("isChecked",-1);
            int color = intent.getIntExtra("Color",-1);
            String taskDescription = intent.getStringExtra("Description");
            int startTime = -1;
            int endTime = -1;

            //if isChecked = 1, then get start time and end time
            if(isChecked == 1){
                startTime = intent.getIntExtra("startTime",-1);
                endTime = intent.getIntExtra("endTime",-1);
            }
            populateBubble(tName,tKey,tDuration,isChecked,startTime,endTime,color,taskDescription);
        }
    }

    private void populateBubble(String tName, int tKey, int tDuration, int isChecked, int
                                startTime, int endTime,int color,String taskDescription){


        if(nextButtonIndex > 15){
            Toast.makeText(this,"No space to add another Task!",Toast.LENGTH_SHORT).show();
        }
        else{
            if(isChecked == 0)
                buttons[nextButtonIndex].setText(tName+ "*");
            else
                buttons[nextButtonIndex].setText(tName);
            //Toast.makeText(this,tKey,Toast.LENGTH_SHORT).show();
            buttons[nextButtonIndex].setVisibility(View.VISIBLE);
            // CALL UPDATE COLOR TO UPDATE THE COLOR OF THE BUBBLE
            updateColor(nextButtonIndex,color);

            //add it to the schedule object
            if(isChecked == 1)
                schedule.addTask(tName,tKey,tDuration,isChecked,startTime,endTime,color,taskDescription);
            else
                schedule.addTask(tName,tKey,tDuration,isChecked,color,taskDescription);

            nextButtonIndex++;
        }
    }

    public void removeTask(View view){
        for(int i = 0; i < buttons.length;i++){
            buttons[i].setClickable(true);
        }
        Toast.makeText(this,"Click a Task to remove it",Toast.LENGTH_SHORT).show();
    }
    //this is for when the buttons are clicked
    public void buttonClicked(View view){

        switch (view.getId()) {
            case R.id.button0:
                schedule.removeTask(0);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button1:
                schedule.removeTask(1);
                //Toast.makeText(this, keyNames[0], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                schedule.removeTask(2);
                //Toast.makeText(this, keyNames[1], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                schedule.removeTask(3);
                //Toast.makeText(this, keyNames[2], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                schedule.removeTask(4);
                //Toast.makeText(this, keyNames[3], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5:
                schedule.removeTask(5);
                //Toast.makeText(this, keyNames[4], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6:
                schedule.removeTask(6);
                //Toast.makeText(this, keyNames[5], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button7:
                schedule.removeTask(7);
                //Toast.makeText(this, keyNames[6], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button8:
                schedule.removeTask(8);
                //Toast.makeText(this, keyNames[7], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button9:
                schedule.removeTask(9);
                //Toast.makeText(this, keyNames[8], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button10:
                schedule.removeTask(10);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button11:
                schedule.removeTask(11);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button12:
                schedule.removeTask(12);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button13:
                schedule.removeTask(13);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button14:
                schedule.removeTask(14);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
            case R.id.button15:
                schedule.removeTask(15);
                //Toast.makeText(this, keyNames[9], Toast.LENGTH_SHORT).show();
                break;
        }
        for(int i = 0; i < buttons.length;i++){
            buttons[i].setClickable(false);
        }
        nextButtonIndex--;
        updateButtons();
    }

    private void updateButtons(){

        for(int i = 0; i < schedule.tasks.size();i++) {

            buttons[i].setText(schedule.getName(i));
            //Toast.makeText(this,tKey,Toast.LENGTH_SHORT).show();
            buttons[i].setVisibility(View.VISIBLE);
            // CALL UPDATE COLOR TO UPDATE THE COLOR OF THE BUBBLE
            updateColor(i, schedule.getColor(i));
        }
        for(int j = schedule.tasks.size(); j < buttons.length; j++){
            buttons[j].setVisibility(View.INVISIBLE);
        }

    }


    private void updateColor(int index, int color){
        if( color == 1){
            buttons[index].setBackgroundResource(R.drawable.round_button);
        }
        else if( color == 2){
            buttons[index].setBackgroundResource(R.drawable.round_button_green);
        }
        else if( color == 3){
            buttons[index].setBackgroundResource(R.drawable.round_button_orange);
        }
        else if( color == 4){
            buttons[index].setBackgroundResource(R.drawable.round_button_purple);
        }
        else if( color == 5){
            buttons[index].setBackgroundResource(R.drawable.round_button_red);
        }
        else if( color == 6){
            buttons[index].setBackgroundResource(R.drawable.round_button_yellow);
        }
        else if( color == 7){
            buttons[index].setBackgroundResource(R.drawable.round_button_pink);
        }
        else if( color == 8){
            buttons[index].setBackgroundResource(R.drawable.round_button_lime);
        }
        else if( color == 9){
            buttons[index].setBackgroundResource(R.drawable.round_button_blue);
        }
    }

    /* NOT USED
    public void saveData(){
        SharedPreferences.Editor editor = someData.edit();
        //editor.putInt("sharedInt", currentPicture);
        editor.commit();
    }
    public void loadData(){
        int dataReturned = someData.getInt("sharedInt", -1);
        //title.setText("The last image clicked on: [" + dataReturned + "]");
    }*/

    public void addTask(View view){
        /* go to the addTask class, ** STILL NEED TO SEND INFORMATION */
        Intent intent = new Intent(this,addTask.class);

        //start activity for result
        startActivityForResult(intent, REQ_CODE);
    }
    public void openInfo(View view){
        Intent intent = new Intent(this,infoPage.class);
        startActivity(intent);
    }
    public void createSchedule(View view) throws InterruptedException {
        db.close();
        /* go to the addTask class, ** STILL NEED TO SEND INFORMATION */
        if(schedule.tasks.size() < 1){
            Toast.makeText(this,"No Tasks Added... Nice try",Toast.LENGTH_SHORT).show();
        }
        else {
            //schedule.getOrderedTasks();
            Intent intent = new Intent(MainActivity.this, showSchedule.class);
            intent.putExtra("Schedule", schedule);
            startActivity(intent);
        }
    }

    /* UNIMPLEMENTED METHOD
    public void preShow(View view){
        // go to the addTask class, ** STILL NEED TO SEND INFORMATION
        Intent intent = new Intent(MainActivity.this,preShow.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/


    private void readInDatabase(){

        Scanner scan = new Scanner(getResources().openRawResource(R.raw.categories));
        int i = 0;
        String firstLine;
        /* this is for Categories */
        while (scan.hasNextLine()) {
            String name = scan.nextLine();
            if(i == 0){
                firstLine = name;
            }
            else{
                Scanner parser = new Scanner(name);
                int catID = parser.nextInt();
                String catName = parser.next();
                System.out.println("id :" + catID +"  and name: " + catName);
                db.execSQL("INSERT INTO Categories (catName) VALUES ('" + catName + "');");
            }
            i++;
        }
        scan.close();

        Scanner sc1 = new Scanner(getResources().openRawResource(R.raw.subcategories));
        i = 0;
        String subFirst;
        /* this is for SubCategories */
        while (sc1.hasNextLine()) {
            String name = sc1.nextLine();
            if(i == 0){
                subFirst = name;
            }
            else{
                Scanner parser = new Scanner(name);
                int subID = parser.nextInt();
                String subName = parser.next();
                int catID = parser.nextInt();
                db.execSQL("INSERT INTO SubCategories (subName, catID) Values ('"
                        + subName + "', '" + catID + "');");
                //System.out.println(name);
            }
            i++;
        }
        sc1.close();

        Scanner sc2 = new Scanner(getResources().openRawResource(R.raw.tasks));
        i = 0;
        String taskFirst;
        /* this is for tasks */
        while (sc2.hasNextLine()) {
            String name = sc2.nextLine();
            if(i == 0){
                taskFirst = name;
            }
            else{
                Scanner parser = new Scanner(name);
                int taskID = parser.nextInt();
                String taskName = parser.next();
                int subID = parser.nextInt();
                db.execSQL("INSERT INTO Tasks (taskName, subID) Values ('"
                        + taskName + "', '" + subID + "');");
                //System.out.println(name);
            }
            i++;
        }
        sc2.close();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void deleteDatabase(View view) {
        delete();
    }
    public void delete(){
        db.deleteDatabase(database);
        contactListEditText.setText("");
        //Toast.makeText(this, "Database Deleted", Toast.LENGTH_SHORT).show();
        db.close();
    }

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

            // The database on the file system
            database = getApplicationContext().getDatabasePath("MyContacts.db");



            //check if there is anything in the tables, if not, add stuff
            Cursor cursor = db.rawQuery("SELECT * FROM Tasks",null);
            cursor.moveToFirst();
            int counter = 0;

            // Verify that we have results
            if (cursor != null && (cursor.getCount() > 0)) {
                do {
                    // Get the results and store them in a String
                    counter++;

                } while (cursor.moveToNext());
            } else {

                //Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
                readInDatabase();

            }
            if(counter > 9) {//then there are too many entries in the database. delete it and restart this
                delete();
            }
            cursor.close();

            db.close();
            // Check if the database exists
            if (database.exists()) {
                //Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();
                openDatabase();

            }

        }
        catch(Exception e){
            Log.e("CONTACTS ERROR", "Error Creating Database");
            e.printStackTrace();
        }

        //close database
        db.close();
    }

}
