package com.company;

import java.util.*;
import java.io.*;

public class Dordle {
    private static ArrayList<String> totalList;
    private static boolean guessed1;
    private static boolean guessed2;
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
    private static String runGuesses (ArrayList<String> wordList1, ArrayList<String> wordList2) {
        String bestGuess = "No Guesses Made";
        int bestQuality1 = 3000;
        int bestQuality2 = 3000;
        int counter = 0;
        for (String guess : totalList) {
            // Find the buckets each string goes into and look at how much they're narrowed down
            int quality1 = -1;
            String likelyFeedback1 = "";
            HashMap<String, Integer> buckets1 = new HashMap<>();
            int quality2 = -1;
            String likelyFeedback2 = "";
            HashMap<String, Integer> buckets2 = new HashMap<>();
            for (String ans : wordList1) {
                String feedback = getFeedback(guess, ans);
                if (!buckets1.containsKey(feedback)) {
                    buckets1.put(feedback, 0);
                }
                buckets1.put(feedback, buckets1.get(feedback) + 1);
            }
            for (String ans : wordList2) {
                String feedback = getFeedback(guess, ans);
                if (!buckets2.containsKey(feedback)) {
                    buckets2.put(feedback, 0);
                }
                buckets2.put(feedback, buckets2.get(feedback) + 1);
            }
            // Set quality to the max value in the hashmap
            for (String str : buckets1.keySet()) {
                if (buckets1.get(str) > quality1) {
                    quality1 = buckets1.get(str);
                    likelyFeedback1 = str;
                }
            }
            for (String str : buckets2.keySet()) {
                if (buckets2.get(str) > quality2) {
                    quality2 = buckets2.get(str);
                    likelyFeedback2 = str;
                }
            }
            // System.out.println("(" + counter + ") " + guess + ": " + quality + ", " + likelyFeedback);

            if (likelyFeedback1.equals("22222") && buckets1.size() == 1 && !guessed1) {
                return guess;
            }
            if (likelyFeedback2.equals("22222") && buckets2.size() == 1 && !guessed2) {
                return guess;
            }
            // Replace quality if necessary
            if (guessed1) {
                quality1 = 3000;
            }
            if (guessed2) {
                quality2 = 3000;
            }
            if (Math.min(quality1,quality2) < Math.min(bestQuality1,bestQuality2)) {
                bestQuality1 = quality1;
                bestQuality2 = quality2;
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
        ArrayList<String> wordList1 = new ArrayList<>();
        ArrayList<String> wordList2 = new ArrayList<>();
        guessed1 = false;
        guessed2 = false;
        totalList = new ArrayList<>();
        while (input.hasNextLine()) {
            String next = input.nextLine();
            wordList1.add(next.toLowerCase());
            wordList2.add(next.toLowerCase());
            totalList.add(next.toLowerCase());
        }
        System.out.println("Instructions: The program will output a guess, and for each letter, type 0 if it's gray, 1 if it's orange, and 2 if it's green");
        System.out.println("Enter 'q' without quotes to quit");
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(wordList.get(i) + " and adieu: " + getFeedback(wordList.get(i), "adieu"));
//        }
        int numGuesses = 0;
        while (true) {
            String bestGuess = runGuesses(wordList1, wordList2);
            numGuesses++;
            System.out.println("Guess: " + bestGuess);
            System.out.print("Please enter the feedback: ");
            String feedbacks = in.nextLine();
            String[] feedbackArr = feedbacks.split(" ");
            String feedback1 = feedbackArr[0];
            String feedback2 = feedbackArr[1];
            // Check for user error
            boolean isQuitting = false;
            while (!isValidFeedback(feedback1) || !isValidFeedback(feedback2)) {
                if (!isValidFeedback(feedback1)) {
                    System.out.println("Sorry, first feedback is not valid: " + feedback1);
                } else {
                    System.out.println("Sorry, second feedback is not valid: " + feedback2);
                }

                System.out.print("Please enter the feedback: ");
                feedbacks = in.nextLine();
                feedbackArr = feedbacks.split(" ");
                feedback1 = feedbackArr[0];
                feedback2 = feedbackArr[1];
            }
            if (isQuitting) {
                System.out.println("Good game!");
                break;
            }
            if (feedback1.equals("22222")) {
                guessed1 = true;
                System.out.println("Guessed first word correctly in " + numGuesses + " tries!");
            }
            if (feedback2.equals("22222")) {
                guessed2 = true;
                System.out.println("Guessed second word correctly in " + numGuesses + " tries!");
            }
            if (guessed1 && guessed2) {
                break;
            }
            updateList(wordList1, bestGuess, feedback1);
            updateList(wordList2, bestGuess, feedback2);
            System.out.println("Words remaining for left: ");
            int counter = 0;
            for (String str : wordList1) {
                counter++;
                System.out.println("(" + counter + "): " + str);
            }
            System.out.println("Words remaining for right: ");
            counter = 0;
            for (String str : wordList2) {
                counter++;
                System.out.println("(" + counter + "): " + str);
            }
        }
    }
}
