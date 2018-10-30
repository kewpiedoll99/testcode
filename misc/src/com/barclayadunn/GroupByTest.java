package com.barclayadunn;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class GroupByTest {
    private static List<String> names = Lists.newArrayList("LLQA", "eric", "jeannice", "Camille", "kaleigh", "rtannenbaum", "vaibhav", "MootyWaffles", "timmy", "luke");
    private static String sampleText = "[LLQA] what's the unitato? [LLQA] besides half unicorn half potato [eric] it may also be a " +
            "unicorn with a potato for a horn [eric] speaking of potatoes [eric] I would join any lunch fellowships that may be " +
            "departing in the next 15 or so [jeannice] It's CEPOD's unit service, the unitato is ours [jeannice] :D still elusive " +
            "of course like the real thing [LLQA] they have invaded stage[lex] unitato spotting: [jeannice] must be a different breed " +
            "[Camille] anyone know where the stats log goes on the analytics servers? [Camille] I uh did something bad[kaleigh] " +
            "is it on the same machine as their scores? [Camille] apparently the stats log parser will just randomly delete " +
            "files if you run it [Camille] fyi [Camille] well, not \"randomly\" [Camille] it will delete any existing parsed logs " +
            "for the day you are running it on [kaleigh] i copy scores from srv01.analytics.renttherunway.it [Camille] lolz" +
            "[ Camille] yeah I'm on that server [Camille] but I don't know where the logs are [kaleigh] i can't remember or " +
            "find it - i've seen them - vaibhav is your best bet [kaleigh] or saurabh[Camille] I've got anna on the horn " +
            "[MootyWaffles] reservation made [MootyWaffles] going to back to sleep [Ashley] aw [eric] william is like Node[eric] " +
            "request? response! [eric] back to sleep [Camille] lol [Camille] eric++ [woot! now at 5] [eric] http://bukk.it/yes.gif " +
            "[timmy] just a thought with everyone getting sick in the fishbowl, but has that room ever been vacuumed [timmy] " +
            "not even like, carpet cleaned, but really just vacuumed. [Camille] it has been[Camille] I remember once they did it " +
            "[timmy] once![eric] ah yes [eric] it was [eric] The Great Vacuuming [eric] before my time [rtannenbaum] everyone " +
            "please stay away from my desk [luke] oh, btw. I dropped off the power cord for the drum machine, but please don't " +
            "overwrite any tracks I already have on there [eric] hahahaha [eric] I don't know why I found that so funny [eric] " +
            "luke++ [woot! now at 4] [rtannenbaum] stay away from quarantine corner of the fishbowl [rtannenbaum] haha [eric] " +
            "I am regressing";

    public static void main(String [] args) {
        List<CompositeThing> compositeThingList = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            compositeThingList.add(new CompositeThing(i, names.get(i % 10), sampleText.substring(i, i+20)));
        }

        Map<CompositeThing, List<CompositeThing>> groupedLists = compositeThingList.stream()
                .collect(groupingBy(Function.identity(), toList()));
        groupedLists.entrySet().forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

class CompositeThing {
    private int id;
    private String name;
    private String description;

    public CompositeThing(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .toString();
    }
}