package com.qr.math;

/**
 * @author QianRui
 * 2018/11/9
 */
public class StreamLimiter {
    static void collect(int[] target, int to) {
        int i = (target.length + to - 1) / to;
        double[][] doubles = new double[2][to];
        double[][] doubles2 = new double[2][to];
        int index = 0;
        for (int j = 0; j < target.length; j += i) {
            MaxMin maxMin = markMaxMin(target, j, j + i);
            doubles[1][index] = maxMin.min;
            doubles[0][index] = maxMin.minIndex;
            doubles2[1][index] = maxMin.max;
            doubles2[0][index] = maxMin.maxIndex;
            index++;
        }
        ScatterPlotChart.showChart(doubles, doubles2);
    }

    private static MaxMin markMaxMin(int[] target, int start, int end) {
        MaxMin maxMin = new MaxMin();
        for (int i = start; i < end && i < target.length; i++) {
            maxMin.mark(target[i], i * 2);
        }
        return maxMin;
    }

}

class MaxMin {
    int max;
    int maxIndex;
    int min;
    int minIndex;

    public void mark(int i, int index) {
        if (i > max) {
            max = i;
            maxIndex = index;
        } else if (min == 0 || i < min) {
            min = i;
            minIndex = index;
        }
    }
}