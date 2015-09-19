package com.example.brwong.funfacts;

import java.util.Random;

/**
 * Created by Brwong on 5/2/15.
 */
public class FactBook {
    //Member variables (properties of the class)
    public String[] mfunFactsArray = {"Alison has a fat ass.", "Ostriches can run faster than horses.",
            "Olympic gold medals are actually made mostly of silver.",
            "You are born with 300 bones; by the time you are an adult you will have 206.",
            "It takes about 8 minutes for light from the Sun to reach Earth.",
            "Some bamboo plants can grow almost a meter in just one day.",
            "The state of Florida is bigger than England.",
            "Some penguins can leap 2-3 meters out of the water.",
            "On average, it takes 66 days to form a new habit.",
            "Mammoths still walked the earth when the Great Pyramid was being built."}; //This is actually a property of this class, shouldn't be in the method.
    //Methods
    public String getFact(){
        //This runs when the our fact button is clicked
        String newFact = "";
        Random randomGenerator = new Random(); //Instantiating the Random class to generate a random number - THIS IS A CONSTRUCTOR
        int randomNumber = randomGenerator.nextInt(mfunFactsArray.length);//Uses Random's method nextInt to generate n number of ints specified in the ().
        //NUMBERS GIVEN START FROM 0
        //funFactsArray.length ALWAYS lets use generate all the elements in our array!
        newFact = mfunFactsArray[randomNumber]; //Generates a random number then uses it to retrieve an array element

        return newFact;
    }
}
