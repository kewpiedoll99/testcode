package com.barclayadunn.compare;

import java.util.Date;

/**
 * User: barclayadunn
 * Date: 8/22/14
 * Time: 12:09 PM
 */
public class ClickRow implements Comparable<ClickRow> {

    private Date date = null;
    private String hour = null;
    private int count;

    ClickRow() {
    }

    public ClickRow(Date date, String hour, int count) {
        this.date = date;
        this.hour = hour;
        this.count = count;
    }

    public Date getDate() { return date; }
    public String getHour() { return hour; }
    public int getCount() { return count; }

    public int compareTo(ClickRow other) {
        if (this.date == null) {
            if (other.getDate() != null) {
                return -1;
            }
            return 0; // this better not happen
        }
        if (!this.date.equals(other.getDate())) {
            return this.date.compareTo(other.getDate());
        }
        if (Integer.parseInt(this.hour) < Integer.parseInt(other.getHour())) {
            return -1;
        }
        if (Integer.parseInt(this.hour) > Integer.parseInt(other.getHour())) {
            return 1;
        }
        if (this.count < other.getCount()) {
            return -1;
        }
        if (this.count > other.getCount()) {
            return 1;
        }
        return 0;
    }
}
