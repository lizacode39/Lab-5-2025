package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final double EPS = 1e-10;
    private FunctionPoint[] pointValue;
    private int sizeValue;

    public ArrayTabulatedFunction(FunctionPoint[] pointArray, int pointCount) throws InappropriateFunctionPointException {
        if (pointCount < 2) {
            throw new IllegalArgumentException();
        }
        sizeValue = 1;
        pointValue = new FunctionPoint[pointCount + (pointCount / 2)];
        pointValue[0] = pointArray[0];
        for (int i = 1; i < pointCount; i++) {
            if (pointArray[i - 1].getX() >= pointArray[i].getX()) {
                throw new IllegalArgumentException();
            }
            addPoint(pointArray[i]);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointCount) {
        if (leftX >= rightX || pointCount < 2) {
            throw new IllegalArgumentException();
        }
        sizeValue = pointCount;
        pointValue = new FunctionPoint[2 * sizeValue];
        double step = (rightX - leftX) / (sizeValue - 1);
        for (int i = 0; i < sizeValue; i++) {
            pointValue[i] = new FunctionPoint(leftX + step * i, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] value) {
        if (leftX >= rightX || value.length < 2) {
            throw new IllegalArgumentException();
        }
        sizeValue = value.length;
        pointValue = new FunctionPoint[2 * sizeValue];
        double step = (rightX - leftX) / (sizeValue - 1);
        for (int i = 0; i < sizeValue; i++) {
            pointValue[i] = new FunctionPoint(leftX + step * i, value[i]);
        }
    }

    private void isFunctionPointIndexOutOfBoundsException(int index) {
        if (0 > index || index >= sizeValue) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы точек");
        }
    }

    public double getLeftDomainBorder() {
        return pointValue[0].getX();
    }

    public double getRightDomainBorder() {
        return pointValue[sizeValue - 1].getX();
    }

    public double getFunctionValue(double x) {
        int index;
        if ((pointValue[0].getX() <= x) && (x <= pointValue[sizeValue - 1].getX())) {
            for (index = 0; index < sizeValue && pointValue[index].getX() < x; index++) ;
            double x_1 = pointValue[index - 1].getX();
            double y_1 = pointValue[index - 1].getY();
            double x_2 = pointValue[index].getX();
            double y_2 = pointValue[index].getY();
            return ((x - x_1) * (y_2 - y_1)) / (x_2 - x_1) + y_1;
        }
        return Double.NaN;
    }

    public int getPointCount() {
        return sizeValue;
    }

    public FunctionPoint getPoint(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return new FunctionPoint(pointValue[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        if ((index != 0) && (index != sizeValue - 1)) {
            if ((pointValue[index - 1].getX() < point.getX()) && (point.getX() < pointValue[index + 1].getX())) {
                pointValue[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else if (index == 0) {
            if (point.getX() < pointValue[1].getX()) {
                pointValue[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else {
            if ((index == sizeValue - 1) && (pointValue[index - 1].getX() < point.getX())) {
                pointValue[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        }
    }

    public double getPointX(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return pointValue[index].getX();
    }

    public double getPointY(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return pointValue[index].getY();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        setPoint(index, new FunctionPoint(x, pointValue[index].getY()));
    }

    public void setPointY(int index, double y) {
        isFunctionPointIndexOutOfBoundsException(index);
        pointValue[index].setY(y);
    }

    public void deletePoint(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        if (sizeValue < 3) {
            throw new IllegalStateException();
        }
        pointValue[index] = null;
        for (int i = index; i < sizeValue - 1; i++) {
            pointValue[i] = pointValue[i + 1];
        }
        --sizeValue;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (pointValue.length < sizeValue + 1) {
            FunctionPoint[] newPointValue = new FunctionPoint[sizeValue + (sizeValue / 2)];
            System.arraycopy(pointValue, 0, newPointValue, 0, pointValue.length);
            pointValue = newPointValue;
        }
        int index;
        for (index = 0; index < sizeValue && pointValue[index].getX() < point.getX(); index++) ;
        if (index != sizeValue && pointValue[index].getX() == point.getX()) {
            throw new InappropriateFunctionPointException("есть точка, абсцисса которой совпадает с абсциссой добавляемой точки");
        } else {
            System.arraycopy(pointValue, index, pointValue, index + 1, sizeValue - index);
            pointValue[index] = new FunctionPoint(point);
            ++sizeValue;
        }
    }

    public void printTabFun() {
        for (int i = 0; i < sizeValue; i++) {
            System.out.println("№" + (i + 1) + " x:" + pointValue[i].getX() + " y:" + pointValue[i].getY());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append(getPoint(0).toString());
        for (int i = 1; i < sizeValue; i++) {
            sb.append(", ").append(getPoint(i).toString());
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof TabulatedFunction) {
            TabulatedFunction other = (TabulatedFunction) o;
            if (sizeValue != other.getPointCount()) return false;
            for (int i = 0; i < sizeValue; i++) {
                if (Math.abs(getPointX(i) - other.getPointX(i)) >= EPS ||
                        Math.abs(getPointY(i) - other.getPointY(i)) >= EPS) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + sizeValue;
        for (int i = 0; i < sizeValue; i++) {
            hash = 31 * hash + pointValue[i].hashCode();
        }
        return hash;
    }

    public Object clone() {
        FunctionPoint[] newPoints = new FunctionPoint[sizeValue];
        for (int i = 0; i < sizeValue; i++) {
            newPoints[i] = new FunctionPoint(pointValue[i]);
        }
        try {
            return new ArrayTabulatedFunction(newPoints, sizeValue);
        } catch (InappropriateFunctionPointException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }
}