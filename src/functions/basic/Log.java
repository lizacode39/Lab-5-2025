package functions.basic;

import functions.Function;

public class Log implements Function {

    private double base = Math.E;

    public Log(double base){
        this.base = base;
    }

    public double getLeftDomainBorder(){
        return 0;
    }

    public double getRightDomainBorder(){
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x){
        return Math.log(x)/Math.log(base);
    }
}
