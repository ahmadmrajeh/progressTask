package com.progressoft.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;


public class maxMinImp {

    public static void calc_Normal(int least, int Maxed, LinkedHashMap<Integer,BigDecimal>maxedMap) {
                                            //CODING A FORMULA TO CALCULATE MINmax Normaliztion
        maxedMap.forEach((key, value) -> {

            double temp = (key - least);

            double temp2 = Maxed - least;

            BigDecimal temp3 = BigDecimal.valueOf((temp / temp2) * (1.0 - 0.0) + 0.0);

            maxedMap.put(key, temp3.setScale(2, RoundingMode.HALF_EVEN));
        });
    }





    public static void minMaxResult (Path oldPath, Path newPath, int numOfCols, LinkedHashMap<Integer,BigDecimal>maxMap) {
                               // THIS METHOD PRINTS THE RESULTS OF MINMAX NORM ON THE DEST FILE AS REQUIRED
        try {
            minMaxZscore.reader =Files.newBufferedReader(oldPath) ;

            BufferedWriter writer = Files.newBufferedWriter(newPath) ;
            PrintWriter wrLine = new PrintWriter(writer) ;

            if( numOfCols==5) {

                wrLine.println("name"+','+"job"+','+"company"+','+"salary"+','+"salary_mm"+','+"address");
                wrLine.flush();
            }else if(numOfCols==3) {

                wrLine.println("id"+','+"name"+','+"mark"+','+"mark_mm");
                wrLine.flush();
            }
            minMaxZscore.reader.readLine() ;
            while ((minMaxZscore.line=minMaxZscore.reader.readLine())!= null ){

                String[] values = minMaxZscore.line.split(",") ;
                if( numOfCols==5) {

                    wrLine.println(values[0]+','+values[1]+','+values[2]+','+values[3]+','+ maxMap.get(Integer.parseInt(values[3])).setScale(2,RoundingMode.HALF_EVEN)+','+values[4]);
                    wrLine.flush();
                }else if(numOfCols==3) {
                    wrLine.println(values[0] + ',' + values[1] + ',' + values[2] + ',' + maxMap.get(Integer.parseInt(values[2])).setScale(2,RoundingMode.HALF_EVEN));
                    wrLine.flush();
                    }
                }
             } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }


static BigDecimal  median (Double[] numArray) {
    //CODING A FORMULA TO CALCULATE median
        Arrays.sort(numArray);   double median;

        if (numArray.length % 2 == 0)
                median = ( numArray[numArray.length / 2] + numArray[numArray.length / 2 - 1]) / 2;
        else
                median =  numArray[numArray.length/2];

        return BigDecimal.valueOf(median);
    }
}
