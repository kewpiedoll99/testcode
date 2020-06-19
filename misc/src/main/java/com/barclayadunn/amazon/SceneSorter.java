package com.barclayadunn.amazon;

// IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
// SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
// DEFINE ANY CLASS AND METHOD NEEDED
import java.util.*;

// CLASS BEGINS, THIS CLASS IS REQUIRED
public class SceneSorter {

    public static void main(String [] args) {
        Character [] inputArray = new Character[] {'a', 'b', 'a', 'b', 'c', 'b', 'a', 'c', 'a', 'd', 'e', 'f', 'e',
                                                   'g', 'd', 'e', 'h', 'i', 'j', 'h', 'k', 'l', 'i', 'j'};
        List<Character> inputList = new ArrayList(Arrays.asList(inputArray));
        System.out.println(lengthEachScene(inputList));
    }

    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    static List<Integer> lengthEachScene(List<Character> inputList)
    {
        // WRITE YOUR CODE HERE
        Map<Character, Integer> firstIndexes = new HashMap<>();
        Map<Character, Integer> lastIndexes = new HashMap<>();
        List<Scene> scenes = new ArrayList<>();
        Scene currentScene = null;
        // for each Character in inputList
        for (Character c : inputList) {
            // if Character not in firstIndexOf hashmap
            if (!firstIndexes.keySet().contains(c)) {
                // find firstIndexOf Character
                firstIndexes.put(c, findFirstIndexOf(inputList, c));
                // a0, b1, c4, d9, e10, f11, g13, h16, i17, j18, k20, l21
            }
            // if Character not in lastIndexOf hashmap
            if (!lastIndexes.keySet().contains(c)) {
                // find lastIndexOf Character
                lastIndexes.put(c, findLastIndexOf(inputList, c));
                // a8, b5, c7, d14, e15, f11, g13, h19, i22, j23, k20, l21
            }
        }

        // for each Character in uniqueChars
        for (Character c : inputList) {
            if (scenes.size() == 0) {
                // start new Scene
                currentScene = new Scene(c, firstIndexes.get(c), lastIndexes.get(c));
                scenes.add(currentScene);
            } else if (currentScene != null && currentScene.characters.contains(c)) {
                // add to characters
                currentScene.characters.add(c);
                // if thischar's lastIndex > scene's lastIndex, update
                if (lastIndexes.get(c) > currentScene.lastIndex) {
                    currentScene.lastIndex = lastIndexes.get(c);
                }
            } else {
                if (currentScene == null) {
                    currentScene = scenes.get(scenes.size() - 1);
                }
                // if this char's firstIndex is > scene's lastIndex
                if (firstIndexes.get(c) > currentScene.lastIndex) {
                    // start new Scene
                    currentScene = new Scene(c, firstIndexes.get(c), lastIndexes.get(c));
                    scenes.add(currentScene);
                } else {
                    // add this char to currentScene, check first/last index
                    currentScene.characters.add(c);
                    if (lastIndexes.get(c) > currentScene.lastIndex) {
                        currentScene.lastIndex = lastIndexes.get(c);
                    }
                }
            }
        }

        List<Integer> sceneLengths = new ArrayList<>();
        // for each Scene in scenes
        for (Scene scene : scenes) {
            sceneLengths.add(scene.characters.size());
        }
        return sceneLengths;
    }
    // METHOD SIGNATURE ENDS

    private static int findFirstIndexOf(List<Character> inputList, Character target) {
        int index = -1;
        for (int i = 0; i < inputList.size(); i++) {
            Character c = inputList.get(i);
            // System.out.println("char: " + c);
            if (c.equals(target)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private static int findLastIndexOf(List<Character> inputList, Character target) {
        int index = -1;
        for (int i = 0; i < inputList.size(); i++) {
            Character c = inputList.get(i);
            if (c.equals(target) && i > index)
                index = i;
        }
        return index;
    }
}

class Scene {
    int firstIndex;
    int lastIndex;
    List<Character> characters = new ArrayList<>();

    public Scene(Character c, int firstIndex, int lastIndex) {
        this.characters.add(c);
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }
}
