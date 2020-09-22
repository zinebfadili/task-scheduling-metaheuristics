package mcp;

public class Task implements Comparable<Task>{

private Long deadline;
private Long period;
private Integer id;
private Integer WCET;
private Integer priority;

    @Override
    public int compareTo(Task t) {
        int c1 = this.getPeriod().compareTo(t.getPeriod());
        if(c1!=0)
        return c1;
        return this.getId().compareTo(t.getId());
    }
    
    public Task(Integer id, Integer WCET, Long d, Long p) {
    	this.id = id;
    	this.WCET = WCET;
    	this.deadline = d;
    	this.period = p;
    }
    
    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWCET() {
        return WCET;
    }

    public void setWCET(Integer wCET) {
        WCET = wCET;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Task(Long deadline, Long period, Integer id, Integer wCET, Integer priority) {
        this.deadline = deadline;
        this.period = period;
        this.id = id;
        WCET = wCET;
        this.priority = priority;
    }

}