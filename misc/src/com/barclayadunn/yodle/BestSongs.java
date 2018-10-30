package com.barclayadunn.yodle;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: barclaydunn
 * Date: 3/25/13
 * Time: 3:05 PM
 */
public class BestSongs {

    private static final boolean DEBUG_REQUESTED = false;
    private static final double MAX_FREQUENCY = Math.pow(10, 12);
    private static final int MAX_SONGS_ON_ALBUM = 50000;
    private static final Pattern validator = Pattern.compile("[^a-z0-9_]*");

    public static void main(String[] args) {

        String input;
        String [] splitInput;

        int countSongsOnAlbum = 0;
        int countBestSongs = 0;

        int frequency;
        int firstSongFrequency = 0;
        String songTitle;
        int count = 0;

        TreeSet<Song> songs = new TreeSet<Song>();
        Scanner sc = new Scanner(System.in);
        Matcher matcher;

        while (!"".equals(input = sc.nextLine())) {
            splitInput = input.split(" ", 2);
//            for (String s : splitInput) { debug(s); }

            if (count < 1) {
                // first line contains two integers n and m: the number of songs on the album, and the number of songs to select
                try {
                    // check have two integers
                    countSongsOnAlbum = Integer.parseInt(splitInput[0]);
                    debug("countSongsOnAlbum: " + countSongsOnAlbum);
                    countBestSongs = Integer.parseInt(splitInput[1]);
                    debug("countBestSongs: " + countBestSongs);
                } catch (NumberFormatException nfe) {
                    System.err.println("Unacceptable input: First line should be two integers separated by a space.");
                    return;
                }
                // check submitted #s conform to this (1 ≤ n ≤ 50000, 1 ≤ m ≤ n)
                if (countSongsOnAlbum < 1 || countSongsOnAlbum > MAX_SONGS_ON_ALBUM) {
                    System.err.println("Unacceptable input: First integer should be between 1 and " + MAX_SONGS_ON_ALBUM + ".");
                    return;
                } else if (countBestSongs < 1 || countBestSongs > countSongsOnAlbum) {
                    System.err.println("Unacceptable input: Second integer should be between 1 and " + countSongsOnAlbum + " (inclusive).");
                    return;
                }

            } else {

                // Then follow n lines. The i’th of these lines contains an integer fi and string si,
                // where 0 ≤ fi ≤ 1012 is the number of times the i’th song was listened to, and si is the name of the song.
                try {
                    // check first is int
                    frequency = Integer.parseInt(splitInput[0]);
                } catch (NumberFormatException nfe) {
                    System.err.println("Unacceptable input: Expecting an integer and a String separated by a space.");
                    return;
                }
                if (splitInput.length < 2) {
                    System.err.println("Unacceptable input: Expecting a song title after the frequency.");
                    return;
                }
                songTitle = splitInput[1];

                // check frequency conforms to this (0 ≤ fi ≤ 1012)
                if (frequency < 0 || frequency > MAX_FREQUENCY) {
                    System.err.println("Unacceptable input: Frequency should be between 0 and " + MAX_FREQUENCY + ".");
                    return;
                }
                // Each song name is at most 30 characters long and consists only of the characters ‘a’-‘z’, ‘0’-‘9’, and underscore (‘_’).
                // check conforms to rule above
                matcher = validator.matcher(songTitle);
                if (matcher.matches()) {
                    System.err.println("Unacceptable input: Song title may only consist of a-z, 0-9, and _.");
                    return;
                }

                firstSongFrequency = count == 1 ? frequency : firstSongFrequency;

                // create song and add to TreeMap
                Song song = new Song(songTitle, frequency, count, firstSongFrequency);
                debug("Song: " + song.toString());
                songs.add(song);
            }
            count++;
            if (count > countSongsOnAlbum) {
                break;
            }
        }

        debug("first song frequency: " + firstSongFrequency);
        int i = 0;
        for (Song songOut : songs) {
            debug("quality: " + songOut.getQuality());
            System.out.println(songOut.getSongTitle());
            i++;
            if (i == countBestSongs) {
                break;
            }
        }
    }

    static void debug(String output) {
        if (DEBUG_REQUESTED) {
            System.out.println("debug: " + output);
        }
    }
}

class Song implements Comparable<Song> {
    private final String songTitle;
    private final BigDecimal bdFrequency;
    protected final BigDecimal bdOrderNumber;
    private final BigDecimal zipfPredictedFrequency; // firstSongFrequency / orderNumber
    private final BigDecimal quality;                // frequency / zipfPredictedFrequency

    Song(String songTitle, int frequency, int orderNumber, int firstSongFrequency) {
        this.songTitle = songTitle;
        this.bdOrderNumber = new BigDecimal(orderNumber);
        this.bdFrequency = new BigDecimal(frequency);

        BigDecimal bdFirstSongFrequency = new BigDecimal(firstSongFrequency);

        this.zipfPredictedFrequency = orderNumber != 0 ? bdFirstSongFrequency.divide(bdOrderNumber, BigDecimal.ROUND_HALF_EVEN) : new BigDecimal(0);
        this.quality = bdFrequency.divide(zipfPredictedFrequency, BigDecimal.ROUND_HALF_EVEN);
    }

    public String getSongTitle() { return songTitle; }
    public BigDecimal getQuality() { return quality; }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("songTitle: ").append(songTitle).append("; ");
        sb.append("frequency: ").append(bdFrequency).append("; ");
        sb.append("orderNumber: ").append(bdOrderNumber).append("; ");
        sb.append("zipfPredictedFrequency: ").append(zipfPredictedFrequency).append("; ");
        sb.append("quality: ").append(quality);
        return sb.toString();
    }

    // sorting in descending order
    public int compareTo(Song s2) {
        if (this.quality.compareTo(s2.quality) != 0) {
            return -(this.quality.compareTo(s2.quality));
        } else {
            return bdOrderNumber.compareTo(s2.bdOrderNumber);
        }
    }
}
