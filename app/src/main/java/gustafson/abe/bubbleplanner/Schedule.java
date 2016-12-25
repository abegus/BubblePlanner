package gustafson.abe.bubbleplanner;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by abegustafson on 4/13/16.
 */
public class Schedule implements Serializable{
    private int num_tasks;
    //private Task[] tasks;
    public ArrayList<Task> tasks;
    //public ArrayList<Task> orderedTasks;
    private int size;

    /* constructor */
    public Schedule(){
        tasks = new ArrayList<>();

    }

    public int getSize(){
        return size;
    }
    public String getName(int i){
        return tasks.get(i).taskName;
    }
    public String getDescription(int i){
        return tasks.get(i).description;
    }
    public int getColor(int i){
        return tasks.get(i).color;
    }
    public int getDuration(int i){
        return tasks.get(i).duration;
    }
    public int getStart(int i){
        return tasks.get(i).startTime;
    }
    public int getEnd(int i){
        return tasks.get(i).endTime;
    }
    public boolean isSet(int i){
        return tasks.get(i).isSet;
    }
    public void setStartTime(int i, int time){
        tasks.get(i).startTime = time;
        return;
    }
    public void setEndTime(int i, int time){
        tasks.get(i).endTime = time;
        return;
    }


    public void removeTask(int index){
        tasks.remove(index);
        size -=1;
    }

    //this is for a set time task
    public void addTask(String tName, int tKey, int tDuration, int isChecked, int
            startTime, int endTime,int color, String taskDescription){

        Task t = new Task(tName,tKey,tDuration,isChecked,startTime,endTime,color,taskDescription);
        tasks.add(t);
        size = size + 1;
    }
    //this is for a unbound time task
    public void addTask(String tName, int tKey, int tDuration, int isChecked,int
                        color, String taskDescription){

        Task t = new Task(tName,tKey,tDuration,isChecked,color,taskDescription);
        tasks.add(t);

        size+=1;
    }

/* still need to handle cases where there is only unbound tasks and no bounded ones */
    public void getOrderedTasks() throws InterruptedException {
        //temp array list to hold the set tasks (and eventually everything)
        ArrayList<Task> orderedTasks= new ArrayList<>();
        //temp array list to hold the unbound tasks (to fit into the one above)
        ArrayList<Task> unboundTasks = new ArrayList<>();
        //temp array to order everything initially
        ArrayList<Task> tempTasks = (ArrayList<Task>)tasks.clone();
        int counter = 0;

        //put things into orderedTasks in start/end order. put things into
        //unboundTasks with longest time first
        while(!tempTasks.isEmpty()){

            int unboundIndex = 0;
            int orderedIndex = 0;
            //for(int i = 0; i < tempTasks.size(); i++){
            Task ta = tempTasks.remove(0);
                //for the set time case
                if(ta.isSet){
                    //base insertion
                    if(orderedTasks.size() == 0){
                        orderedTasks.add(0,ta);
                    }
                    //else loop through and put it where it needs to be.
                    else{
                        boolean inserted = false;
                        for(int x = 0; x < orderedTasks.size(); x++){
                            if(ta.endTime <= orderedTasks.get(x).startTime){
                                orderedTasks.add(x,ta);
                                inserted = true;
                            }

                        }
                        if(!inserted)
                            orderedTasks.add(orderedTasks.size(),ta);
                    }
                }
                //for the unbound time case
                else{
                    //base insertion
                    if(unboundTasks.size() == 0){
                        //Task ta = tempTasks.remove(i);
                        unboundTasks.add(0,ta);
                    }
                    //else loop through and put it where it needs to be.
                    else{
                        boolean inserted = false;
                        for(int x = 0; x < unboundTasks.size(); x++){
                            if(ta.duration > unboundTasks.get(x).duration){
                                unboundTasks.add(x,ta);
                                inserted = true;
                            }
                        }
                        if(!inserted)
                            unboundTasks.add(unboundTasks.size(),ta);
                    }
                }

           // }
            /*
            System.out.println("top: "+counter+" task:"+ta.taskName + " sizes: "+unboundTasks.size() + " "+orderedTasks.size());
            counter++;
            try {
                Thread.sleep(500);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/

        }
        //System.out.println("EVERYTHING IS IN, CALL THE RECURSIVE ORDERING");
        int z = recursiveOrdering(unboundTasks,orderedTasks);



        //return orderedTasks;
        return;
    }
    private int recursiveOrdering(ArrayList<Task> unboundTasks,ArrayList<Task> orderedTasks) throws InterruptedException {

        //loop through and put tasks wherever they can fit in the orderedTasks
        int counter = 0;
        //System.out.println("in recursiveOrdering...");
        while(unboundTasks.size() > 0) {
            Task task = unboundTasks.remove(0);

            boolean inserted = false;
            //loop through and see where you can put this task
            for(int i = 0; i < orderedTasks.size();i++){
                //if this is not the last time in ordered tasks to look at, then check in between
                if(i < (orderedTasks.size() - 1)) {
                    int timeBetween = orderedTasks.get(i + 1).startTime - orderedTasks.get(i).endTime;
                    //if it will fit, then put it there
                    if (task.duration <= timeBetween){
                        task.startTime = orderedTasks.get(i).endTime;
                        task.endTime = task.startTime + task.duration;
                        orderedTasks.add(i+1,task);
                        i = orderedTasks.size();
                    }

                }
                //else just add it on to the end...
                else{
                    task.startTime = orderedTasks.get(i).endTime;
                    task.endTime = task.startTime + task.duration;
                    orderedTasks.add(i+1,task);
                    i = orderedTasks.size();
                }
                //System.out.println(counter +" in the recursive " + i + "   and the task name: "+task.taskName +" arraysize: "+orderedTasks.size());
                //wait(10);
                /*
                try {
                    Thread.sleep(500);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }*/
                counter++;
            }

        }
        tasks = (ArrayList<Task>)orderedTasks.clone();
        return 0;
    }

    private void mostEfficient(ArrayList<Task> unboundTasks){

    }
}
