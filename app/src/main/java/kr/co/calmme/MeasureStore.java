package kr.co.calmme;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

class MeasureStore {
    private final CopyOnWriteArrayList<Measurement<Integer>> measurements = new CopyOnWriteArrayList<>();
    private int minimum = 2147483647;
    private int maximum = -2147483648;

    /**
     * 최신 N 측정 값은 분석 전에 값을 평활화하기 위해 항상 평균화됩니다.
     *
     * 이 값은 실험이 필요할 수 있습니다. 로컬 범위에 넣는 것보다 클래스 수준에서 더 좋습니다.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int rollingAverageSize = 4;

    void add(int measurement) {
        Measurement<Integer> measurementWithDate = new Measurement<>(new Date(), measurement);

        measurements.add(measurementWithDate);
        if (measurement < minimum) minimum = measurement;
        if (measurement > maximum) maximum = measurement;
    }

    // 여기서 리스트에 계속 값을 담아서 넘겨줌
    CopyOnWriteArrayList<Measurement<Float>> getStdValues() {
        CopyOnWriteArrayList<Measurement<Float>> stdValues = new CopyOnWriteArrayList<>();

        for (int i = 0; i < measurements.size(); i++) {
            int sum = 0;
            for (int rollingAverageCounter = 0; rollingAverageCounter < rollingAverageSize; rollingAverageCounter++) {
                sum += measurements.get(Math.max(0, i - rollingAverageCounter)).measurement;
            }

            Measurement<Float> stdValue =
                    new Measurement<>(
                            measurements.get(i).timestamp,
                            ((float)sum / rollingAverageSize - minimum ) / (maximum - minimum));
            // 여기서 계속 값이 누적
            stdValues.add(stdValue);
        }

        return stdValues;
    }

    @SuppressWarnings("SameParameterValue") // this parameter can be set at OutputAnalyzer
    CopyOnWriteArrayList<Measurement<Integer>> getLastStdValues(int count) {
        if (count < measurements.size()) {
            return  new CopyOnWriteArrayList<>(measurements.subList(measurements.size() - 1 - count, measurements.size() - 1));
        } else {
            return measurements;
        }
    }

    Date getLastTimestamp() {
        return measurements.get(measurements.size() - 1).timestamp;
    }
}
