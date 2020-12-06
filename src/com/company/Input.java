package com.company;

/*

This class is meant to process input, and make sure it's correct

/*
 * Processes the input string (str)
 * 1. Makes sure that uppercase letters are transformed into lowercase
 * 2. Signs are transformed into whitespace.
 * 3. The string is insured to trim BREAK signs, replaced with a period.
 */



public class Input {

    //variables
    private final static String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
    private final static String LOWERCASE = "abcdefghijklmnopqrstuvxyz";
    private final static String NUMERALS = "0123456789";
    private final static String WHITESPACE = "                          ";
    private final static String SIGN = "@#$%^&*()_-+=~`{[}]|:;<>/'";
    private final static String BREAK = "?!,";
    private final static String PERIOD = ".";



    //method to process a string
 public static String process(String str){

     //traverse through a string, and replace uppercase letters with lowercase.
     for(int i = 0; i < UPPERCASE.length(); i++){
         //replaces uppercase letterw with lowercase
         str = str.replace(UPPERCASE.charAt(i),LOWERCASE.charAt(i));
     }
     //traverse through a string, and replace SIGN(s) with a whitespace.
     for(int i = 0; i < SIGN.length(); i++){
         //replaces signs with whitespace
         str = str.replace(SIGN.charAt(i),WHITESPACE.charAt(i));
     }
     //traverse through a string, replacing BREAK with a PERIOD '.'
     for(int i = 0; i <BREAK.length();i++){
         //replacing sentence-breaking signs with a period.
         str = str.replace(BREAK.charAt(i),PERIOD.charAt(i));
     }

     //processing string
     String ps ="";
     if(str.length()!=0){

         char chr1 = str.charAt(0);

         //looping the string comparing char1 and char2
         for(int i = 1; i < str.length();i++){
             char chr2 = str.charAt(i);
             if(chr1 == ' ' && (chr2 == ' ' || chr2 == ',' || chr2 =='.'))
                 continue;
             //if theres a whitespace before "?",
             if(chr1 != ' ' && chr2 =='?')

                 //increment the string ps with first character and a whitespace.
                 ps += chr1 + " ";
             else
                 //increment by adding first character
                 ps += chr1;
                //
                 chr1=chr2;
             }
             ps += chr1;

     }
     return ps;
 }

 //method to ensure ADDING of whitespace before and after a sentence.

    public static String addPad(String str){
     if(str.length() == 0) {
         return " ";
     }
     char first = str.charAt(0);
     //-1 to access the last character
     char last = str.charAt(str.length()-1);
        if (first == ' ' && last == ' ') return str;
        if (first == ' ' && last != ' ') return str + " ";
        if (first != ' ' && last == ' ') return " " + str ;
        if (first != ' ' && last != ' ') return " " + str    + " ";
        return str;
}

//method to trim off whitespace of a string
    public static String trimString(String str){
     for(int i = 0; i < str.length(); i++){
         if(str.charAt(i) != ' '){
             return str.substring(i);
         }
     }
     return "";
}
/*
Responsible for finding the index of non-number character on a given String Str
if not found length of the string is returned.

* */
    private static int findNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (NUMERALS.indexOf(str.charAt(i)) == -1)
                return i;
        }
        return str.length();
    }
    /*
    findMatch()

    Responsible for finding a pattern (patrn) for a given String(str)
    if found --> number is returned before * or # sign.
    if not found --> -1 value is returned.
    * */

    public static int findMatch(String str, String patrn){
        int count = 0;
        int i = 0; //traverse through str
        int j = 0; //traverse through patrn
        while(i < str.length() && j<patrn.length()) {
            char p = patrn.charAt(j);

            //if '*' or '#' in patrn is found loop terminates
            if (p == '*' || p == '#')
                return count;
            if (str.charAt(i) != p)
                return -1;
            //if equal
            i++;
            j++;
            count++;
        }
        return  count;
    }

    /*
      * Traverses the string for matches to a pattern
      * count is incremented if a match is found for a pattern
      * -1 if no match is found.
      *
      *
      */

    public static int findPatrn(String str, String patrn){
        int count = 0;
        for(int i = 0; i < str.length(); i++){
            if(findMatch(str.substring(i), patrn)>=0)
                return count;
            count++;
            }
            return -1;
    }


/* **
 *
 * Boolean method (true or false)
 * Looks for match of input (str) String against a given pattern (patrn)
 * if so an array with matches[] is populated with matches.
 * otherwise returns false.
 */

    public static boolean matchFound(String str, String patrn, String matches[]){
        int i = 0; //traverse through string (str)
        int j = 0; //indexing of line array.
        int pos = 0; //indexing of pattern (patrn)
        while(pos < patrn.length() && j < matches.length) {
            char chr = patrn.charAt(pos);
            if (chr == '*') {


                int n; //remaining length of string.
                if (pos == patrn.length() - 1) {
                    n = str.length() - i;
                } else {
                    n = findPatrn(str.substring(i), patrn.substring(pos + 1));
                }
                if (n < 0) {
                    return false;
                }
                matches[j++] = str.substring(i, i + n);
                i += n;
                pos++;


            }
            if (chr == '#') {

                int n = findNum(str.substring(i));
                matches[pos++] = str.substring(i, i + n);
                i += n;
                pos++;
            } else {
                int n = findMatch(str.substring(i), patrn.substring(pos));
                i += n;
                pos++;

            }
        }
        if(i>str.length()-1 && pos > patrn.length()-1)
            return true;
        return false;
}


}



