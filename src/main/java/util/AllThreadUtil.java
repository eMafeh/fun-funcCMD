package util;


import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author kelaite
 * 2018/2/3
 */
public class AllThreadUtil {
    private static final List<Key> KEYS = new ArrayList<>();
    private static volatile boolean exit = false;

    public static Key whileTrueThread(Callable<Boolean> callable, int sleepTime) {
        Key key = new Key();
        key.isRun = true;
        key.setSleepTime(sleepTime);
        InRunnable runnable = getDieLoopRunnable(callable, key);
        Thread thread = getThread(runnable);
        KEYS.add(key);
        thread.start();
        return key;
    }

    private synchronized static Thread getThread(InRunnable runnable) {
        if (exit) {
            throw new RuntimeException("this class is exit");
        }
        return new Thread(runnable);
    }

    private static InRunnable getDieLoopRunnable(Callable<Boolean> callable, Key key) {
        return () -> {
            while (key.isRun) {
                try {
                    Boolean isSleep = callable.call();
                    //没有返回或者返回睡一会，那就睡
                    if (isSleep == null || isSleep) {
                        Thread.sleep(key.sleepTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void stop(Key key) {
        if (key != null) {
            key.isRun = false;
        }
    }

    public static synchronized void exit() {
        exit = true;
        for (Key key : KEYS) {
            key.isRun = false;
        }
    }

    public static class Key {
        private volatile boolean isRun;
        private volatile int sleepTime;

        public boolean isRun() {
            return isRun;
        }

        public int getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(int sleepTime) {
            this.sleepTime = sleepTime > 0 ? sleepTime : this.sleepTime;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    private interface InRunnable extends Runnable {

    }
}
