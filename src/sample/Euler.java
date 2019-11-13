package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.ArrayList;

class Euler extends Controller {

    static double findMaxGlobal(int numberOfSteps){

        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double prevY = 0.0;

        for (int i = 1; i <= numberOfSteps; ++i){
            double x = x0 + stepLength * i;
            double y = prevY + stepLength * derivativeAtPoint(x - stepLength, prevY);

            maxGlobalError = Math.max(maxGlobalError, Math.abs(exactSolution.calculate(x) - y));

            prevY = y;
        }
        return maxGlobalError;
    }

    static void compute(){
        XYChart.Series<Number, Number> euler = new XYChart.Series<>();
        XYChart.Series<Number, Number> eulerLocalError = new XYChart.Series<>();

        euler.getData().add(new XYChart.Data<>(x0, y0));
        eulerLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        for (int i = 1; i <= N; ++i){



            double x = x0 + h * i;
            double y = euler.getData().get(i - 1).getYValue().doubleValue() + h * derivativeAtPoint(euler.getData().get(i - 1).getXValue().doubleValue(), euler.getData().get(i - 1).getYValue().doubleValue());

            euler.getData().add(new XYChart.Data<>(x, y));
            eulerLocalError.getData().add(new XYChart.Data<>(x, Math.abs(exactSolutionY.get(i) - exactSolutionY.get(i - 1) - h * derivativeAtPoint(x - h, exactSolutionY.get(i - 1)))));
        }
        euler.setName("Euler method");
        eulerLocalError.setName("Euler method");

        localErrorsChart.getData().add(eulerLocalError);
        methodsChart.getData().add(euler);
    }

}
