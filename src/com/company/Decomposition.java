package com.company;


/*
This class is responsible for storing decomposition patterns and their reassembly
patterns.
* */

import java.util.ArrayList;
import java.util.List;

/*Thi class is in charge of the "Decomposition" process of a sentence
* '*/

public class Decomposition {

    //instance variable

    private String decomPat; //pattern of decomposition
    private List<String> reasembList; //list containing the (re-assembly) rules
    public int curIndex = 0; // index of  the current rule


    //constructor

    //takes a pattern as input
    public Decomposition(String patrn){
        this.decomPat = patrn;

        //reasembly list is created.
        reasembList = new ArrayList<String>();
    }

    //methods

    //adding a rule to the reassembly-list
    public void addRule(String rule){
        reasembList.add(rule);
    }

    //updates the current rule on the reassembly-list
    public void updateRule(){
        if(++curIndex == reasembList.size())
            curIndex = 0;
    }

    //getters
    public String getDecomPat(){
        return decomPat;
    }

    public String getRule(){
        return reasembList.get(curIndex);
    }

    //tostring
    //returns a .toString method representing of the decomposed string.

    public String toString(){

        String str = "Decomposed pattern: " + decomPat + ".";

        //for each loop
        for(String re : reasembList){
            str += " reasmbl: " + re;
        }
        return str;

    }


}
