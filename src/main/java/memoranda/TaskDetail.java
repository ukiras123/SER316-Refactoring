package main.java.memoranda;


/**
 * Added a new class to capture the detail of the task
 * 
 * @author Kiran
 *
 */
public class TaskDetail
    {
        private String text ="";
        private String description ="";
        private int priority =0;
        private long effort= 0;
        
        public TaskDetail(String text, String description, int priority, long effort)
        {
            this.text = text;
            this.description = description;
            this.priority = priority;
            this.effort = effort;
        }

        public String getText() {
            return text;
        }

        public String getDescription() {
            return description;
        }

        public int getPriority() {
            return priority;
        }

        public long getEffort() {
            return effort;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public void setEffort(long effort) {
            this.effort = effort;
        }
    }