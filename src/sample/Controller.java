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

public class Controller {

    //Values which are prompted from text fields and defined by user
    static double x0, y0, X;
    static int N;
    private static int N1, N2;

    //step size depending on user input values
    static double h;

    //exact solution of certain differential equation
    static Function exactSolution = new Function("y(x)=x*(3*x-1)");

    //fxml elements which are drawn on the main window
    @FXML TextField x0Field, y0Field, XField, NField, N1Field, N2Field;
    @FXML Button plotButton;

    //defining axes for the total errors chart
    private static NumberAxis totalErrorsXAxis = new NumberAxis();
    private static NumberAxis totalErrorsYAxis = new NumberAxis();

    //defining axes for the approximations and exact solution chart
    private static NumberAxis approximationsAndExactSolutionXAxis = new NumberAxis();
    private static NumberAxis pproximationsAndExactSolutionYAxis = new NumberAxis();

    //defining axes for the local errors chart
    private static NumberAxis localErrorsXAxis = new NumberAxis();
    private static NumberAxis localErrorsYAxis = new NumberAxis();

    //defining total errors chart, approximations and exact solution chart and local errors chart
    private static LineChart<Number, Number> totalErrorsChart = new LineChart<>(totalErrorsXAxis, totalErrorsYAxis);
    static LineChart<Number, Number> approximationsAndExactSolutionChart = new LineChart<>(approximationsAndExactSolutionXAxis, pproximationsAndExactSolutionYAxis);
    static LineChart<Number, Number> localErrorsChart = new LineChart<>(localErrorsXAxis, localErrorsYAxis);

    //function for calculating dy/dx of certain differential equation
    static double derivativeAtPoint(double xCoor, double yCoor){
        return 1.0 + 2.0 * yCoor / xCoor;
    }

    //function for naming axes of previously defined charts
    private static void namingAxes(){
        totalErrorsXAxis.setLabel("number of steps");
        totalErrorsYAxis.setLabel("maximum global error");

        approximationsAndExactSolutionXAxis.setLabel("x");
        pproximationsAndExactSolutionYAxis.setLabel("y");

        localErrorsXAxis.setLabel("x");
        localErrorsYAxis.setLabel("y");
    }

    //if we press "plot" button many times, we should erase data from previous pressings
    private static void clearDataFromPreviousGraphings(){
        approximationsAndExactSolutionChart.getData().clear();
        localErrorsChart.getData().clear();
        totalErrorsChart.getData().clear();
    }

    //extracting data which user inserted in text fields
    private static void readPrompts(TextField x0Field, TextField y0Field, TextField XField, TextField NField, TextField N1Field, TextField N2Field){
        x0 = Double.parseDouble(x0Field.getText());
        y0 = Double.parseDouble(y0Field.getText());
        X = Double.parseDouble(XField.getText());
        N = Integer.parseInt(NField.getText());
        N1 = Integer.parseInt(N1Field.getText());
        N2 = Integer.parseInt(N2Field.getText());
    }

    //computing step size depending on user input, namely X, x0, N
    private static void computeJump(){
        h = (X - x0) / N;
    }

    //naming charts, i.e. what chart displays what thing
    private static void namingCharts(){
        approximationsAndExactSolutionChart.setTitle("Approximations");
        localErrorsChart.setTitle("Local Errors");
        totalErrorsChart.setTitle("Total Errors");
    }

    //function for inserting into approximations and exact solution chart the exact solution
    private static void plotExact(){

        //first of all, create series which will be added soon to the approximations and exact solution chart
        XYChart.Series<Number, Number> exact = new XYChart.Series<>();

        //jumping step by step N times (N - user inserted value)
        for (int i = 0; i <= N; ++i){

            //calculating x and y coordinates of exact solution
            double x = x0 + h * i;
            double y = exactSolution.calculate(x);

            //adding the pair of coordinates to previously defined series
            exact.getData().add(new XYChart.Data<>(x, y));
        }
        //giving name to series containing many pairs of coordinates and inserting it to approximations and exact solution chart
        exact.setName("Exact");
        approximationsAndExactSolutionChart.getData().add(exact);
    }

    //function for calculating and inserting local errors of different computational methods to the local errors chart, and also
    //for calculating and inserting approximations of different computational methods to the approximations and exact solution chart
    private static void plotApproximationsAndLocalErrors(){
        //addressing classes for different computational methods which will calculate and insert their local errors to the local errors chart
        Euler.compute();
        ImprovedEuler.compute();
        RungeKutta.compute();
    }

    //function for calculating and inserting total errors of different computational methods to the total errors chart
    private static void plotTotalErrors(){

        //defining series for each method's local errors
        XYChart.Series<Number, Number> eulerTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> improvedEulerTotalErrors = new XYChart.Series<>();
        XYChart.Series<Number, Number> rungeKuttaTotalErrors = new XYChart.Series<>();

        //considering every step size from N1 to N2 (N1, N2 - user inserted values)
        for (int i = N1; i <= N2; ++i){
            //for each step size, calculating maximum global error, and collection of such results will be the total errors chart
            eulerTotalErrors.getData().add(new XYChart.Data<>(i, Euler.findMaxGlobal(i)));
            improvedEulerTotalErrors.getData().add(new XYChart.Data<>(i, ImprovedEuler.findMaxGlobal(i)));
            rungeKuttaTotalErrors.getData().add(new XYChart.Data<>(i, RungeKutta.findMaxGlobal(i)));
        }

        //naming each series correspondingly to methods for which they calculated maximum global errors
        eulerTotalErrors.setName("Euler method");
        improvedEulerTotalErrors.setName("Improved Euler method");
        rungeKuttaTotalErrors.setName("Runge Kutta method");

        //finally, inserting three series to the total errors chart
        totalErrorsChart.getData().add(eulerTotalErrors);
        totalErrorsChart.getData().add(improvedEulerTotalErrors);
        totalErrorsChart.getData().add(rungeKuttaTotalErrors);
    }

    //function which is to pop-up the new window with three charts when the user presses "plot" button
    private static void organiseLayoutForCharts(){

        //creating new window
        Stage stage = new Stage();

        //creating new container for horizontal alignment of charts
        HBox graphBox = new HBox();

        //adding charts one by one to the container
        graphBox.getChildren().add(approximationsAndExactSolutionChart);
        graphBox.getChildren().add(totalErrorsChart);
        graphBox.getChildren().add(localErrorsChart);

        //creating scene of the window, and defining its size
        Scene scene = new Scene(graphBox, 1000, 500);

        //embed created scene into the pop-up window
        stage.setScene(scene);

        //action for popping up created window with charts
        stage.show();
    }

    //function which executes when the "plot" button is pressed
    public void plot(ActionEvent actionEvent) {

        //naming axes of charts
        namingAxes();

        //when "plot" button pressed many times, we clear data from previous graphings many times
        clearDataFromPreviousGraphings();

        //reading user input
        readPrompts(x0Field, y0Field, XField, NField, N1Field, N2Field);

        //computing step size for computational methods
        computeJump();

        //naming each charts for which it corresponds to
        namingCharts();

        //inserting exact solution into the approximations and exact solution chart
        plotExact();

        //inserting approximations to the approximations and exact solution chart, and also
        //inserting local errors to the local errors chart
        plotApproximationsAndLocalErrors();

        //inserting total errors to the total errors chart
        plotTotalErrors();

        //defining for each chart its layout, i.e. position, and showing them with pop-up window
        organiseLayoutForCharts();
    }
}
