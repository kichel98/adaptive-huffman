package com.kikd.lista2;

import java.util.HashMap;
import java.util.Map;

public class StatsUtils {
    static double calcAvgCodeLength(Map<Byte, Integer> frequencyMap, int length, Tree tree) {
        final double[] avgCodeLength = {0}; // workaround, otherwise assignment inside lambda wouldn't be allowed
        frequencyMap.forEach((sym, freq) -> {
            avgCodeLength[0] += freq * tree.getPathCodeForSymbol(sym).length();
        });
        return avgCodeLength[0] / length;
    }

    static double calcEntropy(Map<Byte, Integer> frequencyMap, int length) {
        double entropy = 0;

        for (Integer frequency : frequencyMap.values()) {
            entropy += frequency * (Math.log(frequency) / Math.log(2));
        }
        return (Math.log(length) / Math.log(2)) - (1.0/length) * entropy;
    }

    static Map<Byte, Integer> getFrequencyMap(byte[] bytes) {
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte singleByte : bytes)  {
            frequencyMap.merge(singleByte, 1, Integer::sum);
        }
        return frequencyMap;
    }
}
