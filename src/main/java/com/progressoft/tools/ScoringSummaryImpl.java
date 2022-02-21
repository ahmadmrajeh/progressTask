package com.progressoft.tools;

import java.math.BigDecimal;

public class ScoringSummaryImpl implements ScoringSummary {
    private BigDecimal mean;
    private BigDecimal standardDeviation ;
    private BigDecimal variance ;
    private BigDecimal   median ;
    private BigDecimal  min;
    private BigDecimal   max;
    public ScoringSummaryImpl(BigDecimal mean,BigDecimal standardDeviation ,  /*CONSTRUCTOR*/
                              BigDecimal variance ,BigDecimal   median ,
                              BigDecimal  min,BigDecimal   max) {
        this.mean = mean;
        this.standardDeviation=standardDeviation ;
        this.median=median ;
        this.max=max ;
        this.variance =variance;
        this.min=min ;
    }

    public  ScoringSummaryImpl setMean(BigDecimal newMean) {
        this.mean = newMean;
        return this;
    }


    public  ScoringSummaryImpl setVariance(BigDecimal newVariance) {
        this.variance = newVariance;
        return this;
    }


    public  ScoringSummaryImpl setMin(BigDecimal newMin) {
        this.min = newMin;
        return this;
    }

    public  ScoringSummaryImpl setMax(BigDecimal newMax) {
        this.max = newMax;
        return this;
    }


    public  ScoringSummaryImpl setMedian(BigDecimal newMedian) {
        this.median = newMedian;
        return this;
    }

    public  ScoringSummaryImpl setStandardDeviation(BigDecimal newDeviation) {
        this.standardDeviation = newDeviation;
        return this;
    }


    @Override
    public BigDecimal mean() {
        return this.mean;
    }

    @Override
    public BigDecimal standardDeviation() {
        return this.standardDeviation;
    }

    @Override
    public BigDecimal variance() {
        return this.variance;
    }

    @Override
    public BigDecimal median() {
        return this.median;
    }

    @Override
    public BigDecimal min() {
        return this.min;
    }

    @Override
    public BigDecimal max() {
        return this.max;
    }
}
