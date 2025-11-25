package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions(){}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws InappropriateFunctionPointException {
        if(leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder() || pointsCount < 2){
            throw new IllegalArgumentException();
        }
        FunctionPoint[] pointArray = new FunctionPoint[pointsCount + pointsCount/2];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++){
            double x = leftX + step*i;
            pointArray[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return new ArrayTabulatedFunction(pointArray, pointsCount);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out){
        try{
            DataOutputStream output = new DataOutputStream(out);
            int pointCount = function.getPointCount();
            output.writeInt(pointCount);
            for (int i = 0; i < pointCount; i++){
                output.writeDouble(function.getPointX(i));
                output.writeDouble(function.getPointY(i));
            }
            output.close();
        }
        catch (IOException e){
            System.out.println("Произошла ошибка!");
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try{
            DataInputStream input = new DataInputStream(in);
            int pointCount = input.readInt();
            FunctionPoint[] pointArray = new FunctionPoint[pointCount + pointCount/2];
            for(int i = 0; i < pointCount; i++){
                pointArray[i] = new FunctionPoint(input.readDouble(), input.readDouble());
            }
            input.close();
            return new ArrayTabulatedFunction(pointArray, pointCount);
        }
        catch(IOException e) {
            System.out.println("Произошла ошибка!");
        }
        catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка в создании класса");
        }
        return null;
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        try{
            BufferedWriter output = new BufferedWriter(out);
            int pointCount = function.getPointCount();
            output.write(pointCount + " ");
            for (int i = 0; i < pointCount; i++) {
                output.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
            }
            output.close();
        }
        catch(IOException e) {
            System.out.println("Произошла ошибка!");
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer input = new StreamTokenizer(in);
            input.nextToken();
            int pointCount = (int) input.nval;
            FunctionPoint[] pointArray = new FunctionPoint[pointCount + pointCount / 2];
            for (int i = 0; i < pointCount; i++) {
                input.nextToken();
                double x = (double) input.nval;
                input.nextToken();
                double y = (double) input.nval;
                pointArray[i] = new FunctionPoint(x,y);
            }
            return new ArrayTabulatedFunction(pointArray, pointCount);
        }
        catch(IOException e) {
            System.out.println("Произошла ошибка!");
        }
        catch (InappropriateFunctionPointException e) {
            System.out.println("Ошибка в создании класса");
        }
        return null;
    }
}
