package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(java.lang.String[] args) {
        System.out.println("Hello from main");

        Scanner myScan = null;

        try {
            myScan = new Scanner(new File("C:\\Users\\HAM\\Documents\\IdeaProjects\\ChatBot\\src\\com\\company\\script.txt"));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Eliza elizaBot = new Eliza(myScan);
        elizaBot.runEliza();

    }
}
