package util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipUtil {
    private static final BASE64Decoder decoder = new BASE64Decoder();
    private static final BASE64Encoder encoder = new BASE64Encoder();

    public static String zip(String input, int level) {
        byte[] bytes = input.getBytes();
        byte[] zip = zip(bytes, level);
        return encoder(zip);
    }

    public static String unzip(String input) {
        byte[] decoder = decoder(input);
        byte[] unzip = unzip(decoder);
        return new String(unzip);
    }

    public static byte[] zip(byte[] input, int level) {
        Deflater deflater = new Deflater(level);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            deflater.setInput(input);
            deflater.finish();
            byte[] bytes = new byte[2048];
            while (!deflater.finished()) {
                int i = deflater.deflate(bytes);
                outputStream.write(bytes, 0, i);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            deflater.end();
        }
    }

    public static byte[] unzip(byte[] input) {
        Inflater inflater = new Inflater();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            inflater.setInput(input);
            byte[] bytes = new byte[2048];
            while (!inflater.finished()) {
                int i = inflater.inflate(bytes);
                outputStream.write(bytes, 0, i);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            inflater.end();
        }
    }

    public static byte[] decoder(String base64) {
        try {
            return decoder.decodeBuffer(base64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encoder(byte[] input) {
        return encoder.encode(input)
                .replaceAll("[\\s*\t\n\r]", "");
    }
}
