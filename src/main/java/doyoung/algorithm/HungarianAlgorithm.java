package doyoung.algorithm;

import java.util.Arrays;

public class HungarianAlgorithm implements MinimumSumCalculator {

    private static final long VERY_LARGER_NUMBER = Long.MAX_VALUE;
    private static final int NOT_SELECT = -1;

    private int k;
    private int[] minSlackXtY;
    private int[] matchYtX, matchXtY;
    private int[] parentXByCommittedY;

    private long[][] squareMatrix;
    private long[] labelY, labelX;
    private long[] minSlackXtY_Value;
    private long[][] originSquareMatrix;

    private boolean[] committedYs;

    // labelX : 해당 행에서 가장 작은 수
    private void computeInitialFeasibleSolution() {
        for (int x = 0; x < k; x++) {
            labelX[x] = VERY_LARGER_NUMBER;
        }
        for (int y = 0; y < k; y++) {
            for (int x = 0; x < k; x++) {
                if (squareMatrix[y][x] < labelX[x]) {
                    labelX[x] = squareMatrix[y][x];
                }
            }
        }
    }

    @Override
    public long calculate(long[][] inputMatrix) {
        init(inputMatrix);

        // 비어있는 테이블은 계산할 필요 없음.
        if (k == 0) {
            return 0;
        }

        /* step1 행렬의 각 행과 열에서 최솟값으로 각각 뺀다. */
        reduce();
        computeInitialFeasibleSolution();
        greedyMatch();
        int y = fetchUnmatchedY();

        /* step2 최소라인으로 0의 성분을 가진 행과 열을 지워 K개인지 확인한다. */
        while (y < k) {
            initializeStep(y);
            executeStep();
            y = fetchUnmatchedY();
        }

        long sum = 0;
        for (int i = 0; i < k; i++) {
            sum += originSquareMatrix[i][matchYtX[i]];
        }

        return sum;
    }

    private void init(long[][] inputMatrix) {
        if (inputMatrix.length > 0 && inputMatrix.length != inputMatrix[0].length) {
            throw new IllegalArgumentException("HungarianAlgorithm 은 정방행렬만 계산 가능합니다.");
        }
        k = inputMatrix.length;

        squareMatrix = new long[k][k];
        originSquareMatrix = new long[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                squareMatrix[i][j] = inputMatrix[i][j];
                originSquareMatrix[i][j] = inputMatrix[i][j];
            }
        }

        labelY = new long[k];
        labelX = new long[k];
        minSlackXtY = new int[k];
        minSlackXtY_Value = new long[k];
        committedYs = new boolean[k];
        parentXByCommittedY = new int[k];
        matchYtX = new int[k];
        matchXtY = new int[k];
        Arrays.fill(matchYtX, NOT_SELECT);
        Arrays.fill(matchXtY, NOT_SELECT);
    }

    private void executeStep() {
        while (true) {
            int minSlackY = NOT_SELECT, minSlackX = NOT_SELECT;
            long minSlackY_Value = VERY_LARGER_NUMBER;
            // 슬랙 중에서 가장 작은 수를 찾는다.
            for (int x = 0; x < k; x++) {
                if (parentXByCommittedY[x] == NOT_SELECT) {
                    if (minSlackXtY_Value[x] < minSlackY_Value) {
                        minSlackY_Value = minSlackXtY_Value[x];
                        minSlackY = minSlackXtY[x];
                        minSlackX = x;
                    }
                }
            }
            if (minSlackY_Value > 0) {
                // 특정 x가 선택되었는데, 이는 이미 다른 누군가가 선택했음.
                updateLabeling(minSlackY_Value);
            }
            parentXByCommittedY[minSlackX] = minSlackY;
            if (matchXtY[minSlackX] == NOT_SELECT) {
                // 새로 연결되는 패스를 찾음.
                int committedX = minSlackX;
                int parentX = parentXByCommittedY[committedX];
                while (true) {
                    int temp = matchYtX[parentX];
                    match(parentX, committedX);
                    committedX = temp;
                    if (committedX == NOT_SELECT) {
                        break;
                    }
                    parentX = parentXByCommittedY[committedX];
                }
                return;
            } else {
                // 이미 선택된 Y를 찾음. 해당 패스를 선택하고 이미 선택되어있던 X는 다른 패스를 찾는다.
                int targetY = matchXtY[minSlackX];
                committedYs[targetY] = true;
                for (int x = 0; x < k; x++) {
                    if (parentXByCommittedY[x] == NOT_SELECT) {
                        long slack = squareMatrix[targetY][x] - labelY[targetY] - labelX[x]; //
                        if (minSlackXtY_Value[x] > slack) {
                            minSlackXtY_Value[x] = slack;
                            minSlackXtY[x] = targetY;
                        }
                    }
                }
            }
        }
    }

    private int fetchUnmatchedY() {
        for (int y = 0; y < k; y++) {
            if (matchYtX[y] == NOT_SELECT) {
                return y;
            }
        }
        return k;
    }

    private void greedyMatch() {
        for (int y = 0; y < k; y++) {
            for (int x = 0; x < k; x++) {
                // (y,x) 에 이미 선택된 열과 행이 없고 0이라면 선택.
                if (matchYtX[y] == NOT_SELECT && matchXtY[x] == NOT_SELECT
                    && squareMatrix[y][x] - labelY[y] - labelX[x] == 0) {
                    match(y, x);
                }
            }
        }
    }

    private void initializeStep(int y) {
        Arrays.fill(committedYs, false); // 선택된 y집합
        Arrays.fill(parentXByCommittedY, NOT_SELECT); // 선택된 y의 x 값
        committedYs[y] = true;
        for (int x = 0; x < k; x++) {
            // 특정 x에서 y와 연결되는 간선비용을 미리 계산
            minSlackXtY_Value[x] = squareMatrix[y][x] - labelY[y] - labelX[x];
            minSlackXtY[x] = y;
        }
    }

    private void match(int y, int x) {
        matchYtX[y] = x;
        matchXtY[x] = y;
    }

    private void reduce() {
        // 각 행에서 가장 작은 수로 행 빼기
        for (int y = 0; y < k; y++) {
            long min = VERY_LARGER_NUMBER;
            for (int x = 0; x < k; x++) {
                if (squareMatrix[y][x] < min) {
                    min = squareMatrix[y][x];
                }
            }
            for (int j = 0; j < k; j++) {
                squareMatrix[y][j] -= min;
            }
        }

        // 각 열에서 가장 작은 수로 모든 열 빼기
        long[] min = new long[k];
        for (int x = 0; x < k; x++) {
            min[x] = VERY_LARGER_NUMBER;
        }
        for (int y = 0; y < k; y++) {
            for (int x = 0; x < k; x++) {
                if (squareMatrix[y][x] < min[x]) {
                    min[x] = squareMatrix[y][x];
                }
            }
        }
        for (int y = 0; y < k; y++) {
            for (int x = 0; x < k; x++) {
                squareMatrix[y][x] -= min[x];
            }
        }
    }

    private void updateLabeling(long slack) {
        for (int w = 0; w < k; w++) {
            if (committedYs[w]) {
                labelY[w] += slack;
            }
        }
        for (int j = 0; j < k; j++) {
            if (parentXByCommittedY[j] != NOT_SELECT) {
                labelX[j] -= slack;
            } else {
                minSlackXtY_Value[j] -= slack;
            }
        }
    }
}
