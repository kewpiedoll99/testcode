package com.barclayadunn.yodle;

import java.io.*;
import java.util.*;
import java.util.List;

/**
 * User: barclaydunn
 * Date: 4/3/13
 * Time: 4:17 PM
 *
 * solution to question at http://www.yodlecareers.com/puzzles/jugglefest.html
 */
public class Jugglers {

    static boolean debugRequested = true;
    static Map<Integer, Circuit> circuitMap;
    static List<Juggler> jugglerList;

    public static void main(String[] args) {
        if (args.length < 1) {
            error("Usage: java Jugglers infilename outfilename");
            return;
        }

        jugglerList = new ArrayList<Juggler>();
        circuitMap = new HashMap<Integer, Circuit>();
        Character cRef = new Character('C'), jRef = new Character('J');
        Circuit c;
        Juggler j;
        int cCount = 0, jCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line;
            while((line = reader.readLine()) != null) {
//                debug(line);
                if (!line.contains(" ")) {
                    continue;
                }
                String [] attributes = line.split(" ");
                if (attributes != null && attributes.length > 0) {
                    if (cRef.equals(attributes[0].charAt(0))) {
                        c = createNewCircuit(attributes);
//                        debug(c.toString());
                        circuitMap.put(c.getIntId(), c);
                        cCount++;
                    } else if (jRef.equals(attributes[0].charAt(0))) {
                        j = createNewJuggler(attributes);
//                        debug(j.toString());
                        jugglerList.add(j);
                        jCount++;
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException fnfe) {
            error("line 53: " + fnfe.getMessage());
            return;
        } catch (IOException ioe) {
            error("line 56: " + ioe.getMessage());
            return;
        }
//        debug("jCount: " + jCount);
//        debug("cCount: " + cCount);
        int jugglersPerCircuit = (jCount == 0) ? 0 : jCount/cCount;
//        debug("jugglersPerCircuit: " + jugglersPerCircuit);

        // assign dot products
        Map<Integer, Integer> circuitMatchQualityMap;
        for (Juggler j1 : jugglerList) {
            circuitMatchQualityMap = new HashMap<Integer, Integer>();
            for (Circuit c1 : circuitMap.values()) {
                circuitMatchQualityMap.put(c1.getIntId(), getDotProduct(j1, c1));
            }
            j1.setCircuitMatchQualityMap(circuitMatchQualityMap);
//            debug(j1.toString());
        }

        // create Map<Integer (circuit id), List<Juggler>>
        Map<Integer, TreeSet<CircuitJuggler>> allCircuitAssignments = new HashMap<Integer, TreeSet<CircuitJuggler>>();
        TreeSet<CircuitJuggler> circuitAssignments;
        List<Juggler> iterationJugglers;
        List<Juggler> unassignedJugglers = new ArrayList<Juggler>(jugglerList);
        for (Juggler j1 : jugglerList) {
            unassignedJugglers.add(j1);
        }

        int count = 0;
        // assign jugglers to circuits
        // wrap the for loop in this while because we may remove jugglers from one circuit in favor of another
        // and will need to go through the exercise again with the removed juggler.
        Circuit iterationCircuit;
        CircuitJuggler iterationCircuitJuggler;
        CircuitJuggler circuitJugglerToRemove;
        Juggler jugglerToRemove;
        while (unassignedJugglers.size() > 0 && count < 15) {
            // copy whatever's left in unassignedJugglers into iterationJugglers for an(other) iteration thru remainder
            iterationJugglers = new ArrayList<Juggler>(unassignedJugglers);
            for (Juggler j1 : unassignedJugglers) {
                iterationJugglers.add(j1);
            }

            for (Juggler j1 : iterationJugglers) {
//                debug("trying to assign " + j1.toString());
                // for each circuit preference in juggler's prefs
                for (int circuitPref : j1.getPreferences()) {
//                    debug("looking at circuit pref " + circuitPref);
                    iterationCircuit = getCircuitById(circuitPref);
                    iterationCircuitJuggler = new CircuitJuggler(iterationCircuit, j1);

                    circuitAssignments = (allCircuitAssignments.get(circuitPref) == null) ?
                            new TreeSet<CircuitJuggler>() :
                            allCircuitAssignments.get(circuitPref);
                    if (circuitAssignments.size() < jugglersPerCircuit && !circuitAssignments.contains(iterationCircuitJuggler)) {
//                        debug("getting here");
                    // if # of jugglers in circuit's assignments is < jugglersPerCircuit
                        // add juggler to their first preferred circuit
                        circuitAssignments.add(iterationCircuitJuggler);
                        unassignedJugglers.remove(j1);
                    }
                    // else if this juggler's matchQuality for this circuit is higher than some one of the circuit's currently assigned jugglers
                    // then remove the juggler with the lowest matchQuality currently assigned to this circuit and add back to unassignedJugglers
                    else {
                        for (CircuitJuggler cj : circuitAssignments) {
//                            debug(j1.getId() + " qm for " + iterationCircuit.getId() + ": " + j1.getCircuitMatchQualityMap().get(circuitPref));
//                            debug("cj qm: " + cj.getCircuitJugglerMatchQuality());
                            if (j1.getCircuitMatchQualityMap().get(circuitPref) > cj.getCircuitJugglerMatchQuality()) {
                                // add this juggler to this circuit
                                circuitAssignments.add(iterationCircuitJuggler);
                                // remove this juggler from unassignedJugglers
                                unassignedJugglers.remove(j1);

                                // remove last juggler from circuitAssignments
                                circuitJugglerToRemove = circuitAssignments.last();
                                circuitAssignments.remove(circuitJugglerToRemove);
                                // add that juggler removed back into unassignedJugglers
                                jugglerToRemove = circuitJugglerToRemove.juggler;
                                unassignedJugglers.add(jugglerToRemove);

                                // no need to iterate thru rest of cj's
                                break;
                            }
                        }
                    }
                    allCircuitAssignments.put(circuitPref, circuitAssignments);

                    if (unassignedJugglers.contains(j1)) {
                        // continue to next circuit preference for this juggler
                        continue;
                    } else {
                        // continue to next unassigned juggler
                        break;
                    }
                }
            }
            count++;
        }

        CircuitJuggler cjHolder;
        int outputInnerCount, outputOuterCount;
        String comma;
        StringBuilder sb = new StringBuilder();
        TreeSet<String> outputLines = new TreeSet<String>();
        for (Map.Entry<Integer, TreeSet<CircuitJuggler>> circuitAssignmentsIterator : allCircuitAssignments.entrySet()) {
            outputOuterCount = 0;
            comma = ",";
            sb = new StringBuilder(circuitAssignmentsIterator.getKey() + " ");
//            debug("key: " + circuitAssignmentsIterator.getKey());
            for (CircuitJuggler cj : circuitAssignmentsIterator.getValue()) {
//                debug("value: " + cj.circuit.getId() + ": " + cj.juggler.getId());
                outputInnerCount = 0;
                sb.append(cj.juggler.getId() + " ");
                for(int pref : cj.juggler.getPreferences()) {
                    sb.append(pref + ":");
                    cjHolder = new CircuitJuggler(getCircuitById(pref), cj.juggler);
                    sb.append(cjHolder.getCircuitJugglerMatchQuality());
                    if (outputInnerCount > 1) {
                        sb.append(comma + " ");
                    } else {
                        sb.append(" ");
                    }
                    outputInnerCount++;
                }
                if (outputOuterCount == 2) {
                    comma = "";
                }
                outputOuterCount++;
            }
//            debug(sb.toString());
            outputLines.add(sb.toString());
        }

        try {
            File file = new File(args[1]);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String outputLine : outputLines.descendingSet()) {
                bw.write(outputLine + "\n");
            }
            bw.close();
            fw.close();
        } catch (IOException ioie) {
            error("line 203: " + ioie.getMessage());
            return;
        }
    }

    private static Circuit createNewCircuit(String[] attributes) {
        if (attributes == null || attributes.length < 5) {
            error("line 210: Wrong length circuit data");
            return null;
        }
        String id = attributes[1];
        int h = getColonedAttribute(attributes[2]);
        int e = getColonedAttribute(attributes[3]);
        int p = getColonedAttribute(attributes[4]);
        return new Circuit(id, h, e, p);
    }

    private static Circuit getCircuitById(int circuitId) {
        return circuitMap.get(circuitId);
    }

    private static Juggler createNewJuggler(String[] attributes) {
        if (attributes == null || attributes.length < 6) {
            error("line 226: Wrong length juggler data");
            return null;
        }
        String id = attributes[1];
        int h = getColonedAttribute(attributes[2]);
        int e = getColonedAttribute(attributes[3]);
        int p = getColonedAttribute(attributes[4]);
        LinkedList<Integer> preferences = new LinkedList<Integer>();
        String intPartOfIdString;
        for (String s : attributes[5].split(",")) {
            try {
                // removes the letter at start of id, parses the rest into an int
                intPartOfIdString = s.substring(1);
                preferences.add(Integer.parseInt(intPartOfIdString));
            } catch (NumberFormatException nfe) {
                error("line 241: " + nfe.getMessage());
            }
        }
        Juggler juggler = new Juggler(id, h, e, p);
        juggler.setPreferences(preferences);
        return juggler;
    }

    private static int getDotProduct(Juggler juggler, Circuit circuit) {
        return juggler.getH() * circuit.getH() +
                juggler.getE() * circuit.getE() +
                juggler.getP() * circuit.getP();
    }

    private static int getColonedAttribute(String attribute) {
        if (attribute == null) {
            return 0;
        } else {
            String [] attribKeyValue = attribute.split(":");
            if (attribKeyValue.length < 2) {
                return 0;
            } else {
                try {
                    return Integer.parseInt(attribKeyValue[1]);
                } catch (NumberFormatException e) {
                    error("line 266: NFE on attribKeyValue key: " + attribKeyValue[0] + "attribKeyValue value: " + attribKeyValue[1]);
                    return 0;
                }
            }
        }
    }

    static void error(String output) {
        System.err.println("ERROR: " + output);
    }

    static void debug(String output) {
        if (debugRequested) {
            System.out.println("debug: " + output);
        }
    }
}

class Aspected {
    String id;
    int h;
    int e;
    int p;

