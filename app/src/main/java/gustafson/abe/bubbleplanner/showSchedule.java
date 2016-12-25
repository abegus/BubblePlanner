package gustafson.abe.bubbleplanner;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by abegustafson on 4/11/16.
 */
public class showSchedule extends AppCompatActivity {
    private int[][] time_slots;
    // this is the schedule object that will hold the schedule information
    Schedule schedule;

    //7:45 AM to 10:00PM
    private int startTime = 465;
    private int endTime = 1320;
    String stTime;
    String enTime;
    private int curTime = startTime;

    /* stuff for the dashes */
    private View[] dashes;
    private View[] rightLines;
    private TextView[] times;
    private TextView top;
    private TextView bot;

    int numDashes = 20;
    int lineWidth = 3;
    float spaceBetween;
    //this is for what 15 minute interval to start at
    int offset = 0;

    //this is the height on my s4
    int sideHeight = 1490;
    int sideWidth = 80;

    RelativeLayout leftSide;
    RelativeLayout rightSide;
    TextView beginView;
    TextView endView;
    CheckBox checkBox;
    TextView tv;
    TextView qty;
    Paint paint = new Paint();
    //DrawClass drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_schedule);
        Intent intent = getIntent();
        schedule = (Schedule)intent.getSerializableExtra("Schedule");

        //this is where the sorting is going to happen...

        try {
            schedule.getOrderedTasks();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //beginView = (TextView)findViewById(R.id.begin_time);
        endView = (TextView)findViewById(R.id.end_time);
        top = (TextView)findViewById(R.id.startTime);
        bot = (TextView)findViewById(R.id.endTime);
        //TableLayout ll = (TableLayout) findViewById(R.id.table);
        rightSide = (RelativeLayout)findViewById(R.id.table);
        leftSide = (RelativeLayout)findViewById(R.id.linearLayout);
        //leftSide.getHeight();
        int height = leftSide.getHeight();



        getScheduleInformation();



        //all of the following stuff is for creating the "time frame" being showed
        //get the information needed given the time frame
        getTimeFrame();

        top.setText(stTime);
        bot.setText(enTime);

        /* initialize the stuff for the dashes */
        dashes = new View[numDashes-2];
        rightLines = new View[numDashes-2];
        times = new TextView[numDashes / 4];

        //minus 2 because the start and end dont need dashes
        spaceBetween = (float)(sideHeight - ((numDashes -2)*lineWidth)) / (numDashes-2);
        endView.setText(""+spaceBetween);

        //j is the counter for the times
        int j = 0;
        for(int i = 0; i < dashes.length; i++){

            //increment the current time by 15
            curTime += 15;

            rightLines[i] = new View(this);
            dashes[i] = new View(this);
            leftSide.addView(dashes[i]);
            rightSide.addView(rightLines[i]);

            float y = (float)( (i * lineWidth) + ((i+ 1) * spaceBetween));
            dashes[i].setY(y);
            rightLines[i].setY(y);

            //for the hour
            if(i % 4 == (offset % 4)) {
                dashes[i].setX(0);
                // create the times to put at the sides
                times[j] = new TextView(this);
                leftSide.addView(times[j]);
                times[j].setTextSize(12);
                times[j].setX(10);
                times[j].setY(y);
                times[j].setText(getTime(curTime));

                j++;
            }
            //15 min
            else if(i%4 == (offset + 1) % 4 || i%4 == (offset + 3) % 4){
                dashes[i].setX(160);
            }
            //30 min
            else {
                dashes[i].setX(80);
            }

            //hour/ half hour
            if(i%2 == (offset % 2) ) {
                rightLines[i].setX(0);
            }
            else{
                rightLines[i].setX(100000);//move it off screen
            }

            // set the transparency
            rightLines[i].setAlpha((float) 0.4);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)dashes[i].getLayoutParams();
            params.height = lineWidth;
            RelativeLayout.LayoutParams param1 = (RelativeLayout.LayoutParams)rightLines[i].getLayoutParams();
            param1.height = lineWidth;

            rightLines[i].setBackgroundColor(-16777216);
            dashes[i].setBackgroundColor(-16777216);


        }

        // this was to add stuff when the rightSide was still a tableLayout
        /* not needed?
        for (int i = 0; i <2; i++) {

            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            checkBox = new CheckBox(this);
            tv = new TextView(this);
            //addBtn = new ImageButton(this);
            //addBtn.setImageResource(R.drawable.add);
            //minusBtn = new ImageButton(this);
            //minusBtn.setImageResource(R.drawable.minus);
            qty = new TextView(this);
            checkBox.setText("hello");
            qty.setText("10");
            row.addView(checkBox);
            //row.addView(minusBtn);
            row.addView(qty);
            //row.addView(addBtn);
            //ll.addView(row,i);
        }*/



