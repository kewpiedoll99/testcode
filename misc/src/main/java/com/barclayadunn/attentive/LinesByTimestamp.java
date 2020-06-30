package com.barclayadunn.attentive;

import java.io.*;
import java.util.*;

/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.

 * I want you to return lines between a date range from a log file
 *
 * File:
 *   List<Line> lines

 * Line:
 *   Long timestamp
 *   String data
 */

class LinesByTimestamp {
    public static void main(String[] args) {
        List<Line> lines = new ArrayList<>();
        File file = new File(lines);
        getDateRangeLines(file, 1234567890L, 1234567891L);
    }

    public static class File {
        List<Line> lines;

        public File(List<Line> lines) {
            this.lines = lines;
        }

        public List<Line> getLines() {
            return lines;
        }
    }

    public class Line {
        Long timestamp;

        public Long getTimestamp() {
            return timestamp;
        }
    }

    private static List<Line> getDateRangeLines(File file, Long start, Long end) {
        List<Line> lines = file.getLines();
        return getSublist(lines, start, end);
    }


    // split at halfway
    // look at left half
    // if last long < start
    // look right half


    private static List<Line> getSublist(List<Line> lines, Long start, Long end) {
        int median = lines.size()/2;
        int startIndex = -1, endIndex = -1;
        List<Line> leftLines = lines.subList(0, (median - 1));
        List<Line> rightLines = lines.subList((median + 1), (lines.size() -1));

        // get start
        if (lines.get(median).getTimestamp().equals(start)) {
            startIndex = median;
        } else if (lines.get(median).getTimestamp() > start) {
            // look at left half
            startIndex = getIndexForTimestamp(leftLines, start);
        } else {
            // look at right half
            startIndex = getIndexForTimestamp(rightLines, start);
        }

        // get end
        if (lines.get(median).getTimestamp().equals(end)) {
            endIndex = median;
        } else if (lines.get(median).getTimestamp() > end) {
            // look at left half
            endIndex = getIndexForTimestamp(leftLines, end);
        } else {
            // look at right half
            endIndex = getIndexForTimestamp(rightLines, end);
        }

        return lines.subList(startIndex, endIndex);
    }

    private static int getIndexForTimestamp(List<Line> lines, Long ts) {
        int median = lines.size()/2;
        List<Line> leftLines = lines.subList(0, (median - 1));
        List<Line> rightLines = lines.subList((median + 1), (lines.size() -1));
        if (lines.get(median).getTimestamp().equals(ts)) {
            return median;
        } else if (lines.get(median).getTimestamp() > ts) {
            // look at left half
            return getIndexForTimestamp(leftLines, ts);
        }
        // look at right half
        return getIndexForTimestamp(rightLines, ts);
    }
}
