package gustafson.abe.bubbleplanner;

import java.io.Serializable;

/**
 * Created by abegustafson on 4/13/16.
 */
public class Task implements Serializable {
    public int duration;
    //true or false,
    public boolean isSet;
    public int startTime;
    public int endTime;
    public String taskName;
    public String description;
    public int color;
    public int taskKey;
    /* 0 = School, 1 = recreation.... so on */
    public int taskType;

    public Task(String tName, int tKey, int tDuration, int isChecked, int
            start, int end,int colo, String taskDescription){

        duration = tDuration;
        isSet = true;
        startTime = start;
        endTime = end;
        taskName = tName;
        description = taskDescription;
        color = colo;
        taskKey = tKey;
        taskType = colo;
    }
    public Task(String tName, int tKey, int tDuration, int isChecked,int
            colo, String taskDescription){

        duration = tDuration;
        isSet = false;
        taskName = tName;
        description = taskDescription;
        color = colo;
        taskKey = tKey;
        taskType = colo;
    }
}