    Aspected(String id, int h, int e, int p) {
        this.id = id;
        this.h = h;
        this.e = e;
        this.p = p;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIntId() {
        // removes the letter at start of id, parses the rest into an int
        String intPartOfIdString = this.id.substring(1);
        return Integer.parseInt(intPartOfIdString);
    }
    public void setId(int id) { this.id = new Integer(id).toString(); }
    public int getH() { return h; }
    public void setH(int h) { this.h = h; }
    public int getE() { return e; }
    public void setE(int e) { this.e = e; }
    public int getP() { return p; }
    public void setP(int p) { this.p = p; }

    @Override
    public String toString() {
        return "Aspected{id='" + id + '\'' + ", h=" + h + ", e=" + e + ", p=" + p + '}';
    }
}

class Circuit extends Aspected {
    Circuit(String id, int h, int e, int p) {
        super(id, h, e, p);
    }
}

class Juggler extends Aspected {
    LinkedList<Integer> preferences;
    Map<Integer, Integer> circuitMatchQualityMap;

    Juggler(String id, int h, int e, int p) {
        super(id, h, e, p);
    }

    public LinkedList<Integer> getPreferences() { return preferences; }
    public void setPreferences(LinkedList<Integer> preferences) { this.preferences = preferences; }
    public Map<Integer, Integer> getCircuitMatchQualityMap() { return circuitMatchQualityMap; }
    public void setCircuitMatchQualityMap(Map<Integer, Integer> circuitMatchQualityMap) { this.circuitMatchQualityMap = circuitMatchQualityMap; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString() + "; Juggler{preferences=" + preferences + ", CMQM={");
        if (circuitMatchQualityMap != null) {
            for (Map.Entry<Integer, Integer> entry : circuitMatchQualityMap.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue() + ", ");
            }
        } else {
            sb.append("null");
        }
        sb.append("}}");
        return sb.toString();
    }
}

