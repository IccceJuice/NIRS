package sample.utils;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.HashMap;
import java.util.Map;

public class BarChartUtils {

    public static BarChart<Number, Number> createBarChartFromArray(HashMap<Integer, Integer> map) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Pixel brightness");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Density");
        BarChart<Number, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<Number, Number> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("2014");
        for (Map.Entry<Integer, Integer> entry: map.entrySet()) {
            dataSeries1.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(dataSeries1);
        barChart.setTitle("Hist of win values");
        return barChart;
    }


}

