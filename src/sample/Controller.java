package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;

public class Controller {

    static double h, x0, y0, X;
    static int N, N1, N2;
    static Function exactSolution = new Function("y(x)=x*(3*x-1)");

    @FXML TextField x0Field, y0Field, XField, NField, N1Field, N2Field;
    @FXML Button plotButton;

    static ArrayList<Double> exactSolutionY = new ArrayList<>();

    private static NumberAxis xAxis = new NumberAxis();
    private static NumberAxis yAxis = new NumberAxis();

    private static NumberAxis x1Axis = new NumberAxis();
    private static NumberAxis y1Axis = new NumberAxis();

    private static NumberAxis x2Axis = new NumberAxis();
    private static NumberAxis y2Axis = new NumberAxis();


    static LineChart<Number, Number> totalErrorsChart = new LineChart<>(xAxis, yAxis);
    static LineChart<Number, Number> methodsChart = new LineChart<>(x1Axis, y1Axis);
    static LineChart<Number, Number> localErrorsChart = new LineChart<>(x2Axis, y2Axis);


    private static void readPrompts(TextField x0Field, TextField y0Field, TextField XField, TextField NField, TextField N1Field, TextField N2Field){
        x0 = Double.parseDouble(x0Field.getText());
        y0 = Double.parseDouble(y0Field.getText());
        X = Double.parseDouble(XField.getText());
        N = Integer.parseInt(NField.getText());
        N1 = Integer.parseInt(N1Field.getText());
        N2 = Integer.parseInt(N2Field.getText());
    }

    static double derivativeAtPoint(double xCoor, double yCoor){
        return yCoor * 2.0 / xCoor + 1.0;
    }

    private static void computeJump(){
        h = (X - x0) / N;
    }

    public void plot(ActionEvent actionEvent) {
        xAxis.setLabel("number of steps");
        yAxis.setLabel("maximum global error");

        x1Axis.setLabel("x");
        y1Axis.setLabel("y");

        x2Axis.setLabel("x");
        y2Axis.setLabel("y");

        exactSolutionY.clear();
        methodsChart.getData().clear();
        localErrorsChart.getData().clear();
        totalErrorsChart.getData().clear();

        readPrompts(x0Field, y0Field, XField, NField, N1Field, N2Field);

        computeJump();


        //String exactSolution = Exact.computeExact(derivativeField, x0Field, y0Field);
        //System.out.println(exactSolution);
        plotExact();


        plotMethodsAndLocalErrors();
        methodsChart.setTitle("Approximations");
        localErrorsChart.setTitle("Local errors");
        totalErrorsChart.setTitle("Total errors");

        plotTotalErrors();

        Stage stage = new Stage();
        HBox graphBox = new HBox();
        graphBox.getChildren().add(methodsChart);
        graphBox.getChildren().add(totalErrorsChart);
        graphBox.getChildren().add(localErrorsChart);
        Scene scene = new Scene(graphBox, 1000, 500);
        stage.setScene(scene);


        stage.show();
    }
    private static void plotMethodsAndLocalErrors(){
        Euler.compute();
        ImprovedEuler.compute();
        RungeKutta.compute();
    }
    private static void plotTotalErrors(){

        XYChart.Series<Number, Number> eulerTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> improvedEulerTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> rungeKuttaTotalErrors = new XYChart.Series<>();

        for (int i = N1; i <= N2; ++i){
            eulerTotalErrors.getData().add(new XYChart.Data<>(i, Euler.findMaxGlobal(i)));
            improvedEulerTotalErrors.getData().add(new XYChart.Data<>(i, ImprovedEuler.findMaxGlobal(i)));
            rungeKuttaTotalErrors.getData().add(new XYChart.Data<>(i, RungeKutta.findMaxGlobal(i)));
        }

        eulerTotalErrors.setName("Euler method");
        improvedEulerTotalErrors.setName("Improved Euler method");
        rungeKuttaTotalErrors.setName("Runge Kutta method");

        totalErrorsChart.getData().add(eulerTotalErrors);
        totalErrorsChart.getData().add(improvedEulerTotalErrors);
        totalErrorsChart.getData().add(rungeKuttaTotalErrors);


    }
    private static void plotExact(){

        XYChart.Series<Number, Number> exact = new XYChart.Series<>();

        for (int i = 0; i <= N; ++i){
            double x = x0 + h * i;
            double y = exactSolution.calculate(x);

            exactSolutionY.add(y);
            exact.getData().add(new XYChart.Data<>(x, y));

            System.out.println(x + " " + y);
        }
        System.out.println(exactSolutionY.size());
        exact.setName("Exact");

        methodsChart.getData().add(exact);
    }

}
