package socket.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static java.lang.Integer.*;

/**
 * Created by snb on 2017/9/7  19:33
 */
public class UnUsePort {
    private static final Runtime runtime = Runtime.getRuntime();

    public static void main(String[] args) throws IOException {
        System.out.println(getPort());
        System.out.println(unUsePort(4000));
    }

    public static Set<Integer> getPort() {
        Set<Integer> ports = new HashSet<>();
        getPort(a -> {
            ports.add(parseInt(a));
            return true;
        });
        return ports;
    }


    public static boolean unUsePort(int port) {
        String s = port + "";
        return getPort(a -> !a.equals(s));
    }

    private static boolean getPort(Function<String, Boolean> function) {
        Process windows = null;
        try {
            windows = runtime.exec("netstat -ano");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line;
        String[] split;
        int i = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(windows.getInputStream(), "GBK"))) {
            while ((line = bufferedReader.readLine()) != null) {
                if (i++ < 3) continue;
                if ((split = line.split("\\s+")).length < 3) continue;
                if ((split = split[2].split(":")).length > 1) {
                    if (!function.apply(split[split.length - 1])) return false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
