package com.digicorechallenge;

public class PerfectSquareCount {

	public static void main(String[] args) {
		int[][] arrays = { { 1, 2 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 5 } };
		perfectSquares(arrays);
	}

	public static void perfectSquares(int[][] arrays) {
		if (arrays.length <= 0) {
			System.out.println("empty array!!");
		}

		for (int i = 0; i < arrays.length; i++) {
			int[] array = arrays[i];
			int m = array[0];
			int n = array[1];
			printNumberOfSquares(m, n);
		}

	}

	private static void printNumberOfSquares(int m, int n) {
		if (m == n) {
			int perfectSquare = n * (n + 1) * ((2 * n) + 1) / 6;
			System.out.println(perfectSquare);
		} else {
			if (n < m) {
				int temp = m;
				m = n;
				n = temp;
			}
			int perfectSquare = n * (n + 1) * (3 * m - n + 1) / 6;
			System.out.println(perfectSquare);
		}
	}

}
