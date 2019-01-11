package cn.qr.method;

/**
 * @author QianRui
 * 2018/12/26
 */
public class LifetimeTest {
    private static final int K = 1024;
    private static final int M = K * K;
    private static final int G = K * M;

    private static final int OBJ_SIZE = (int) (1 * M);

    public static void main(String[] args) {
        int length = OBJ_SIZE / 64;
        ObjectOf64Bytes[] of64Bytes = new ObjectOf64Bytes[length];
        for (long i = 0; i < G; i++) {
            of64Bytes[(int) (i % length)] = new ObjectOf64Bytes();
        }
    }
}

class ObjectOf64Bytes {
    long placeholder0;
    long placeholder1;
    long placeholder2;
    long placeholder3;
    long placeholder4;
    long placeholder5;
}
/*
java -XX:SurvivorRatio=600  -XX:+PrintGC -Xmn1000M -XX:PretenureSizeThreshold=10000 cn.qr.method.LifetimeTest
java  -XX:SurvivorRatio=600  -XX:+PrintHeapAtGC -XX:+PrintGC -Xmn1000M -XX:PretenureSizeThreshold=10000 cn.qr.method.LifetimeTest

*/