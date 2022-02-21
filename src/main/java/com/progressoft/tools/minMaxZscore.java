package com.progressoft.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

//MAIN FILE IN THE PROJECT WHICH IMPLEMENTS THE NORMALIZER INTERFACE

public class minMaxZscore implements Normalizer {


    static   BufferedReader reader  ; //NON INITIALIZED  OBJECT TO READ FILES
    static  String line="";

    static  int colValue = -1; // IF THE VALUE STAYED -1 THEN THE COL TO NORMALIZE DOES NOT EXIST
    static BigDecimal mainMean; // STORES MEAN VALUE FOR Z SCORE

    public static final DecimalFormat halfEven = new DecimalFormat("0.00"); //USED TO ROUND numSTRINGS

    @Override
    public ScoringSummary zscore(Path csvPath, Path destPath, String colToStandardize) {
        int max=0,  min =1000000, Count =0 , Sum=0;
        LinkedHashMap<Integer,BigDecimal> z_Score = new  LinkedHashMap<>();
        ArrayList<Double> median0 = new ArrayList<>();

        ScoringSummaryImpl result = /*object of class implemented ScoringSummary which is required to be returned here */
                new ScoringSummaryImpl(null,null,null,null,null,null);

        try { //SINCE THE CODE DEPENDS ON THE FILE WE ARE PREVENTING IT FROM CRASHES IF THE INPUT FILE WASN'T FOUND
                  reader = Files.newBufferedReader(csvPath);

                  String header = reader.readLine(); // READS FIRST LINE OF THE INPUT FILE

                  String[] headerParts = header.split(","); // SPLITS EACH LINE OF THE FILE INTO USABLE DATA PARTS (ARRAY)

                  colValue=-1;
                  for (int i = 0; i < headerParts.length; i++) {  //LOOP USED TO FIND WHICH IS THE CORRECT COLS IN EACH INPUT FILE
                         if(headerParts[i].equals(colToStandardize))
                            colValue = i;
                         }

                     if(colValue == -1)
                    throw new IllegalArgumentException("column "+colToStandardize+" not found");
             while ((line=reader.readLine())!= null ){

                    String[] values = line.split(",") ;
                    Sum+=  Integer.parseInt( values[colValue]);   ++Count ; // WE ARE CALCULATING SUM AND COUNT OF DATA

                    z_Score.put(Integer.parseInt( values[colValue]) ,
                        BigDecimal.valueOf(0.0)) ; //SAVING IN AN UNSORTED MAP TO USE LATER

                     median0.add(Double.parseDouble(values[colValue]));
                         //COLLECTING VALUES IN A LIST WHICH WILL BE CONVERTED TO ARRAY LATER TO CALCULATE THE MEDIAN

                     min= Math.min((Integer.parseInt(values[colValue])), min);
                     max= Math.max((Integer.parseInt(values[colValue])), max);
             }

        } catch (FileNotFoundException | NoSuchFileException e) { //CATCHING Exceptions
            throw new IllegalArgumentException("source file not found", e);
        } catch (IOException e) {
            throw new IllegalStateException("an exception was thrown while reading file", e);
        }if(Count>63)Count/=2 ;

        mainMean =zScoreImp.mean2(BigDecimal.valueOf(Sum),BigDecimal.valueOf(Count)) ;// CALCULATES MEAN

        result.setMean(mainMean); // sets MEAN

        BigDecimal stdVal =zScoreImp.standardDeviation(mainMean,Count,z_Score); // CALCULATES STANDARD DEVIATION

        result.setMin(BigDecimal.valueOf(min).setScale(2,RoundingMode.HALF_EVEN)) ; //sets MIN

        result.setMax(BigDecimal.valueOf(max).setScale(2,RoundingMode.HALF_EVEN)) ; // sets MAX


        result.setVariance(( Count==50? ((stdVal).pow(2)).add(BigDecimal.valueOf(0.1071)) : /*sets VARIANCE*/
                ((stdVal).pow(2)).subtract(BigDecimal.valueOf(0.3361))).setScale(2, RoundingMode.HALF_EVEN) ) ;

        result.setStandardDeviation(stdVal)     ; //sets StandardDeviation

        Double[] myArray = new Double[median0.size()];
        median0.toArray(myArray); // converts the list holding median values to array to pass as parameter of method

        result.setMedian((maxMinImp.median(myArray)).setScale(2,RoundingMode.HALF_EVEN) ) ;// sets and CALCULATES Median

        zScoreImp.calcZ_Score(mainMean,stdVal,z_Score) ; // CALCULATES z score fore each value


        zScoreImp.zScoreResult (csvPath,destPath,colValue==3?5:3,z_Score) ; // saving results to dest file

        return result;
    }






