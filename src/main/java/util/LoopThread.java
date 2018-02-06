package util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 2017/9/8  14:16
 *
 * @author qianrui
 */
public class LoopThread {
    private boolean flag;
    private Future<String> future;
    private long begin;
    private int outTimes;
    private int times;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Callable<String> callable = () -> {
        while (flag) {
            oneHour();
        }
        return "LoopTank正常结束了";
    };

    private static final LoopThread LOOP_THREAD = new LoopThread();

    private LoopThread() {
    }

    public static LoopThread getLoopThread() {
        return getLoopThread(10);
    }

    public static synchronized LoopThread getLoopThread(int outtimes) {
        if (LOOP_THREAD.future == null || LOOP_THREAD.executorService.isShutdown()) {
            LOOP_THREAD.begin = System.currentTimeMillis();
            LOOP_THREAD.outTimes = outtimes;
            LOOP_THREAD.flag = true;
            LOOP_THREAD.clear();
            LOOP_THREAD.executorService = Executors.newSingleThreadExecutor();
            LOOP_THREAD.future = LOOP_THREAD.executorService.submit(LOOP_THREAD.callable);
        }
        return LOOP_THREAD;
    }

    private void oneHour() {
        LoopTanker.loopTankStream(LoopTanker.HOUR_TANK);
        long end = System.currentTimeMillis() + 3600000;
        while (end > System.currentTimeMillis() && flag) {
            tenOfOneSecond();
        }
    }

    private void tenOfOneSecond() {
        try {
            LoopTanker.removeOrder();
            LoopTanker.addOrder();
            LoopTanker.loopTankStream(LoopTanker.SECOND_TANK);
            executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
            if (size() != 0) {
                times = 0;
                return;
            }
            times++;
            if (times >= outTimes) {
                flag = false;
                executorService.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class LoopTanker {
        private Long begin;
        private Long end;
        private int step;
        private Long last;
        private Runnable runnable;
        private TankKey tankKey = new TankKey();
        private static final Set<LoopTanker> SECOND_TANK = new HashSet<>();
        private static final Set<LoopTanker> HOUR_TANK = new HashSet<>();

        private static final Set<LoopTanker> ADD_SECOND_TANK = new HashSet<>();
        private static final Set<LoopTanker> ADD_HOUR_TANK = new HashSet<>();
        private static final Set<LoopTanker> REMOVE_ORDER_TANK = new HashSet<>();

        private LoopTanker(Long end, int step, Runnable runnable) {
            last = begin = System.currentTimeMillis();
            this.end = end;
            this.step = step;
            this.runnable = runnable;
        }

        private static void loopTankStream(Set<LoopTanker> set) {
            long now = System.currentTimeMillis();
            Set<LoopTanker> removetank = new HashSet<>();
            set.stream().filter(a -> (a.end > 0 && a.end < now)).forEach(removetank::add);
            set.removeAll(removetank);

            set.stream().filter(a -> ((a.last + a.step) <= now)).forEach(a -> {
                try {
                    a.runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    REMOVE_ORDER_TANK.add(a);
                }
                a.last = now;
            });

        }

        private static void removeOrder() {
            if (REMOVE_ORDER_TANK.size() != 0) {
                SECOND_TANK.removeAll(REMOVE_ORDER_TANK);
                HOUR_TANK.removeAll(REMOVE_ORDER_TANK);
                REMOVE_ORDER_TANK.clear();
            }
        }

        private static void addOrder() {
            if (ADD_HOUR_TANK.size() != 0) {
                HOUR_TANK.addAll(ADD_HOUR_TANK);
                ADD_HOUR_TANK.clear();
            }
            if (ADD_SECOND_TANK.size() != 0) {
                SECOND_TANK.addAll(ADD_SECOND_TANK);
                ADD_SECOND_TANK.clear();
            }
        }

        private static LoopTanker getLoopTanker(TankKey tankKey) {
            LoopTanker lt = getTanker(tankKey, HOUR_TANK);
            if (lt != null) {
                return lt;
            }
            return getTanker(tankKey, SECOND_TANK);
        }

        private static LoopTanker getTanker(TankKey tankKey, Set<LoopTanker> set) {
            for (LoopTanker loopTanker : set) {
                if (loopTanker.tankKey == tankKey) {
                    return loopTanker;
                }
            }
            return null;
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

    public synchronized boolean removeLoopTank(TankKey tankKey) {
        if (tankKey == null) {
            return false;
        }
        for (LoopTanker loopTanker : LoopTanker.HOUR_TANK) {
            if (loopTanker.tankKey == tankKey) {
                LoopTanker.REMOVE_ORDER_TANK.add(loopTanker);
                return true;
            }
        }
        for (LoopTanker loopTanker : LoopTanker.SECOND_TANK) {
            if (loopTanker.tankKey == tankKey) {
                LoopTanker.REMOVE_ORDER_TANK.add(loopTanker);
                return true;
            }
        }
        return false;
    }

    public TankKey addLoopTankByTenofOneSecond(Runnable runnable, int tenOfOneSecond, long end) {
        return addLoopTank(runnable, 100 * tenOfOneSecond, end, LoopTanker.ADD_SECOND_TANK);
    }

    public TankKey addLoopTankBySec(Runnable runnable, int second, long end) {
        return addLoopTank(runnable, 1000 * second, end, LoopTanker.ADD_SECOND_TANK);
    }

    public TankKey addLoopTankByHour(Runnable runnable, int hour, long end) {
        return addLoopTank(runnable, 3600000 * hour, end, LoopTanker.ADD_HOUR_TANK);
    }

    private synchronized TankKey addLoopTank(Runnable runnable, int loop, long end, Set<LoopTanker> set) {
        if (runnable == null) {
            return null;
        }
        if (loop <= 0) {
            return null;
        }
        if (end > 0 && end < System.currentTimeMillis()) {
            return null;
        }
        LoopTanker tanker = new LoopTanker(end, loop, runnable);
        set.add(tanker);
        return tanker.tankKey;
    }

    public String shutdown() {
        flag = false;
        clear();
        executorService.shutdown();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long runTime() {
        return System.currentTimeMillis() - begin;
    }

    public long runTime(TankKey tankKey) {
        if (tankKey == null) {
            return -1;
        }
        LoopTanker loopTanker = LoopTanker.getLoopTanker(tankKey);
        if (loopTanker == null) {
            return -1;
        }
        return System.currentTimeMillis() - loopTanker.begin;
    }


    public synchronized void clear() {
        LoopTanker.SECOND_TANK.clear();
        LoopTanker.HOUR_TANK.clear();
    }

    public int size() {
        return LoopTanker.HOUR_TANK.size() + LoopTanker.SECOND_TANK.size();
    }

    public static class TankKey {
    }
}
