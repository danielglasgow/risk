package risk;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ExpectedValue {
	private Map<Point, Double> expectedValueTable = new HashMap<Point, Double>();

	public ExpectedValue() {
		buildTable();
	}

	private void buildTable() {
		// Generate better starting values
		expectedValueTable.put(new Point(5, 1), 4.47);
		expectedValueTable.put(new Point(4, 1), 3.43);
		expectedValueTable.put(new Point(3, 1), 2.33);
		expectedValueTable.put(new Point(2, 1), 1.42);
		buildValues();
		System.out.println(this);
	}

	private void buildValues() {
		// fix first row below (n vs 1)
		// n vs 1
		for (int i = 6; i < 100; i++) {
			expectedValueTable.put(new Point(i, 1), (double) i - 0.5);
		}
		// 1 vs n && n vs 0
		for (int i = 1; i < 100; i++) {
			expectedValueTable.put(new Point(i, 0), (double) i);
			expectedValueTable.put(new Point(1, i), 1.0);
		}
		// get dice percentages more exact
		// 2 vs n
		for (int i = 2; i < 100; i++) {
			double expectedValue = 0.256
					* expectedValueTable.get(new Point(2, (i - 1))) + 0.744
					* expectedValueTable.get(new Point(1, i));
			expectedValueTable.put(new Point(2, i), expectedValue);
		}
		// 3 vs n
		for (int i = 2; i < 100; i++) {
			double expectedValue = 0.228
					* expectedValueTable.get(new Point(3, (i - 2))) + 0.448
					* expectedValueTable.get(new Point(1, i)) + 0.324
					* expectedValueTable.get(new Point(2, i - 1));
			expectedValueTable.put(new Point(3, i), expectedValue);
		}

		// 4 and above vs n
		for (int j = 4; j < 100; j++) {
			for (int i = 2; i < 100; i++) {
				double expectedValue = 0.372
						* expectedValueTable.get(new Point(j, (i - 2))) + 0.292
						* expectedValueTable.get(new Point(j - 2, i)) + 0.336
						* expectedValueTable.get(new Point(j - 1, i - 1));
				expectedValueTable.put(new Point(j, i), expectedValue);
			}
		}
	}

	public String toString() {
		String string = "";
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0) {
					if (j != 0) {
						if (j == 9) {
							System.out.println("      " + j);
						} else {
							System.out.print("      " + j);
						}
					}
				} else {
					if (j == 0) {
						System.out.println("");
						System.out.print(i);
					} else {
						String value = "";
						value += expectedValueTable.get(new Point(i, j));
						String newValue = value + " ";
						if (value.length() > 3) {
							newValue = value.substring(0, 4);
						}
						if (j == 9) {
							System.out.println("   " + newValue);
						} else {
							System.out.print("   " + newValue);
						}
					}
				}
			}
		}
		return string;
	}

	public Map<Point, Double> getExpectedValueTable() {
		return this.expectedValueTable;
	}

}
