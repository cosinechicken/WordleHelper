package com.company;

import java.util.*;
import java.io.*;

public class WordSquare {
    private static String wordQuery = "david";
    private static int solCount = 0;
    private static int wordLength;
    private static ArrayList<String> wordList;
    private static ArrayList<HashMap<String, ArrayList<String>>> mapArr;
    public static void main(String[] args) throws IOException {
        wordLength = wordQuery.length();
        wordQuery = wordQuery.toUpperCase();
        wordList = new ArrayList<>();
        String fileName = "words-" + wordLength + ".txt";
        Scanner input = new Scanner(new BufferedReader(new FileReader(fileName)));
        while (input.hasNextLine()) {
            String next = input.nextLine();
            wordList.add(next.toUpperCase());
        }
        mapArr = new ArrayList<HashMap<String, ArrayList<String>>>();
        // mapArr.get(n) is the prefix map with prefixes of length n+1
        for (int i = 0; i < wordLength; i++) {
            mapArr.add(new HashMap<String, ArrayList<String>>());
        }
        for (String word : wordList) {
            for (int i = 0; i < wordLength; i++) {
                String prefix = word.substring(0,i+1);
                if (!mapArr.get(i).keySet().contains(prefix)) {
                    mapArr.get(i).put(prefix, new ArrayList<String>());
                }
                mapArr.get(i).get(prefix).add(word);
            }
        }

        boolean valid = solve(wordQuery);
        if (!valid) {
            System.out.println("No solutions found for " + wordQuery);
        }
    }
    private static boolean solve(String wordQuery) {
        ArrayList<String> prefixes = new ArrayList<>();
        // prefix.get(i) is the prefix for the word at index i+1
        for (int i = 0; i < wordLength - 1; i++) {
            prefixes.add(wordQuery.substring(i+1, i+2));
        }
        return solve_helper(wordQuery, prefixes, 0);
    }
    private static boolean solve_helper(String word, ArrayList<String> prefixes, int index) {
        // System.out.println(prefixes);
        boolean hasSol = false;
        if (index == wordLength - 2) {
            // base case
            if (mapArr.get(index).get(prefixes.get(index)).size() > 0) {
                prefixes.set(index, mapArr.get(index).get(prefixes.get(index)).get(0));
                solCount++;
                System.out.println("Solution " + solCount + ": ");
                System.out.println(word);
                for (String prefix : prefixes) {
                    System.out.println(prefix);
                }
                System.out.println();
                return true;
            }
            return false;
        }
        for (String choice : mapArr.get(index).get(prefixes.get(index))) {
            // System.out.println("TEMP: " + prefixes);
            // Set the word at index to be the chosen one
            prefixes.set(index, choice);
            // Set prefixes of the other words
            for (int i = index+1; i < wordLength - 1; i++) {
                prefixes.set(i, prefixes.get(i) + choice.charAt(i+1));
            }
            // Check prefix validity
            boolean valid = true;
            for (int i = index+1; i < wordLength - 1; i++) {
                if (!mapArr.get(index+1).containsKey(prefixes.get(i))) {
                    valid = false;
                }
            }
            if (valid) {
                // Try out the prefixes
                boolean result = solve_helper(word, prefixes, index+1);
                if (result) {
                    hasSol = true;
                }
            }
            // Revert prefixes
            for (int i = index+1; i < prefixes.size(); i++) {
                prefixes.set(i, prefixes.get(i).substring(0, index+1));
            }
        }
        return hasSol;
    }
}