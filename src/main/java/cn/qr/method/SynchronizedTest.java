package cn.qr.method;

/**
 * @author QianRui
 * 2018/12/27
 */
public class SynchronizedTest {
    private static class Lock {
        @Override public int hashCode() { return super.hashCode(); }
    }

    private final static Lock lock = new Lock();
    private static int counter = 0;

    public static void main(String[] args) {
        lock.hashCode();
        System.identityHashCode(lock);
        for (int i = 0; i < 1_000_000; i++) {
            foo();
        }
    }

    private static void foo() {
        synchronized (lock) {
            counter++;
        }
    }

}
/*

java  -XX:+UnlockDiagnosticVMOptions -XX:+PrintBiasedLockingStatistics -XX:TieredStopAtLevel=1 -XX:+UseBiasedLocking cn.qr.method.SynchronizedTest
*/