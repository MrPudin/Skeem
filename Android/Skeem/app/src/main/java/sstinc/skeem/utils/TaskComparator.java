package sstinc.skeem.utils;

import org.joda.time.format.PeriodFormat;

import java.util.Comparator;

import sstinc.skeem.models.Task;

/**
 * This class helps compare Tasks using {@link Comparator}.
 *
 * @see Comparator
 */
public class TaskComparator implements Comparator<Task> {
    private Order order;
    private boolean isAscending;
    // Ways to order by
    public enum Order {NAME, SUBJECT, TASKTYPE, DEADLINE,
        DURATION, SCHEDULED_START, SCHEDULED_STOP}

    // Default constructor
    public TaskComparator() {
        // Set default order
        this.order = Order.NAME;
        this.isAscending = false;
    }

    /**
     * Sets the order to sort the tasks by based on the enum value given.
     * If it is ascending, the tasks are sorted by 0-9, A-z.
     *
     * @param order sort the tasks by this order
     * @param isAscending sort by ascending order
     */
    public void setSortBy(Order order, boolean isAscending) {
        this.order = order;
        this.isAscending = isAscending;
    }

    /**
     * Implemented function from {@link Comparator} to compare two tasks.
     * @param o1 task 1
     * @param o2 task 2
     * @return comparison value, -1, 0 or 1.
     */
    @Override
    public int compare(Task o1, Task o2) {
        int mul_val = this.isAscending ? 1 : -1;
        int c;
        switch (this.order) {
            case NAME:
                c = mul_val*o1.getName().compareTo(o2.getName());
                if (c == 0) {
                    c = mul_val*o1.getSubject().compareTo(o2.getSubject());
                }
                break;
            case SUBJECT:
                c = mul_val*o1.getSubject().compareTo(o2.getSubject());
                if (c == 0) {
                    c = mul_val*o1.getName().compareTo(o2.getName());
                }
                break;
            case DEADLINE:
                c = mul_val*o1.getDeadline().toString().compareTo(o2.getDeadline().toString());
                if (c == 0) {
                    c = mul_val*o1.getName().compareTo(o2.getName());
                }
                if (c == 0) {
                    c = mul_val*o1.getSubject().compareTo(o2.getSubject());
                }
                break;
            case DURATION:
                c = mul_val*PeriodFormat.getDefault().print(o1.getPeriodNeeded()).compareTo(
                        PeriodFormat.getDefault().print(o2.getPeriodNeeded()));
                if (c == 0) {
                    c = mul_val*o1.getName().compareTo(o2.getName());
                }
                if (c == 0) {
                    c = mul_val*o1.getSubject().compareTo(o2.getSubject());
                }
                break;
            case SCHEDULED_START:
                c = mul_val*o1.getScheduledStart().toString().compareTo(
                        o2.getScheduledStart().toString());
                // Order by SCHEDULED_STOP next
                if (c == 0) {
                    c = mul_val*o1.getScheduledStop().toString().compareTo(
                            o2.getScheduledStop().toString());
                }
                // Order by NAME last
                if (c == 0) {
                    c = mul_val*o1.getName().compareTo(o2.getName());
                }
                break;
            case SCHEDULED_STOP:
                c = mul_val*o1.getScheduledStop().toString().compareTo(
                        o2.getScheduledStop().toString());
                // Order by SCHEDULED_START next
                if (c == 0) {
                    c = mul_val*o1.getScheduledStart().toString().compareTo(
                            o2.getScheduledStart().toString());
                }
                // Order by NAME last
                if (c == 0) {
                    c = mul_val*o1.getName().compareTo(o2.getName());
                }
                break;
            default:
                // Default to name
                c = mul_val*o1.getName().compareTo(o2.getName());
                if (c == 0) {
                    c = mul_val*o1.getSubject().compareTo(o2.getSubject());
                }
        }
        return c;
    }
}
