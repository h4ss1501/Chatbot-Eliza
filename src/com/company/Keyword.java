package com.company;

import java.util.ArrayList;
import java.util.List;

/*
This is a data-type that stores A keyword with associated decomposition patterns.
* */

//class must implement comparable interface
public class Keyword implements Comparable<Keyword>{

    /*
    Class that stores a keyword, and it's decomposition patterns.

    This data type classifies the list based on
    A keyword with an attached rank
    * */


    //instance variables
    private List<Decomposition> decomplist;
    private String keyword;
    private int rank;


    //constructer takes keyword and rank
    //creates a decompositionlist ArrayList.
    public Keyword(String word, int rank){
        this.keyword = word;
        this.rank = rank;
        this.decomplist = new ArrayList<Decomposition>();
    }

    //methods
    //method to add a decomposition rule to the decompList
    public void addDecomp(Decomposition dec){
        decomplist.add(dec);
    }

    //overriden compareTo
    //to compare keywords
    @Override
    public int compareTo(Keyword otherK) {
        return otherK.rank -this.rank;
    }


    //getters
    public String getKeyword(){
        return keyword;
    }

    //returns the entire list of decomposition.
    public List<Decomposition> getDecomp(){
        return decomplist;
    }


    //toString
    public String toString(){
        String str = "Keyword: " + keyword + " ";
        for(Decomposition dec :decomplist){
            str += dec.toString();
        }
        return str;
    }




}
