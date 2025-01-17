package mcp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Core {

    private int id;
    private boolean sorted = false;
    private boolean validWcrt = false;
    private double WCETFactor;
    private List<Task> tasks;

    public void sortTasks() {
        Comparator<Task> byPriority = Comparator.comparing(Task::getPriority);
        tasks.sort(byPriority);
    }

    public Core(int id, double WCETFactor) {
        this.id = id;
        this.WCETFactor = WCETFactor;
        this.tasks = new ArrayList<Task>();
    }

    public Core(int id, double WCETFactor, List<Task> tasks) {
        this.id = id;
        this.WCETFactor = WCETFactor;
        this.tasks = tasks;
    }

    public double getWCETFactor() {
        return WCETFactor;
    }

    public int getId() {
        return id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean addTask(Task t) {
        sorted = false;
        validWcrt = false;
        return tasks.add((Task) t);
    }

    public boolean addTaskList(List<Task> tasks) {
        sorted = false;
        boolean added = this.tasks.addAll(tasks);
        if (added) {
            sortTasks();
            sorted = true;
        }
        return added;
    }

    public Task getTaskByIndex(int idx) {
        validWcrt = false;
        return tasks.remove(idx);
    }

    public Task getRandomTask() {
        int i = (int) (Math.random() * tasks.size());
        validWcrt = false;
        return getTaskByIndex(i);
    }

    public Task swapRandomTask(Task t1) {
        sorted = false;
        validWcrt = false;
        int i = (int) Math.random() * tasks.size();
        Task t2 = getTaskByIndex(i);
        tasks.add(i, t1);
        return t2;
    }


    public int getUnschedulable() {
        int unschedulable = 0;
        if (!sorted) {
            Collections.sort(tasks);
            sorted = true;
        }
        if (!validWcrt) {
            calcWCRT();
        }

        Task task;
        for (int i = 0; i < tasks.size(); i++) {
            task = tasks.get(i);
            if (task.getWCRT() > task.getDeadline()) {
                unschedulable++;
            }
        }

        return unschedulable;

    }

    /*
     * calcWCRT calculates the Worst case reaction time for each task
     * and saves it in the task.
     * This will be called everytime the list of task changes.
     * */
    public void calcWCRT() {
        if (!sorted) {
            Collections.sort(tasks);
            sorted = true;
        }
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setWCRT((getWCRT(i)));

        }

        validWcrt = true;

    }

    // not here
    public void removeTaskById(int id) {
        int index = 0;
        for (Task aTask : tasks) {
            if (aTask.getId() == id) {
                break;
            }
            index++;
        }
        getTaskByIndex(index);
    }

    /*
     * getWCRT calculates the worst case reaction time of a task with the 
     * specified index.
     * The method used is discussed in Section 4.4.2 in the 
     * "Hard Real-Time--Periodic scheduling" chapter.
     * */
    public int getWCRT(int i) {

        long deadline = tasks.get(i).getDeadline();
        double ci = Math.ceil(tasks.get(i).getWCET() * WCETFactor);
        double interference, intSum, responseTime;
        interference = (i == 0) ? 0 : tasks.get(i - 1).getWCRT();
        //interference = 0;
        do {
            intSum = 0;
            responseTime = interference + ci;
            for (int j = 0; j < i; j++) {
                intSum += Math.ceil(responseTime / tasks.get(j).getPeriod())
                        * (tasks.get(j).getWCET() * WCETFactor);
            }
            interference = intSum;
        } while (interference + ci > responseTime && responseTime < deadline);

        return (int) Math.ceil(responseTime);

    }

    /*
     * calculates the laxity of the core
     * */
    public int getLaxity() {
        if (!validWcrt) {
            calcWCRT();
        }
        int laxity = 0;
        Task task;
        for (int i = 0; i < tasks.size(); i++) {
            task = tasks.get(i);
            laxity += task.getDeadline() - task.getWCRT();
        }
        return laxity;
    }
}