        //populate the schedule
        populateSchedule();

    }

    private void populateSchedule(){


        for(int i = 0; i < schedule.tasks.size();i++){
            float startY;
            float endY;
            float width;
            int num15periods;
            int num15fromstart;

            num15periods = schedule.getDuration(i) / 15;
            num15fromstart = (schedule.getStart(i) -startTime ) / 15;

            startY = (spaceBetween * ((float)(num15fromstart))) + (lineWidth * (num15fromstart));
            width = (spaceBetween * ((float)(num15periods))) + (lineWidth * (num15periods));

            TextView taskView = new TextView(this);
            taskView.setText(" " +schedule.getName(i)+ "  ["+getTime(schedule.getStart(i)) + " - "
                    + getTime(schedule.getEnd(i)) +"]");
            if(schedule.getDuration(i) > 30 && schedule.getDescription(i).length() > 1){
                taskView.setText(taskView.getText() + "\n Notes: "+ schedule.getDescription(i));
            }
            rightSide.addView(taskView);
            taskView.setX(1);
            taskView.setTextSize(12);
            taskView.setY(startY);
            taskView.setHeight((int) width);
            taskView.setWidth(710);

            taskView.setPadding(10,5,10,5);
            if( i == 1){
                //taskView.setWidth(300);
            }
            //taskView.setWidth(rightSide.getWidth());
            updateColor(schedule.getColor(i),taskView);

        }
    }

    private void updateColor(int color, TextView task){
        if( color == 1){
            task.setBackgroundResource(R.drawable.bg_regular);
        }
        else if( color == 2){
            task.setBackgroundResource(R.drawable.bg_green);
        }
        else if( color == 3){
            task.setBackgroundResource(R.drawable.bg_orange);
        }
        else if( color == 4){
            task.setBackgroundResource(R.drawable.bg_purple);
        }
        else if( color == 5){
            task.setBackgroundResource(R.drawable.bg_red);
        }
        else if( color == 6){
            task.setBackgroundResource(R.drawable.bg_yellow);
        }
        else if( color == 7){
            task.setBackgroundResource(R.drawable.bg_pink);
        }
        else if( color == 8){
            task.setBackgroundResource(R.drawable.bg_lime);
        }
        else if( color == 9){
            task.setBackgroundResource(R.drawable.bg_blue);
        }
    }

    private void getScheduleInformation(){

        int lowTime = 100000;
        int highTime = 0;
        for(int i = 0; i < schedule.tasks.size(); i++){
            if( schedule.getStart(i) < lowTime)
                lowTime = schedule.getStart(i);
            if(schedule.getEnd(i) > highTime)
                highTime = schedule.getEnd(i);
        }
        startTime = lowTime;
        endTime = highTime;
    }

    private String getTime(int time){
        String ret;
        boolean startAm;
        if(time >= 780) {
            time = time - 720;
            startAm = false;
        }
        else
            startAm = true;

        int startHourTemp = time / 60;
        int startMinTemp = time % 60;
        //780 is 1:00 pm

        ret = startHourTemp + ":" + startMinTemp;
        if(startMinTemp == 0)
            ret += "0";
        if(startAm)
            ret += " AM";
        else
            ret += " PM";

        return ret;
    }
    private void getTimeFrame(){
        int totalTime = endTime - startTime;
        int increments = totalTime / 15;
        //increments = increments - 2;
        numDashes = increments + 1;

        boolean startAm;
        boolean endAm;
        if(startTime >= 780) {
            startTime = startTime - 720;
            startAm = false;
        }
        else
            startAm = true;
        if(endTime >= 780) {
            endTime = endTime - 720;
            endAm = false;
        }
        else
            endAm = true;

        int startHourTemp = startTime / 60;
        int startMinTemp = startTime % 60;
        int endHourTemp = endTime / 60;
        int endMinTemp = endTime % 60;
        //780 is 1:00 pm

        stTime = startHourTemp + ":" + startMinTemp;
        enTime = endHourTemp + ":" + endMinTemp;
        if(startMinTemp == 0)
            stTime += "0";
        if(endMinTemp == 0)
            enTime += "0";
        if(startAm)
            stTime += " AM";
        else {
            stTime += " PM";
            startTime += 720;
        }
        if(endAm) {
            enTime += " AM";
        }
        else {
            enTime += " PM";
            endTime += 720;
        }
    }

}