package com.barclayadunn.squarespace;

import java.io.*;
        import java.time.Instant;
        import java.util.Map;
        import java.util.HashMap;
        import java.util.List;
        import java.util.ArrayList;
        import java.util.Arrays;

/**
 Meeting must start and end on a multiple of 5, i.e., 2:07 is not allowed
 Meetings must be five mins or more
 Meetings may nto be longer than 7 days
 Meeting rooms 1,2,3,4,5,6,7,8
*/
// assumptions:
// rounding seconds up 31 or greater, down 30 or less
// ~rounding start/end values, then calculating meeting length~ calc mtg length before rounding
class CandidateSolution {
    private int NO_AVAILABILITY = -1;
    private int INVALID_INPUT = -2;
    private int ONE_DAY = 86400; // seven days in sec 604800
    // week in min 10080
    private List<Integer> rooms = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

    // hashmap<room number, time of day>
    // times of day are epoch time for day plus (0, 5, 10, ... 86395
    // which correspond to 0:00 (midnight), 0:05, 0:10, ... 23:55
    // if a room is booked at any of the times in the 5 min increments in the span, it's unavailable
    private Map<Integer, List<Long>> roomBookings = new HashMap<>();

    public CandidateSolution() {
        for (int room : rooms) {
            roomBookings.put(room, new ArrayList<Long>());
        }
    }

    public int reserveRoom(long startEpochSeconds, long endEpochSeconds) {
        System.out.println("startEpochSeconds: " + startEpochSeconds + " endEpochSeconds: " + endEpochSeconds +
                " diff: " + (endEpochSeconds - startEpochSeconds));
        if (startEpochSeconds < 0 || endEpochSeconds < 0 ||
                endEpochSeconds < startEpochSeconds) {
            return INVALID_INPUT;
        }

        // is time a 5 min increment
        if (!isFiveMinIncrement(startEpochSeconds) || !isFiveMinIncrement(endEpochSeconds)) {
            return INVALID_INPUT;
        }
        long startEpochMin = getEpochMins(startEpochSeconds), endEpochMin = getEpochMins(endEpochSeconds);
        System.out.println("startEpochMin: " + startEpochMin + " endEpochMin: " + endEpochMin +
                " diff: " + (endEpochMin - startEpochMin));
        // is meeting at least 5 min long and <= 7 days long
        long meetingLengthMin = getMeetingLengthMin(startEpochMin, endEpochMin);
        if (meetingLengthMin < 5 || meetingLengthMin > (ONE_DAY * 7 / 60)) {
            return INVALID_INPUT;
        }

        // are there any conference rooms available during this time
        for (int room : rooms) {
            if (!roomAlreadyBooked(room, startEpochMin, endEpochMin)) {
                // book room
                bookRoom(room, startEpochMin, endEpochMin);
                return room;
            }
        }
        return NO_AVAILABILITY;
    }

    private synchronized void bookRoom(int room, long startEpochMin, long endEpochMin) {
        List<Long> bookedTimes = roomBookings.get(room);
        List<Long> fives = getEpochFives(startEpochMin, endEpochMin);
        for (long five : fives) {
            bookedTimes.add(five);
        }
    }

    private synchronized boolean roomAlreadyBooked(int room, long startEpochMin, long endEpochMin) {
        List<Long> bookedTimes = roomBookings.get(room);
        List<Long> fives = getEpochFives(startEpochMin, endEpochMin);
        for (long five : fives) {
            if (bookedTimes.contains(five)) {
                return true;
            }
        }
        return false;
    }

    private List<Long> getEpochFives(long startEpochMin, long endEpochMin) {
        ArrayList<Long> fives = new ArrayList<Long>();
        long increment = startEpochMin;
        while (increment < endEpochMin) {
            fives.add(increment);
            increment += 5;
        }
        return fives;
    }

    private long getMeetingLengthMin(long startEpochMin, long endEpochMin) {
        return endEpochMin - startEpochMin;
    }

    public long getEpochMins(long epochSeconds) {
        long epochMins = epochSeconds / 60;
        if (epochSeconds % 60 > 30) {
            epochMins += 1;
        }
        return epochMins;
    }

    public boolean isFiveMinIncrement(long epochSeconds) {
        return getEpochMins(epochSeconds) % 5 == 0;
    }
}

/**
 room ->          1   2   3   4   5   6   7   8
 time
 |
 V
 1592230800       X
 1592230805       X
 1592230810       X               X
 1592230815       X               X       X
 1592230820       X                       X   X
 ...

 */

public class SqSpSolution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        CandidateSolution solution = new CandidateSolution();

        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }

            String[] parts = line.trim().split(" ");
            Instant start = Instant.parse(parts[0]);
            Instant end = Instant.parse(parts[1]);

            int result = solution.reserveRoom(start.getEpochSecond(), end.getEpochSecond());

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        }

        bufferedReader.close();
        bufferedWriter.close();
    }
}

/**
 this one i can't figure out, afaict my answers are all correct, but their compiler says no

 startEpochSeconds: 1550397600 endEpochSeconds: 1552820400 diff: 2422800
 startEpochMin: 25839960 endEpochMin: 25880340 diff: 40380
 2/17/19 10:00:00 - 3/17/19 11:00:00
 meeting too long

 startEpochSeconds: 1550397600 endEpochSeconds: 1550397600 diff: 0
 startEpochMin: 25839960 endEpochMin: 25839960 diff: 0
 2/17/19 10:00:00 - 2/17/19 10:00:00
 meeting too short

 startEpochSeconds: 1550397600 endEpochSeconds: 1550397720 diff: 120
 2/17/19 10:00:00 - 2/17/19 10:02:00
 End is a non-five minute time

 startEpochSeconds: 1550397600 endEpochSeconds: 1550398920 diff: 1320
 2/17/19 10:00:00 - 2/17/19 10:22:00
 End is a non-five minute time

SECONDS?
 startEpochSeconds: 1550397600 endEpochSeconds: 1550398822 diff: 1222
 startEpochMin: 25839960 endEpochMin: 25839980 diff: 20
 2/17/19 10:00:00 - 2/17/19 10:20:22
 booked a room: 1

 startEpochSeconds: 1550397600 endEpochSeconds: 1550394000 diff: -3600
 2/17/19 10:00:00 - 2/17/19 09:00:00
 Ends before start!

 startEpochSeconds: 1550397600 endEpochSeconds: 1550401200 diff: 3600
 startEpochMin: 25839960 endEpochMin: 25840020 diff: 60
 2/17/19 10:00:00 - 2/17/19 11:00:00
 booked a room: 2

 startEpochSeconds: 1550397600 endEpochSeconds: 1551006000 diff: 608400
 startEpochMin: 25839960 endEpochMin: 25850100 diff: 10140
 2/17/19 10:00:00 - 2/24/19 11:00:00
 meeting too long

SECONDS?
 startEpochSeconds: 1550397622 endEpochSeconds: 1550398822 diff: 1200
 startEpochMin: 25839960 endEpochMin: 25839980 diff: 20
 2/17/19 10:00:22 - 2/17/19 10:20:22
 booked a room: 3
 */