class CircuitJuggler implements Comparable<CircuitJuggler> {
    Circuit circuit;
    Juggler juggler;

    CircuitJuggler(Circuit circuit, Juggler juggler) {
        this.circuit = circuit;
        this.juggler = juggler;
    }

    public int getCircuitJugglerMatchQuality() {
        return this.juggler.circuitMatchQualityMap.get(this.circuit.getIntId());
    }

    // sorting in descending order
    public int compareTo(CircuitJuggler cj2) {

        Map<Integer, Integer> cMQM1 = this.juggler.circuitMatchQualityMap;
        Map<Integer, Integer> cMQM2 = cj2.juggler.circuitMatchQualityMap;

        if (cMQM1 == null || cMQM2 == null) {
            return 0;
        }

        int circuitId1 = circuit.getIntId();
        int circuitId2 = cj2.circuit.getIntId();
        if (circuitId1 != circuitId2) { // this is bad, it means we are comparing CircuitJugglers with two different circuits
            return 0;
        }

        int matchQuality1 = cMQM1.get(circuitId1);
        int matchQuality2 = cMQM2.get(circuitId2);

        if (matchQuality1 > matchQuality2) {
            return -1;
        } else if (matchQuality1 < matchQuality2) {
            return 1;
        } else {
            return 0;
        }
    }
}
