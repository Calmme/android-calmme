package kr.co.calmme

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

internal class MeasureStore {
    private val measurements = CopyOnWriteArrayList<Measurement<Int>>()
    private var minimum = 2147483647
    private var maximum = -2147483648

    /**
     * 최신 N 측정 값은 분석 전에 값을 평활화하기 위해 항상 평균화됩니다.
     *
     * 이 값은 실험이 필요할 수 있습니다. 로컬 범위에 넣는 것보다 클래스 수준에서 더 좋습니다.
     */
    private val rollingAverageSize = 4
    fun add(measurement: Int) {
        val measurementWithDate = Measurement(Date(), measurement)
        measurements.add(measurementWithDate)
        if (measurement < minimum) minimum = measurement
        if (measurement > maximum) maximum = measurement
    }// 여기서 계속 값이 누적

    // 여기서 리스트에 계속 값을 담아서 넘겨줌
    val stdValues: CopyOnWriteArrayList<Measurement<Float>>
        get() {
            val stdValues = CopyOnWriteArrayList<Measurement<Float>>()
            for (i in measurements.indices) {
                var sum = 0
                for (rollingAverageCounter in 0 until rollingAverageSize) {
                    sum += measurements[Math.max(0, i - rollingAverageCounter)].measurement
                }
                val stdValue = Measurement(
                    measurements[i].timestamp,
                    (sum.toFloat() / rollingAverageSize - minimum) / (maximum - minimum)
                )
                // 여기서 계속 값이 누적
                stdValues.add(stdValue)
            }
            return stdValues
        }

    fun  // this parameter can be set at OutputAnalyzer
            getLastStdValues(count: Int): CopyOnWriteArrayList<Measurement<Int>> {
        return if (count < measurements.size) {
            CopyOnWriteArrayList(
                measurements.subList(
                    measurements.size - 1 - count,
                    measurements.size - 1
                )
            )
        } else {
            measurements
        }
    }

    val lastTimestamp: Date
        get() = measurements[measurements.size - 1].timestamp
}