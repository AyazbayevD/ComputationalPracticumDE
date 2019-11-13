package sample;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;

class ImprovedEuler extends Controller {

    private static double k2(double x, double y, double k){
        return derivativeAtPoint(x + h, y + h * k);
    }

    static double findMaxGlobal(int numberOfSteps){

        double stepLength = (X - x0) / numberOfSteps;
        double maxGlobalError = 0.0;
        double prevY = 0.0;

        for (int i = 1; i <= numberOfSteps; ++i){

            double x = x0 + stepLength * i;

            double K1 = derivativeAtPoint(x - stepLength, prevY);
            double K2 = k2(x - stepLength, prevY, K1);

            double y = prevY + stepLength * 0.5 * (K1 + K2);

            maxGlobalError = Math.max(maxGlobalError, Math.abs(exactSolution.calculate(x) - y));

            prevY = y;
        }
        return maxGlobalError;
    }

    static void compute(){
        XYChart.Series<Number, Number> improvedEuler = new XYChart.Series<>();
        XYChart.Series<Number, Number> improvedEulerLocalError = new XYChart.Series<>();

        improvedEuler.getData().add(new XYChart.Data<>(x0, y0));
        improvedEulerLocalError.getData().add(new XYChart.Data<>(x0, 0.0));

        for (int i = 1; i <= N; ++i){
            double K1, K2;
            double prevX = improvedEuler.getData().get(i - 1).getXValue().doubleValue();
            double prevY = improvedEuler.getData().get(i - 1).getYValue().doubleValue();

            K1 = derivativeAtPoint(prevX, prevY);
            K2 = k2(prevX, prevY, K1);

            double x = x0 + h * i;
            double y = prevY + h * 0.5 * (K1 + K2);

            improvedEuler.getData().add(new XYChart.Data<>(x, y));

            double prevExactY = exactSolutionY.get(i - 1);

            K1 = derivativeAtPoint(prevX, prevExactY);
            K2 = k2(prevX, prevExactY, K1);

            double A = 0.5 * (K1 + K2);

            improvedEulerLocalError.getData().add(new XYChart.Data<>(x, Math.abs(exactSolutionY.get(i) - exactSolutionY.get(i - 1) - h * A)));
        }
        improvedEuler.setName("Improved Euler method");
        improvedEulerLocalError.setName("Improved Euler method");

        localErrorsChart.getData().add(improvedEulerLocalError);
        methodsChart.getData().add(improvedEuler);
    }
}
