package com.kikd.lista2;

public class ByteStringUtils {
    /*
            Copied from StackOverflow ;)
     */
    static byte[] getBytesByString(StringBuilder text) {
        int splitSize = 8;

        int index = 0;
        int position = 0;

        byte[] resultByteArray = new byte[text.length()/splitSize];

        while (index < text.length()) {
            String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
            int byteAsInt = Integer.parseInt(binaryStringChunk, 2);
            resultByteArray[position] = (byte) byteAsInt;
            index += splitSize;
            position++;
        }
        return resultByteArray;
    }

    static String getStringByBytes(byte[] bytes) {
        StringBuilder message = new StringBuilder();
        byte infoByte = bytes[0];
        for (int i = 1; i < bytes.length - 1; i++) {
            String byteString = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
            message.append(byteString);
        }
        String lastByte = Integer.toBinaryString((bytes[bytes.length - 1] & 0xFF) + 0x100).substring(1);
        for (byte i = 0; i < infoByte; i++) {
            message.append(lastByte.charAt(i));
        }
        return message.toString();
    }

    static void padStringWithZerosToFullBytes(StringBuilder output) {
        int validBitsInLastByte = output.length() % 8;

        // infoByte contains info how many bits in last byte are valid (non-padded)
        String infoByte = String.format("%8s", Integer.toBinaryString(validBitsInLastByte))
                .replace(' ', '0');

        // add infoByte at front in optimal way
        output.reverse()
                .append(new StringBuilder(infoByte).reverse())
                .reverse();

        while (output.length() % 8 != 0) {
            output.append('0');
        }
    }
}
