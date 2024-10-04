import java.util.Random;

public class MultithreadedMatrixMultiplication {

    private static final int SIZE = 20;  // Matrix size (20x20)

    // Method to create a random matrix
    public static int[][] generateMatrix(int size) {
        Random random = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextInt(10);  // Random values between 0 and 9
            }
        }
        return matrix;
    }

    // Method for single-threaded matrix multiplication (for validation)
    public static int[][] singleThreadedMultiply(int[][] matrixA, int[][] matrixB) {
        int[][] result = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                result[i][j] = 0;
                for (int k = 0; k < SIZE; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] matrixA = generateMatrix(SIZE);
        int[][] matrixB = generateMatrix(SIZE);
        int[][] resultMultiThreaded = new int[SIZE][SIZE];
        int[][] resultSingleThreaded;

        // Create 5 threads for matrix multiplication
        Thread[] threads = new Thread[5];
        int rowsPerThread = SIZE / 5;

        for (int i = 0; i < 5; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == 4) ? SIZE : startRow + rowsPerThread;
            threads[i] = new Thread(new MatrixMultiplier(matrixA, matrixB, resultMultiThreaded, startRow, endRow));
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Single-threaded multiplication for verification
        resultSingleThreaded = singleThreadedMultiply(matrixA, matrixB);

        // Verify the results
        boolean isCorrect = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (resultMultiThreaded[i][j] != resultSingleThreaded[i][j]) {
                    isCorrect = false;
                    break;
                }
            }
        }

        if (isCorrect) {
            System.out.println("Multithreaded multiplication is correct!");
        } else {
            System.out.println("There is a mismatch in the results.");
        }
    }
}
