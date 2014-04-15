package br.usp.each.saeg.code.forest.demo.commons;

import org.apache.commons.math.stat.*;

public class CommonsMathDemo {

    public static void main(String[] args) {
        System.out.println(StatUtils.mean(new double[] { 10, 0, 0, 5 }));
        System.out.println(StatUtils.variance(new double[] { 10, 0, 0, 5 }));
        System.out.println(StatUtils.sumSq(new double[] { 10, 0, 0, 5 }));
    }
}
