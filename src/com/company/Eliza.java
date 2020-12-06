package com.company;


import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Eliza {

    //Data variables.
    private LinkedList<Keyword> keywordList = new LinkedList<Keyword>(); //LinkedList contains keywords sorted by rank
    private Map<String,String> prePro = new HashMap<String,String>(); //hashmap entries in K,V pair pre processing
    private Map<String,String> postPro = new HashMap<String,String>(); //hashmap entries in K,V pair post processing.
    private Set<String> quit = new HashSet<String>(); //for quitting.
    private boolean isDone = false; // is the chat session finished.
    private String greetings; //Eliza greets the user.
    private String farewell; //Eliza bids farewell to the user.

    /*Initializing an object of eliza that whose purpose is to store keywords and decompose and reassemble.
    * */

    /*
    Errors:
    Line 59
    IndexOutOfBoundException
    Keyword key = keywordList.get(0);

    Scanner not being initiated.
    line 272
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    * */


    public Eliza (Scanner myScan) {

        //THE LOGIC

        String[] matchArray; //an array to store matches
        int decNum = 0; //Number of decompositions for a given keyword.

        //while loop telling us that as long as our scanner is getting input, continue.
        while (myScan.hasNextLine()) {

            //creating variables
            matchArray = new String[4]; //var declaring the match array with a size of 4
            String str = myScan.nextLine(); //var storing the input from scanner.

            //reassembles the word and stores it in the 1# index of matchArray[];
            if (Input.matchFound(str, "*reasemb: *", matchArray)) {
                Keyword key = keywordList.get(0);
                List<Decomposition> decList = key.getDecomp();
                //accessing last element of the decomposition list.
                Decomposition dec = decList.get(decNum - 1);
                //adding the rule.
                dec.addRule(matchArray[1]);

                
                //if the input matches the *decomp: *" decomposition pattern.
            } else if (Input.matchFound(str, "*decomp: *", matchArray)) {

                Keyword k = keywordList.get(0);
                k.addDecomp(new Decomposition(matchArray[1]));

                //increase decomposition index by 1
                decNum++;

            //looks for keyword pattern
            } else if (Input.matchFound(str, "*key: * #", matchArray)) {
                int num = 0;
                if (matchArray[2].length() != 0) {
                    num = Integer.parseInt(matchArray[2]);
                    keywordList.addFirst(new Keyword(matchArray[1], num));
                    decNum = 0;
                }
            } else if (Input.matchFound(str, "*key: *", matchArray)) {
                keywordList.addFirst(new Keyword(matchArray[1], 0));
                decNum = 0;
            }
            //stores in the keyword containing matching words "matchArray[]";
            else if (Input.matchFound(str, "*pre: * *", matchArray)) prePro.put(matchArray[1], matchArray[2]);
            else if (Input.matchFound(str, "*post: * *", matchArray)) postPro.put(matchArray[1], matchArray[2]);
            else if (Input.matchFound(str, "*quit: *", matchArray)) quit.add(matchArray[1]);
            else if (Input.matchFound(str, "*initial: *", matchArray)) greetings = matchArray[1];
            else if (Input.matchFound(str, "*final: *", matchArray)) farewell = matchArray[1];

        }
        //sorting the keyword list by rank
        Collections.sort(keywordList);
    }




    /*
    This replace method is responsible for replacing String strings of post and pre entries.
    Input: transformed string --> trimmed
    Output: substitutes words in either pre processed or post processed entries.
    * */

    private String replace(String str,Map<String,String> entryMap){
        String[] lines = new String[2];

        str = Input.trimString(str); //Variable storing the string after .trimString method is applied.
        String trans = ""; //variable to withhold transformed string.

        //looking for the "* *" pattern.
        while(Input.matchFound(str,"* *",lines)){

            if(entryMap.containsKey(lines[0])) lines[0] = entryMap.get(lines[0]);
            trans += lines[0] + " ";
            str = Input.trimString(lines[1]);

        }
        if (prePro.containsKey(str))
            str = prePro.get(str);
        trans += str;
        if(trans.charAt(trans.length()-1)==' ')
            trans = trans.substring(0,trans.length()-1);
        return trans;

    }/*
    Applies transformation to pre processed entries
    tries to find match with a keyword.
    if not "farewell" is returned. program ends.
     * */

    private String reply(String str){
        str = replace(str,prePro);

        if(quit.contains(str)){
            isDone = true;
            return farewell;
        }
        str = Input.addPad(str);
            for(Keyword key : keywordList){
                String[] replyLines = new String[2];
                if(Input.matchFound(str,"*" + key.getKeyword() + "*",replyLines)){
                    String reply = decompose(key,str);
                    if(reply != null)
                        return reply;
                }
            }
        return null;

            /*For post enteries

            Assembles a reply based on the inputArray[]

            If condition looks if a match is found, decomp rule is then updated
            and populates the reassembly array.

            Input: decomposition rule (decomp), input string of arrays (inputArry[])
            Output: A string thats been re-assembled. Stored @ index 3# of ressambArray[].
            * */
    }
    private String assemble(Decomposition decomp, String[] inputArray){
        String str = "";
        String reasemString = decomp.getRule();
        String[] reassembArray = new String [3];
        decomp.updateRule();

        if(Input.matchFound(reasemString,"* (#)* ",reassembArray)){
            int n = Integer.parseInt(reassembArray[1])-1;
            //calling replace function,
            str = reassembArray[0]+ " " + replace(inputArray[n],postPro);
            reasemString = reassembArray[2];
        }
        str += reasemString;

        //ressambled string returned.
        return str;
    }

    /*
    Looks for a match to the given String (str) by decomposing the pattern based on a certain keyword (key)
    if found the assemble method is called upon, and is returned as a reply
    otherwise null is returned.
    * */

    private String decompose(Keyword key, String str){
        String[] inputArray = new String[20];

        //for each loop
        for(Decomposition decomp : key.getDecomp()){
            String patrn = decomp.getDecomPat();
            if(Input.matchFound(str,patrn,inputArray)){
                String reply = assemble(decomp,inputArray);
                if(reply != null)
                    return reply;
            }
        }
        return null;
    }
        /*Gets an input string (str) --> generates an appropriate reply to user.
        *
        * if not found returns a string
        * */
        private String response(String str){
            String reply = null;
            str = Input.process(str);
            String[] respArray = new String[2];

            if(Input.matchFound(str,"*.*",respArray)){
                reply = reply(respArray[0]);
                if(reply != null)
                    return reply;
                str = Input.trimString(respArray[1]);
            }
            if(str.length() != 0){
                reply = reply(str);
                if(reply != null){
                    return reply;
                }
            }
            Keyword keyword = null;
            for(Keyword key :keywordList){
                if(key.getKeyword().equals("xnone")){
                    keyword = key;
                    break;
                }
            }
            if(keyword != null){
                reply = decompose(keyword,str);
                if(reply != null)
                    return reply;
            }

            return "I can't think of an appropriate response for that.";
        }


        public void runEliza() {
            System.out.println(greetings);
            String str = "";

            //bufferedreader for reading text files.
            BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
            //Exception handling for errors.
            do{
                try{
                    str = bfr.readLine();
                }catch (IOException e){
                    e.printStackTrace();
                }
                String respond   = response(str);
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(respond);
            }while(!isDone);
        }

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


































/*

    public static void main(String[] args) {

    }

    //instance scanner & random objects
    Scanner userInp = new Scanner(System.in);
    Random rand = new Random();




	// write your code here




    //methods
     /*
      >  create a var, that contains a list, this list will contain the answers and the questions.

      String pairs[][]
        [answer,question]

        //create class with dictionary containing reflection words "i am, i have,my,mine,you...etc.
     * */

    //Method containing welcoming message + instructions in plain english.


    //regular expressions, predefined scenarios.



/*

Create a 2d array [][], this variable will take care of chatBot
this will take care of all the phrases you might, and how the bot responses.

//the more default you will say, the more intelligent the bot will seem to be.


---grab qoutes---

//will grab whatever input is given
String qoute=input.getText();

//clear input
input.setText("");

//add text method return void (input String str) --> dialog.setText(dialog.getText()+str));
addText("-->You:\t"+qoute);


//trim method
removes whitespace

//punctation (remove ! . ?)


//inArray method boolean
    searches the array for input, if its equal[i] set match var == true; otherwise return match(false)

//


---check for matches---

---default-----


* */

