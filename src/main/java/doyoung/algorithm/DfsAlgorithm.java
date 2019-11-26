package doyoung.algorithm;

public class DfsAlgorithm implements MinimumSumCalculator {

    private long minimumDiscountPrice;
    private long[][] squareMatrix;
    private boolean[] couponUsedHistory;

    private void Dfs(final int productIndex, final long totalDiscountPrice) {
        if (productIndex == squareMatrix.length) {
            minimumDiscountPrice = Math.min(minimumDiscountPrice, totalDiscountPrice);
        }

        for (int i = 0; i < squareMatrix.length; i++) {
            if (!couponUsedHistory[i]) {
                couponUsedHistory[i] = true;
                Dfs(productIndex + 1, totalDiscountPrice + squareMatrix[productIndex][i]);
                couponUsedHistory[i] = false;
            }
        }
    }

    @Override
    public long calculate(long[][] inputMatrix) {
        minimumDiscountPrice = Long.MAX_VALUE;
        couponUsedHistory = new boolean[inputMatrix.length];
        squareMatrix = inputMatrix;

        Dfs(0, 0);

        return minimumDiscountPrice;
    }
}
