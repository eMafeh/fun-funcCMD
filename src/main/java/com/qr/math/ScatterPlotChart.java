package com.qr.math;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;

/**
 * @author shenchao 散点图
 */
public class ScatterPlotChart {
    static DefaultXYDataset xydataset = new DefaultXYDataset();

    static {
        JFreeChart chart = ChartFactory.createScatterPlot("showCount", "number", "counts", xydataset, PlotOrientation.VERTICAL, true, false, false);
        ChartFrame frame = new ChartFrame("prime", chart, true);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderPaint(Color.GREEN);
        chart.setBorderStroke(new BasicStroke(1.5f));
        XYPlot xyplot = chart.getXYPlot();
        xyplot.getRenderer().setSeriesPaint(0, Color.RED);
        xyplot.getRenderer().setSeriesPaint(1, Color.BLUE);
        xyplot.getRenderer().setSeriesPaint(2, Color.yellow);

        xyplot.setBackgroundPaint(Color.black);//new Color(255, 253, 246)
        ValueAxis vaaxis = xyplot.getDomainAxis();
        vaaxis.setAxisLineStroke(new BasicStroke(1.5f));

        ValueAxis va = xyplot.getDomainAxis(0);
        va.setAxisLineStroke(new BasicStroke(1.5f));

        va.setAxisLineStroke(new BasicStroke(1.5f)); // 坐标轴粗细
        va.setAxisLinePaint(new Color(215, 215, 215)); // 坐标轴颜色
        xyplot.setOutlineStroke(new BasicStroke(1.5f)); // 边框粗细
        va.setLabelPaint(new Color(10, 10, 10)); // 坐标轴标题颜色
        va.setTickLabelPaint(new Color(102, 102, 102)); // 坐标轴标尺值颜色
        ValueAxis axis = xyplot.getRangeAxis();
        axis.setAxisLineStroke(new BasicStroke(1.5f));
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
                .getRenderer();
        xylineandshaperenderer.setSeriesOutlinePaint(2, Color.white);
        xylineandshaperenderer.setUseOutlinePaint(true);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setTickMarkInsideLength(2.0F);
        numberaxis.setTickMarkOutsideLength(0.0F);
        numberaxis.setAxisLineStroke(new BasicStroke(1.5f));

        frame.pack();
        frame.setVisible(true);
    }

    static int num;

    public static void showChart(double[][]... datas) {
        for (int i = 0; i < datas.length; i++) {
            xydataset.addSeries(num++, datas[i]);
        }
    }

}
