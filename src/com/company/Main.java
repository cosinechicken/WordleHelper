package com.company;

import java.util.*;
import java.io.*;

public class Main {
    private static ArrayList<String> totalList;
    // Get the 5 character string of 0, 1, 2 from our guess and the answer
    private static String getFeedback(String guess, String ans) {
        int[] ansCharCounts = new int[26];
        for (int i = 0; i < 26; i++) {
            ansCharCounts[i] = 0;
        }
        for (int i = 0; i < 5; i++) {
            ansCharCounts[ans.charAt(i) - 'a']++;
        }
        int[] guessCharCounts = new int[26];
        for (int i = 0; i < 26; i++) {
            guessCharCounts[i] = 0;
        }
        String[] result = new String[5];
        for (int i = 0; i < 5; i++) {
            if (ans.charAt(i) == guess.charAt(i)) {
                result[i] = "2";
                int index = guess.charAt(i) - 'a';
                guessCharCounts[index]++;
            } else {
                result[i] = "";
            }
        }
        for (int i = 0; i < 5; i++) {
            if (result[i].equals("2")) {
                continue;
            }
            int index = guess.charAt(i) - 'a';
            if (guessCharCounts[index] >= ansCharCounts[index]) {
                result[i] = "0";
            }else {
                result[i] = "1";
            }
            guessCharCounts[index]++;
        }
        String resultString = "";
        for (int i = 0; i < 5; i++) {
            resultString += result[i];
        }
        return resultString;
    }
    // Find the word in wordlist such that it narrows the wordlist down as much as possible in the worst case
    private static String runGuesses (ArrayList<String> wordList) {
        String bestGuess = "No Guesses Made";
        int bestQuality = Integer.MAX_VALUE;
        int counter = 0;
        for (String guess : totalList) {
            // Find the buckets each string goes into and look at how much they're narrowed down
            int quality = -1;
            String likelyFeedback = "";
            HashMap<String, Integer> buckets = new HashMap<>();
            for (String ans : wordList) {
                String feedback = getFeedback(guess, ans);
                if (!buckets.containsKey(feedback)) {
                    buckets.put(feedback, 0);
                }
                buckets.put(feedback, buckets.get(feedback) + 1);
            }
            // Set quality to the max value in the hashmap
            for (String str : buckets.keySet()) {
                if (buckets.get(str) > quality) {
                    quality = buckets.get(str);
                    likelyFeedback = str;
                }
            }
            // System.out.println("(" + counter + ") " + guess + ": " + quality + ", " + likelyFeedback);

            if (likelyFeedback.equals("22222") && buckets.size() == 1) {
                return guess;
            }
            // Replace quality if necessary
            if (quality < bestQuality) {
                bestQuality = quality;
                bestGuess = guess;

            }
            counter++;
        }

        return bestGuess;
    }
    private static void updateList(ArrayList<String> wordList, String guess, String feedback) {
        ArrayList<String> temp = new ArrayList<>();
        for (String str : wordList) {
            if (getFeedback(guess, str).equals(feedback)) {
                temp.add(str);
            }
        }
        wordList.clear();
        for (String str : temp) {
            wordList.add(str);
        }
    }
    private static boolean isValidFeedback(String feedback) {
        if (feedback.length() != 5) {
            return false;
        }
        for (int i = 0; i < 5; i++) {
            if (feedback.charAt(i) != '0' && feedback.charAt(i) != '1' && feedback.charAt(i) != '2') {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws IOException {
        // write your code here
        Scanner input = new Scanner(new BufferedReader(new FileReader("Words.txt")));
        Scanner in = new Scanner(System.in);
        ArrayList<String> wordList = new ArrayList<>();
        totalList = new ArrayList<>();
        while (input.hasNextLine()) {
            String next = input.nextLine();
            wordList.add(next.toLowerCase());
            totalList.add(next.toLowerCase());
        }
        System.out.println("Instructions: The program will output a guess, and for each letter, type 0 if it's gray, 1 if it's orange, and 2 if it's green");
        System.out.println("Enter 'q' without quotes to quit");
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(wordList.get(i) + " and adieu: " + getFeedback(wordList.get(i), "adieu"));
//        }
        int numGuesses = 0;
        while (true) {
            String bestGuess = runGuesses(wordList);
            numGuesses++;
            System.out.println("Guess: " + bestGuess);
            System.out.print("Please enter the feedback: ");
            String feedback = in.nextLine();
            // Check for user error
            boolean isQuitting = false;
            while (!isValidFeedback(feedback)) {
                if (feedback.equals("q")) {
                    isQuitting = true;
                    break;
                }
                System.out.println("Sorry, feedback is not valid: " + feedback);
                System.out.print("Please enter the feedback: ");
                feedback = in.nextLine();
            }
            if (isQuitting) {
                System.out.println("Good game!");
                break;
            }
            if (feedback.equals("22222")) {
                System.out.println("Guessed correctly in " + numGuesses + " tries!");
                break;
            }
            updateList(wordList, bestGuess, feedback);
            System.out.println("Words remaining: ");
            int counter = 0;
            for (String str : wordList) {
                counter++;
                System.out.println("(" + counter + "): " + str);
            }
        }
    }
}
