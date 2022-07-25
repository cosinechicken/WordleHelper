package com.company;

import java.util.*;
import java.io.*;

public class SpellingBee {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new BufferedReader(new FileReader("Words.txt")));
        Scanner in = new Scanner(System.in);
        ArrayList<String> wordList = new ArrayList<>();
        while (input.hasNextLine()) {
            String[] nextArr = input.nextLine().split(",");
            for (String str : nextArr) {
                String temp = str.substring(1, str.length() - 1);
                if (temp.length() < 4) {
                    continue;
                }
                wordList.add(temp.toLowerCase());
            }
        }
        System.out.println("Enter the list of letters allowed, with no spaces in between.");
        System.out.println("The first letter is the required letter:");
        String rules = in.nextLine().toLowerCase();

        char[] rulesArr = rules.toCharArray();
        ArrayList<String> ans = new ArrayList<>();
        for (String str : wordList) {
            boolean isValid = true;
            for (int i = 0; i < str.length(); i++) {
                boolean isLetterAllowed = false;
                for (int j = 0; j < rulesArr.length; j++) {
                    if(str.charAt(i) == rulesArr[j]) {
                        isLetterAllowed = true;
                    }
                }
                isValid = isValid && isLetterAllowed;
            }
            boolean containsFirst = false;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == rulesArr[0]) {
                    containsFirst = true;
                }
            }
            if (isValid && containsFirst) {
                ans.add(str);
            }
        }
        for (String str : ans) {
            System.out.println(str);
        }
        System.out.println("There were " + ans.size() + " solutions.");
    }
}
