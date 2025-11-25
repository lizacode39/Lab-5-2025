import functions.*;
import functions.basic.*;

public class Main {
    public static void main(String[] args) {
        // Создаём точки
        FunctionPoint[] points1 = {
                new FunctionPoint(0.0, 1.0),
                new FunctionPoint(1.0, 2.718),
                new FunctionPoint(2.0, 7.389)
        };
        FunctionPoint[] points2 = {
                new FunctionPoint(0.0, 1.0),
                new FunctionPoint(1.0, 2.718),
                new FunctionPoint(2.0, 7.389)
        };

        //  1. Создание объектов
        ArrayTabulatedFunction array1 = null;
        LinkedListTabulatedFunction list1 = null;
        ArrayTabulatedFunction array2 = null;

        try {
            array1 = new ArrayTabulatedFunction(points1, 3);
            list1 = new LinkedListTabulatedFunction(points1, 3);
            array2 = new ArrayTabulatedFunction(points2, 3);
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при создании функций: " + e.getMessage());
            return;
        }

        //2. toString
        System.out.println("toString");
        System.out.println("Array: " + array1);
        System.out.println("List:  " + list1);

        //  3. equals
        System.out.println("\nequals");
        System.out.println("array1.equals(array2): " + array1.equals(array2));
        System.out.println("array1.equals(list1):  " + array1.equals(list1));
        System.out.println("array1.equals(null):   " + array1.equals(null));

        //  4. hashCode
        System.out.println("\nhashCode ");
        System.out.println("array1.hashCode(): " + array1.hashCode());
        System.out.println("array2.hashCode(): " + array2.hashCode());
        System.out.println("list1.hashCode():  " + list1.hashCode());

        // Проверяем согласованность
        System.out.println("array1.hashCode() == array2.hashCode(): " + (array1.hashCode() == array2.hashCode()));

        // Меняем одну точку
        FunctionPoint[] points3 = {
                new FunctionPoint(0.0, 1.0),
                new FunctionPoint(1.0, 2.719),
                new FunctionPoint(2.0, 7.389)
        };

        ArrayTabulatedFunction array3 = null;
        try {
            array3 = new ArrayTabulatedFunction(points3, 3);
        } catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при создании array3: " + e.getMessage());
            return;
        }

        System.out.println("array3.hashCode(): " + array3.hashCode());
        System.out.println("array1.equals(array3): " + array1.equals(array3));

        // 5. clone
        System.out.println("\nclone");
        try {
            ArrayTabulatedFunction arrayClone = (ArrayTabulatedFunction) array1.clone();
            System.out.println("Клон создан: " + arrayClone);

            // Меняем оригинал
            array1.setPointY(0, 999.0);
            System.out.println("После изменения оригинала:");
            System.out.println("Оригинал: " + array1.getPointY(0));
            System.out.println("Клон:     " + arrayClone.getPointY(0));

            // То же для списка
            LinkedListTabulatedFunction listClone = (LinkedListTabulatedFunction) list1.clone();
            list1.setPointY(0, 888.0);
            System.out.println("Список оригинал: " + list1.getPointY(0));
            System.out.println("Список клон:     " + listClone.getPointY(0));

        } catch (Exception e) {
            System.err.println("Ошибка при клонировании: " + e.getMessage());
        }
    }
}