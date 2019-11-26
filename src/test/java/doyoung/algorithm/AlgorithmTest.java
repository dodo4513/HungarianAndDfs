package doyoung.algorithm;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmTest {

    private MinimumSumCalculator dfsAlgorithm;
    private MinimumSumCalculator hungarianAlgorithm;

    @BeforeEach
    void init() {
        dfsAlgorithm = new DfsAlgorithm();
        hungarianAlgorithm = new HungarianAlgorithm();
    }

    @Test
    void crossVerifyDfsAndHungarian() {
        long[][] cost = {//
            {5, 7, 9},//
            {14, 15, 16},//
            {15, 18, 21}};

        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {7, 4, 3},//
            {3, 1, 3},//
            {4, 0, 0}};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {1000, 600},//
            {700, 0}};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {500, 0, 0},//
            {1000, 500, 0},//
            {0, 500, 1000}};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {0, 10, 0},//
            {50, 0, 1},//
            {50, 10, 0}};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {108, 125, 150},//
            {150, 135, 175},//
            {122, 148, 250}};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {1, 4, 5},//
            {5, 7, 6},//
            {5, 8, 8}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {1, 0, 4},//
            {6, 8, 0},//
            {0, 6, 1}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {1, 1, 1},//
            {1, 1, 1},//
            {1, 1, 1}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{};
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {3000, 6000, 0},//
            {0, 3000, 3000},//
            {0, 0, 3000}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {3000, 1500, 1000},//
            {1500, 0, 22},//
            {0, 0, 100}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = new long[][]{//
            {108, 125, 150, 123123, 29123, 1992},//
            {150, 135, 175, 22132, 5555, 123},//
            {122, 148, 250, 123123, 34, 1010},//
            {123123, 0, 0, 0, 0, 0},//
            {0, 534, 213, 0, 0, 0},//
            {0, 1111, 524, 1, 1, 1}//
        };
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = generateArray(10);
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));

        cost = generateArray(5);
        assertEquals(dfsAlgorithm.calculate(cost), hungarianAlgorithm.calculate(cost));
    }

    @RepeatedTest(5)
    void testDfsPerformance() {
        int[] track = {7, 8, 9, 10, 11};
        calculateAndPrint(track, dfsAlgorithm);
    }

    @RepeatedTest(5)
    void testHungarianPerformance() {
        int[] track = {10, 100, 1_000, 2_000};
        calculateAndPrint(track, hungarianAlgorithm);
    }

    private void calculateAndPrint(int[] track, MinimumSumCalculator minimumSumCalculator) {
        for (int i = 0; i < track.length; i++) {
            long[][] cost = generateArray(track[i]);
            StopWatch stopWatch = StopWatch.createStarted();
            minimumSumCalculator.calculate(cost);

            System.out.print("#k:" + track[i] + "  " + stopWatch.getTime(TimeUnit.MILLISECONDS) + "ms       ");
        }
        System.out.println();
    }

    static long[][] generateArray(int size) {

        long[][] squareMatrix = new long[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squareMatrix[i][j] = (int)(Math.random() * (10_00 + 1)) * 100; // 100 ~ 100000
            }
        }

        return squareMatrix;
    }
}
