package com.example.brwong.funfacts;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Brwong on 5/2/15.
 */
public class ColorWheel {
    //Member variables (properties of the class)
    public String[] mColors = {"#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
             }; //This is actually a property of this class, shouldn't be in the method.

    //Methods
    public int getColor(){
        //This runs when the our fact button is clicked
        String newColor = "";
        Random randomGenerator = new Random(); //Instantiating the Random class to generate a random number.
        int randomNumber = randomGenerator.nextInt(mColors.length);//Uses Random's method nextInt to generate n number of ints specified in the ().
        //NUMBERS GIVEN START FROM 0
        //funFactsArray.length ALWAYS lets use generate all the elements in our array!
        newColor = mColors[randomNumber]; //Generates a random number then uses it to retrieve an array element
        int colorAsInt = Color.parseColor(newColor);//TRANSFORMS THE STRING INTO AN INT BECAUSE COLORS ARE ONLY SET AS INTS

        return colorAsInt;
    }
}
