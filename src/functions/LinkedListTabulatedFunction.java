package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private static final double EPS = 1e-10;

    public LinkedListTabulatedFunction() {
    }

    private static class FunctionNode implements Serializable {
        private FunctionPoint point;
        private FunctionNode prev = null;
        private FunctionNode next = null;

        public FunctionNode() {
            point = null;
        }

        public FunctionNode(FunctionNode prev, FunctionPoint point, FunctionNode next) {
            this.point = new FunctionPoint(point);
            this.prev = prev;
            this.next = next;
        }
    }

    private FunctionNode head;
    private int sizeValue;

    private FunctionNode cacheNode;
    private int cacheIndex = 0;

    private void isFunctionPointIndexOutOfBoundsException(int index) {
        if (0 > index || index >= sizeValue) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы точек");
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        if (Math.abs(index - cacheIndex) < index && Math.abs(index - cacheIndex) < sizeValue - index) {
            if (cacheIndex > index) {
                while (index != cacheIndex) {
                    cacheNode = cacheNode.prev;
                    --cacheIndex;
                }
            } else {
                while (index != cacheIndex) {
                    cacheNode = cacheNode.next;
                    ++cacheIndex;
                }
            }
        } else if (index < sizeValue - index) {
            for (cacheIndex = 0, cacheNode = head.next; cacheIndex != index; ++cacheIndex) {
                cacheNode = cacheNode.next;
            }
        } else {
            for (cacheIndex = sizeValue - 1, cacheNode = head.prev; cacheIndex != index; --cacheIndex) {
                cacheNode = cacheNode.prev;
            }
        }
        return cacheNode;
    }

    private FunctionNode addNodeToTail() {
        ++sizeValue;
        FunctionNode newNode = new FunctionNode(head.prev, new FunctionPoint(), head);
        newNode.prev.next = newNode;
        head.prev = newNode;
        return newNode;
    }

    private FunctionNode addNodeToHead() {
        ++sizeValue;
        FunctionNode newNode = new FunctionNode(head, new FunctionPoint(), head.next);
        newNode.next.prev = newNode;
        head.next = newNode;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index == 0) return addNodeToHead();
        if (index == sizeValue) return addNodeToTail();
        ++sizeValue;
        FunctionNode oldNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(oldNode.prev, new FunctionPoint(), oldNode);
        newNode.prev.next = newNode;
        oldNode.prev = newNode;
        cacheNode = newNode;
        cacheIndex = index;
        return newNode;
    }

    private void deleteNodeByIndex(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        if (sizeValue < 3) {
            throw new IllegalStateException();
        }
        FunctionNode deleteNode = getNodeByIndex(index);
        deleteNode.prev.next = deleteNode.next;
        deleteNode.next.prev = deleteNode.prev;
        --sizeValue;
    }

    public LinkedListTabulatedFunction(FunctionPoint[] pointArray, int pointCount) {
        if (pointCount < 2) {
            throw new IllegalArgumentException();
        }
        sizeValue = 1;
        head = new FunctionNode();
        FunctionNode zeroNode = new FunctionNode(head, new FunctionPoint(pointArray[0]), head);
        head.next = zeroNode;
        head.prev = zeroNode;
        for (int i = 1; i < pointCount; ++i) {
            if (pointArray[i - 1].getX() >= pointArray[i].getX()) {
                throw new IllegalArgumentException();
            }
            addNodeToTail().point = new FunctionPoint(pointArray[i]);
        }
        cacheNode = head.next;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointCount) {
        if (leftX >= rightX || pointCount < 2) {
            throw new IllegalArgumentException();
        }

        double step = (rightX - leftX) / (pointCount - 1);
        sizeValue = 1;
        head = new FunctionNode();
        FunctionNode zeroNode = new FunctionNode(head, new FunctionPoint(leftX, 0), head);
        head.next = zeroNode;
        head.prev = zeroNode;
        for (int i = 1; i < pointCount; ++i) {
            addNodeToTail().point = new FunctionPoint(leftX + step * i, 0);
        }
        cacheNode = head.next;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] value) {
        if (leftX >= rightX || value.length < 2) {
            throw new IllegalArgumentException();
        }
        double step = (rightX - leftX) / (value.length - 1);
        sizeValue = 1;
        head = new FunctionNode();
        FunctionNode zeroNode = new FunctionNode(head, new FunctionPoint(leftX, value[0]), head);
        head.next = zeroNode;
        head.prev = zeroNode;
        for (int i = 1; i < value.length; ++i) {
            addNodeToTail().point = new FunctionPoint(leftX + step * i, value[i]);
        }
        cacheNode = head.next;
    }

    public double getLeftDomainBorder() {
        return head.next.point.getX();
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX();
    }

    public int getPointCount() {
        return sizeValue;
    }

    public FunctionPoint getPoint(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return new FunctionPoint(getNodeByIndex(index).point); // Возвращаем КОПИЮ!
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        FunctionNode node = getNodeByIndex(index);
        if (index != 0 && index != sizeValue - 1) {
            if (node.prev.point.getX() < point.getX() && point.getX() < node.next.point.getX()) {
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else if (index == 0) {
            if (point.getX() < node.next.point.getX()) {
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else {
            if (node.prev.point.getX() < point.getX()) {
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        }
    }

    public double getPointX(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        isFunctionPointIndexOutOfBoundsException(index);
        return getNodeByIndex(index).point.getY();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        setPoint(index, new FunctionPoint(x, getPointY(index)));
    }

    public void setPointY(int index, double y) {
        isFunctionPointIndexOutOfBoundsException(index);
        getNodeByIndex(index).point.setY(y);
    }

    public double getFunctionValue(double x) {
        int index;
        if ((head.next.point.getX() <= x) && (x <= head.prev.point.getX())) {
            for (index = 0; index < sizeValue && getPointX(index) < x; index++) ;
            if (x == getPointX(index)) return getPointY(index);
            double x_1 = getPointX(index - 1);
            double y_1 = getPointY(index - 1);
            double x_2 = getPointX(index);
            double y_2 = getPointY(index);
            return ((x - x_1) * (y_2 - y_1)) / (x_2 - x_1) + y_1;
        }
        return Double.NaN;
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int index;
        for (index = 0; index < sizeValue && getPointX(index) < point.getX(); index++) ;
        if (index != sizeValue && getNodeByIndex(index).point.getX() == point.getX()) {
            throw new InappropriateFunctionPointException("есть точка, абсцисса которой совпадает с абсциссой добавляемой точки");
        } else {
            addNodeByIndex(index).point = new FunctionPoint(point);
        }
    }

    public void printList() {
        FunctionNode printNode = head.next;
        for (int i = 0; i < sizeValue; ++i) {
            System.out.println("№" + i + " x:" + printNode.point.getX() + " y" + printNode.point.getY());
            printNode = printNode.next;
        }
    }

    public void printListR() {
        FunctionNode printNode = head.prev;
        for (int i = sizeValue - 1; i >= 0; --i) {
            System.out.println("№" + i + " x:" + printNode.point.getX() + " y:" + printNode.point.getY());
            printNode = printNode.prev;
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(sizeValue);
        FunctionNode current = head.next;
        for (int i = 0; i < sizeValue; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int pointCount = in.readInt();
        sizeValue = 0;
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        for (int i = 0; i < pointCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail().point = new FunctionPoint(x, y);
        }
        cacheNode = head.next;
        cacheIndex = 0;
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
        if (o == null || !(o instanceof TabulatedFunction)) return false;

        TabulatedFunction other = (TabulatedFunction) o;
        if (sizeValue != other.getPointCount()) return false;

        FunctionNode current = head.next;
        for (int i = 0; i < sizeValue; i++) {
            if (!current.point.equals(other.getPoint(i))) {
                return false;
            }
            current = current.next;
        }
        return true;
    }

    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + sizeValue;
        FunctionNode current = head.next;
        for (int i = 0; i < sizeValue; i++) {
            hash = 31 * hash + current.point.hashCode();
            current = current.next;
        }
        return hash;
    }

    public Object clone() {
        FunctionPoint[] points = new FunctionPoint[sizeValue];
        FunctionNode current = head.next;
        for (int i = 0; i < sizeValue; i++) {
            points[i] = new FunctionPoint(current.point);
            current = current.next;
        }
        return new LinkedListTabulatedFunction(points, sizeValue);
    }
}