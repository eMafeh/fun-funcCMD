package com.qr.math;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author QianRui
 * 2018/11/8
 */
public class TestNumber {

    public static void main(String[] args) throws InterruptedException {
        int[] file = getFile("D:/1000万.txt");
        file = Arrays.stream(file).limit(1000000).toArray();
        double[][] doubles = toFileChart(file);
        System.out.println("ok");
        Thread.sleep(3000L);
        StreamLimiter.collect(file, 100);
        ScatterPlotChart.showChart(doubles);

//        toChart(getComputer());
//        toFileChart(getFile());

//        toFile(counts);

    }


    private static double[][] toChart(int[] counts) {
        double[][] datas = new double[2][counts.length];
        for (int i = 2; i < counts.length; i += 2) {
            datas[0][i] = i;
            datas[1][i] = counts[i];
        }
        return datas;
    }

    private static double[][] toFileChart(int[] counts) {
        double[][] datas = new double[2][counts.length];
        for (int i = 1; i < counts.length; i += 1) {
            datas[0][i] = i * 2;
            datas[1][i] = counts[i];
        }
        return datas;
    }

    private static void toFile(int[] counts) {
        File file = new File("D:/xxxx.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            for (int i = 2; i < counts.length; i += 2)
//            System.out.println(i + " : " + counts[i]);
//                System.out.println(counts[i]);
                fileOutputStream.write((counts[i] + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] getFile(String fileName) {
        File file = new File(fileName);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[1024 * 1024 * 100];
            int read = inputStream.read(bytes);
            return Arrays.stream(new String(bytes, 0, read).split("\r\n")).mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int[] getComputer() {
        int MAX = 10000000;
        boolean[] is = new boolean[MAX + 1];
        for (int i = 2; i <= MAX; i++)
            if (is[i] = is(i, is))
                System.out.print(i + " ");

        int[] counts = new int[MAX + 5];

        for (int i = 1; i <= MAX; i++)
            if (is[i])
                for (int j = 1; j <= i; j++)
                    if (is[j] && i + j < counts.length)
                        counts[i + j]++;
        return counts;
    }

    private static boolean is(int i, boolean[] is) {
        for (int j = 2; j * j <= i; j++)
            if (is[j] && i % j == 0) return false;
        return true;
    }
}
