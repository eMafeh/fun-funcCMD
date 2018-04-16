package com.qr.asm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassReaderDemo {
    public static void main(String[] args) throws IOException {
        Runnable a = System.out::println;
        FileInputStream inputStream = new FileInputStream("D:\\XqlDownload\\test\\target\\classes\\com\\qr\\order\\FileOutOrderImpl.class");
        byte[] a1 = a(inputStream);
        System.out.println(a1.length);
        ClassReader(a1, 0, a1.length);
    }

    private static byte[] a(InputStream var0) throws IOException {
        if (var0 == null) {
            throw new IOException("Class not found");
        } else {
            byte[] var1 = new byte[var0.available()];
            int var2 = 0;

            while (true) {
                int var3 = var0.read(var1, var2, var1.length - var2);
                byte[] var4;
                if (var3 == -1) {
                    if (var2 < var1.length) {
                        var4 = new byte[var2];
                        System.arraycopy(var1, 0, var4, 0, var2);
                        var1 = var4;
                    }

                    return var1;
                }

                var2 += var3;
                if (var2 == var1.length) {
                    var4 = new byte[var1.length + 1000];
                    System.arraycopy(var1, 0, var4, 0, var2);
                    var1 = var4;
                }
            }
        }
    }

    public static void ClassReader(byte[] var1, int var2, int var3) {
        int[] a = new int[readUnsignedShort(var2 + 8, var1)];
        String[] c;
        int d;
        final int header;
        int var4 = a.length;
        c = new String[var4];
        int var5 = 0;
        int var6 = var2 + 10;

        for (int var7 = 1; var7 < var4; ++var7) {
            a[var7] = var6 + 1;
            byte var8 = var1[var6];
            int var9;
            switch (var8) {
                case 1:
                    var9 = 3 + readUnsignedShort(var6 + 1, var1);
                    if (var9 > var5) {
                        var5 = var9;
                    }
                    break;
                case 2:
                case 7:
                case 8:
                default:
                    var9 = 3;
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    var9 = 5;
                    break;
                case 5:
                case 6:
                    var9 = 9;
                    ++var7;
            }
            var6 += var9;
        }

        d = var5;
        header = var6;
    }

    public static int readUnsignedShort(int var1, byte[] b) {
        return (b[var1] & 255) << 8 | b[var1 + 1] & 255;
    }
}
