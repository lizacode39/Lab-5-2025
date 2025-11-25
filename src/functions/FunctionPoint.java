package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private static final double EPS = 1e-10;
    private double x;
    private double y;

    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionPoint point = (FunctionPoint) o;
        return Math.abs(x - point.x) < EPS && Math.abs(y - point.y) < EPS;
    }

    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        return (int) (xBits ^ (xBits >>> 32) ^ yBits ^ (yBits >>> 32));
    }

    public Object clone() {
        return new FunctionPoint(this);
    }
}