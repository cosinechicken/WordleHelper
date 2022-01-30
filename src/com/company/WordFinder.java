package com.company;

import java.util.*;
import java.io.*;

public class WordFinder {

    public static void main(String[] args) throws IOException {
	// write your code here
        Scanner input = new Scanner(new BufferedReader(new FileReader("WordData.txt")));
        FileWriter myWriter = new FileWriter("Words.txt");
        while (input.hasNextLine()) {
            String next = input.nextLine();
            String[] arr = next.split(" ");
            String output = arr[5];
            myWriter.write(output + "\n");
        }
        myWriter.close();
    }
}
