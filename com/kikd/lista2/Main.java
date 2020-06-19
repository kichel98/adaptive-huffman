/*
 * author: Piotr Andrzejewski
 * Zadanie na 4 - Dynamiczne Kodowanie Huffmana
 */
package com.kikd.lista2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3 || !args[0].equals("--encode") && !args[0].equals("--decode")) {
            System.out.println("usage: --encode/--decode file.in file.out");
            return;
        }

        Path inputPath = Paths.get(args[1]);
        Path outputPath = Paths.get(args[2]);

        VitterAlgorithm alg = new VitterAlgorithm();
        try {
            if (args[0].equals("--encode")) {
                byte[] input = Files.readAllBytes(inputPath);
                StringBuilder output = alg.encode(input);
                ByteStringUtils.padStringWithZerosToFullBytes(output);
                byte[] outputBytes = ByteStringUtils.getBytesByString(output);
                Files.write(outputPath, outputBytes);

                prepareAndPrintStats(input, outputBytes, alg.getTree());
            } else {
                byte[] inputBytes = Files.readAllBytes(inputPath);
                byte[] output = alg.decode(ByteStringUtils.getStringByBytes(inputBytes));
                Files.write(outputPath, output);
            }
        } catch (IOException e) {
            System.out.println("Unable to read/write file");
        }
    }

    private static void prepareAndPrintStats(byte[] input, byte[] outputBytes, Tree tree) {
        Map<Byte, Integer> frequencyMap = StatsUtils.getFrequencyMap(input);
        System.out.println("Entropia: " + StatsUtils.calcEntropy(frequencyMap, input.length));
        System.out.println("Stopien kompresji: " + (double) input.length / outputBytes.length);
        System.out.println("Srednia dlugosc kodowania: " + StatsUtils.calcAvgCodeLength(frequencyMap, input.length, tree));
    }

}
