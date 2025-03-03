package com.artemis.interpolation;

public class Gauss {
    public static double[] gauss(double[][] A, double[] B, int n) {
        double[] x = new double[n];

        // Élimination de Gauss
        for (int k = 0; k < n; k++) {
            // Recherche du pivot maximal
            int maxRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(A[i][k]) > Math.abs(A[maxRow][k])) {
                    maxRow = i;
                }
            }

            // Échange des lignes
            double[] tempRow = A[k];
            A[k] = A[maxRow];
            A[maxRow] = tempRow;

            double tempB = B[k];
            B[k] = B[maxRow];
            B[maxRow] = tempB;

            // Vérification du pivot nul
            if (Math.abs(A[k][k]) < 1e-10) {
                throw new ArithmeticException("Matrice singulière ou mal conditionnée.");
            }

            // Élimination
            for (int i = k + 1; i < n; i++) {
                double factor = A[i][k] / A[k][k];
                B[i] -= factor * B[k];
                for (int j = k; j < n; j++) {
                    A[i][j] -= factor * A[k][j];
                }
            }
        }

        // Substitution arrière
        for (int i = n - 1; i >= 0; i--) {
            double sum = B[i];
            for (int j = i + 1; j < n; j++) {
                sum -= A[i][j] * x[j];
            }
            x[i] = sum / A[i][i];
        }

        return x;
    }
}