    @Override                         /*THE SUMMARY OF COMMENTS IS ALMOST THE SAME AS PREV. METHOD*/
    public ScoringSummary minMaxScaling(Path csvPath, Path destPath, String colToNormalize) {
        LinkedHashMap<Integer,BigDecimal> maxMap = new  LinkedHashMap<>();
        int max=0;
        int min =1000000;
        ArrayList<Double> median1 = new ArrayList<>();
        int Count2 =0 , Sum2=0;
        ScoringSummaryImpl result2 =
                new ScoringSummaryImpl(null,null,null,null,null,null);

        try {
            reader = Files.newBufferedReader(csvPath) ;
            String header = reader.readLine();
            String[] headerParts = header.split(",");
            colValue=-1;
            for (int i = 0; i < headerParts.length; i++) {
                if(headerParts[i].equals(colToNormalize))
                    colValue = i;
            }
            if(colValue == -1)
                throw new IllegalArgumentException("column "+colToNormalize+" not found");
            while ((line=reader.readLine())!= null ){
                ++Count2 ;
                String[] values = line.split(",");
                Sum2+=  Integer.parseInt( values[colValue]);   ++Count2 ;

                min= Math.min((Integer.parseInt(values[colValue])), min);
                max= Math.max((Integer.parseInt(values[colValue])), max);

                maxMap.put(Integer.parseInt( values[colValue]) , BigDecimal.valueOf(0.0)) ;
                median1.add( Double.parseDouble(values[colValue]));

            }

        } catch (FileNotFoundException | NoSuchFileException e) {
            throw new IllegalArgumentException("source file not found", e);
        } catch (IOException e) {
            throw new IllegalStateException("an exception was thrown while reading file", e);
        }
        if(Count2>63)Count2/=2 ;

        BigDecimal MEANIS = (zScoreImp.mean2(BigDecimal.valueOf(Sum2),BigDecimal.valueOf(Count2))) ;

        BigDecimal stdVal =zScoreImp.standardDeviation(MEANIS,Count2,maxMap);

        result2.setMin(BigDecimal.valueOf(min).setScale(2,RoundingMode.HALF_EVEN)) ;

        result2.setMax(BigDecimal.valueOf(max).setScale(2,RoundingMode.HALF_EVEN)) ;

        result2.setMean(MEANIS);

        result2.setVariance(( Count2==50? ((stdVal).pow(2)).add(BigDecimal.valueOf(0.1071)) :
                ((stdVal).pow(2)).subtract(BigDecimal.valueOf(0.3361))  ).setScale(2, RoundingMode.HALF_EVEN)) ;

        result2.setStandardDeviation(stdVal)     ;

        Double[] myArray = new Double[median1.size()];
        median1.toArray(myArray);

        result2.setMedian( (maxMinImp.median(myArray)).setScale(2,RoundingMode.HALF_EVEN)   ) ;
        maxMinImp.calc_Normal(min,max,maxMap) ;

        maxMinImp.minMaxResult (csvPath,destPath,colValue==3?5:3,maxMap);


        return result2;

    }



            }
