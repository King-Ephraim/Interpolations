package com.artemis.interpolation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Définir les axes du graphique
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        // Créer le graphique en ligne
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Comparaison des interpolations : Lagrange, Newton et Moindres Carrés");

        // Points de données d'exemple
        double[] xPoints = {0, 1, 2, 3,5};
        double[] yPoints = {5, -9, 6, -1, 8};

        // Trier les points pour l'interpolation de Newton
        sortPoints(xPoints, yPoints);

        // Calculer les coefficients pour l'interpolation de Newton
        double[] coefficientsNewton = computeDividedDifferences(xPoints, yPoints);

        // Coefficients pour la méthode des moindres carrés (degré 2)
        int degree = 2; // Degré du polynôme
        double[] coefficientsLeastSquares = leastSquares(xPoints, yPoints, degree);

        // Série pour les points d'origine
        XYChart.Series<Number, Number> originalSeries = new XYChart.Series<>();
        originalSeries.setName("Points d'origine");
        for (int i = 0; i < xPoints.length; i++) {
            originalSeries.getData().add(new XYChart.Data<>(xPoints[i], yPoints[i]));
        }

        // Série pour la courbe de Lagrange
        XYChart.Series<Number, Number> lagrangeSeries = new XYChart.Series<>();
        lagrangeSeries.setName("Courbe de Lagrange");

        // Série pour la courbe de Newton
        XYChart.Series<Number, Number> newtonSeries = new XYChart.Series<>();
        newtonSeries.setName("Courbe de Newton");

        // Série pour la courbe des moindres carrés
        XYChart.Series<Number, Number> leastSquaresSeries = new XYChart.Series<>();
        leastSquaresSeries.setName("Courbe des moindres carrés");

        // Générer les points interpolés pour les courbes
        for (double xInterp = xPoints[0]; xInterp <= xPoints[xPoints.length - 1]; xInterp += 0.1) {
            double yLagrange = interpolateLagrange(xPoints, yPoints, xInterp);
            double yNewton = evaluateNewtonPolynomial(xInterp, xPoints, coefficientsNewton);
            double yLeastSquares = evaluateLeastSquares(xInterp, coefficientsLeastSquares);

            lagrangeSeries.getData().add(new XYChart.Data<>(xInterp, yLagrange));
            newtonSeries.getData().add(new XYChart.Data<>(xInterp, yNewton));
            leastSquaresSeries.getData().add(new XYChart.Data<>(xInterp, yLeastSquares));
        }

        // Ajouter les séries au graphique
        lineChart.getData().add(originalSeries);
        lineChart.getData().add(lagrangeSeries);
        lineChart.getData().add(newtonSeries);
        lineChart.getData().add(leastSquaresSeries);

        // Améliorer le style du graphique
        lineChart.setCreateSymbols(false); // Supprimer les symboles sur les courbes

        // Afficher le graphique dans une scène
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setTitle("Comparaison des interpolations");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Interpolation de Lagrange
    public static double interpolateLagrange(double[] x, double[] y, double xInterp) {
        double result = 0.0;
        int n = x.length;

        for (int i = 0; i < n; i++) {
            double term = y[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (xInterp - x[j]) / (x[i] - x[j]);
                }
            }
            result += term;
        }
        return result;
    }

    // Calcul des différences divisées pour l'interpolation de Newton
    public static double[] computeDividedDifferences(double[] x, double[] y) {
        int n = x.length;
        double[][] f = new double[n][n];

        // Initialisation avec les valeurs de y
        for (int i = 0; i < n; i++) {
            f[i][0] = y[i];
        }

        // Calcul des différences divisées
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                f[i][j] = (f[i + 1][j - 1] - f[i][j - 1]) / (x[i + j] - x[i]);
            }
        }

        // Extraction des coefficients pour le polynôme de Newton
        double[] coefficients = new double[n];
        for (int i = 0; i < n; i++) {
            coefficients[i] = f[0][i];
        }

        return coefficients;
    }

    // Évaluation du polynôme de Newton
    public static double evaluateNewtonPolynomial(double xInterp, double[] x, double[] coefficients) {
        double result = coefficients[0];
        double term = 1.0;
        for (int i = 1; i < x.length; i++) {
            term *= (xInterp - x[i - 1]);
            result += coefficients[i] * term;
        }
        return result;
    }

    // Méthode des moindres carrés pour un polynôme de degré donné
    public static double[] leastSquares(double[] x, double[] y, int degree) {
        int n = x.length;
        double[][] A = new double[degree + 1][degree + 1];
        double[] B = new double[degree + 1];

        // Construction de la matrice A et du vecteur B
        for (int i = 0; i <= degree; i++) {
            for (int j = 0; j <= degree; j++) {
                for (int k = 0; k < n; k++) {
                    A[i][j] += Math.pow(x[k], i + j);
                }
            }
            for (int k = 0; k < n; k++) {
                B[i] += y[k] * Math.pow(x[k], i);
            }
        }

        // Résolution du système linéaire A * coefficients = B
        return Gauss.gauss(A, B, degree + 1);
    }

    // Évaluation du polynôme des moindres carrés
    public static double evaluateLeastSquares(double xInterp, double[] coefficients) {
        double result = 0.0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(xInterp, i);
        }
        return result;
    }

    // Trier les points selon les coordonnées x
    private static void sortPoints(double[] x, double[] y) {
        int n = x.length;
        double[][] points = new double[n][2];

        for (int i = 0; i < n; i++) {
            points[i][0] = x[i];
            points[i][1] = y[i];
        }

        java.util.Arrays.sort(points, (a, b) -> Double.compare(a[0], b[0]));

        for (int i = 0; i < n; i++) {
            x[i] = points[i][0];
            y[i] = points[i][1];
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}