package gustafson.abe.bubbleplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by abegustafson on 4/11/16.
 *
 *
 *
 *
 * after this, use a bundle in mainActivity to keep track of the Schedule object
 */
public class taskDetails extends AppCompatActivity {


    private TextView taskNameText;
    private int taskKey;
    private String taskName;
    private String taskDescription;
    int setTime;

    AbsoluteLayout endTimeLayout;
    TextView timeBox;

    TextView duration;
    TextView description;
    Button cont;
    CheckBox setTimeBox;
    int currentColor;

    /* these vars are for the start */
    Button startUpMinute;
    Button startUpHour;
    Button startDownMinute;
    Button startDownHour;
    TextView startHour;
    TextView startMinute;
    ToggleButton startAP;

    /* these vars are for the end */
    Button endUpMinute;
    Button endUpHour;
    Button endDownMinute;
    Button endDownHour;
    TextView endHour;
    TextView endMinute;
    ToggleButton endAP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_details);
        Intent intent = getIntent();
        endTimeLayout = (AbsoluteLayout)this.findViewById(R.id.endTimeLayout);
        timeBox = (TextView)this.findViewById(R.id.timeBox);
        /* initialize the vars */
        startUpMinute = (Button)this.findViewById(R.id.startUpMinute);
        startUpHour = (Button)this.findViewById(R.id.startUpHour);
        startDownMinute = (Button)this.findViewById(R.id.startDownMinute);
        startDownHour = (Button)this.findViewById(R.id.startDownHour);
        startHour = (TextView)this.findViewById(R.id.startHour);
        startMinute = (TextView)this.findViewById(R.id.startMinute);
        startAP = (ToggleButton)this.findViewById(R.id.startAP);
        endUpMinute = (Button)this.findViewById(R.id.endUpMinute);
        endUpHour = (Button)this.findViewById(R.id.endUpHour);
        endDownMinute = (Button)this.findViewById(R.id.endDownMinute);
        endDownHour = (Button)this.findViewById(R.id.endDownHour);
        endHour = (TextView)this.findViewById(R.id.endHour);
        endMinute = (TextView)this.findViewById(R.id.endMinute);
        endAP = (ToggleButton)this.findViewById(R.id.endAP);

        duration = (TextView)this.findViewById(R.id.duration);
        description = (TextView)this.findViewById(R.id.description);
        cont = (Button)this.findViewById(R.id.cont);
        setTimeBox = (CheckBox)this.findViewById(R.id.setTime);

        taskNameText = (TextView)this.findViewById(R.id.taskName);

        taskKey = intent.getIntExtra("taskKey", -1);
        taskName = intent.getStringExtra("taskKeyName");
        currentColor = intent.getIntExtra("Color",-1);
        taskNameText.setText(taskName );


    }

    public void goBack(View view) {

        int timeDuration = calculateDuration();
        if (timeDuration < 15) {
            Toast.makeText(this, "Not a Valid Time Frame", Toast.LENGTH_SHORT).show();
        } else {
            /* go to the addTask class, ** STILL NEED TO SEND INFORMATION */
            Intent intent = new Intent();

            intent.putExtra("taskKey", taskKey);
            intent.putExtra("taskName",taskName);
            intent.putExtra("duration",timeDuration);
            intent.putExtra("Color",currentColor);
            //taskDescription = (String)description.getText();
           taskDescription = description.getText().toString();
            intent.putExtra("Description",taskDescription);

            //if yes, then it has a set time
            if (setTimeBox.isChecked()){
                intent.putExtra("isChecked",1);
                int startTime = 0;
                if(!startAP.isChecked()){
                    startTime += (60 * 12);
                }
                startTime = startTime + (Integer.parseInt((String) startHour.getText()) * 60);
                startTime += Integer.parseInt((String) startMinute.getText());
                int endTime = 0;
                if(!endAP.isChecked()){
                    endTime += (60*12);
                }
                endTime = endTime + (Integer.parseInt((String) endHour.getText()) * 60);
                endTime += Integer.parseInt((String) endMinute.getText());

                intent.putExtra("startTime",startTime);
                intent.putExtra("endTime",endTime);

            }
            else{
                intent.putExtra("isChecked",0);
            }
            //startActivity(intent);
            //return to addTask with information
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    public void checkClicked(View view){
        //if it is clicked, then show the regular start and stop
        if(setTimeBox.isChecked()) {
            showSetTime();
            startAP.setVisibility(View.VISIBLE);
        }
        else{
            //changed this temporarily
            //setTimeBox.setChecked(true);
            showUnboundTime();
            startAP.setVisibility(View.INVISIBLE);
        }

    }

    private int calculateDuration(){
        int totalTime = 0;
        int startTime = 0;
        int endTime = 0;

        if(!setTimeBox.isChecked()){
            totalTime = totalTime + (Integer.parseInt((String) startHour.getText()) * 60);
            totalTime += Integer.parseInt((String) startMinute.getText());
        }
        else{
            //for the start time
            if(!startAP.isChecked()){
                startTime += (60 * 12);
            }
            startTime = startTime + (Integer.parseInt((String) startHour.getText()) * 60);
            startTime += Integer.parseInt((String) startMinute.getText());
            if(!endAP.isChecked()){
                endTime += (60*12);
            }
            endTime = endTime + (Integer.parseInt((String) endHour.getText()) * 60);
            endTime += Integer.parseInt((String) endMinute.getText());

            totalTime = endTime - startTime;
        }
        return totalTime;
    }



    //for when there is a set time
    private void showSetTime(){
        endTimeLayout.setVisibility(View.VISIBLE);
        timeBox.setText("Start Time");
        timeBox.setTextSize(30);
        startHour.setText("" +7);
        startMinute.setText("" + 45);

    }

    //for when there is an unbound time
    private void showUnboundTime(){
        endTimeLayout.setVisibility(View.INVISIBLE);
        timeBox.setText("Time Duration");
        timeBox.setTextSize(22);
        startHour.setText("" +0);
        startMinute.setText("" +30);

    }


    public void startUpMinuteM(View view) {
        int min = Integer.parseInt((String) startMinute.getText());
        min += 15;
        min = min % 60;
        if(min == 0){
            startMinute.setText("00");
        }
        else
            startMinute.setText(""+min);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void startUpHourM(View view) {
        int hour = Integer.parseInt((String) startHour.getText());
        hour += 1;
        hour = hour % 13;


        if(hour == 0){
            if(setTimeBox.isChecked()){
                hour++;
            }
        }
        startHour.setText(""+hour);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void startDownHourM(View view) {
        int hour = Integer.parseInt((String) startHour.getText());
        hour -= 1;
        hour = hour % 13;
        if(hour < 1){
            hour = 12;
        }

        if(hour == 0){
            if(setTimeBox.isChecked()){
                hour--;
            }
        }
        startHour.setText(""+hour);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void startDownMinuteM(View view) {
        int min = Integer.parseInt((String) startMinute.getText());
        min -= 15;
        min = min % 60;
        if(min < 0){
            min = 45;
        }
        if(min == 0){
            startMinute.setText("00");
        }
        else
            startMinute.setText(""+min);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void endUpHourM(View view) {
        int hour = Integer.parseInt((String) endHour.getText());
        hour += 1;
        hour = hour % 13;


        if(hour == 0){
            if(setTimeBox.isChecked()){
                hour++;
            }
        }
        endHour.setText(""+hour);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void endUpMinuteM(View view) {
        int min = Integer.parseInt((String) endMinute.getText());
        min += 15;
        min = min % 60;
        if(min == 0){
            endMinute.setText("00");
        }
        else
            endMinute.setText(""+min);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void endDownHourM(View view) {
        int hour = Integer.parseInt((String) endHour.getText());
        hour -= 1;
        hour = hour % 13;
        if(hour < 1){
            hour = 12;
        }

        if(hour == 0){
            if(setTimeBox.isChecked()){
                hour--;
            }
        }
        endHour.setText(""+hour);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }
    public void endDownMinuteM(View view) {
        int min = Integer.parseInt((String) endMinute.getText());
        min -= 15;
        min = min % 60;
        if(min < 0){
            min = 45;
        }
        if(min == 0){
            endMinute.setText("00");
        }
        else
            endMinute.setText(""+min);
        int ret = calculateDuration();
        int sumHours = ret / 60;
        ret = ret % 60;
        duration.setText(sumHours+":"+ret);
    }

}
