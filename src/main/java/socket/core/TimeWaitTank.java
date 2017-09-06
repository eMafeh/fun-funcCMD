package socket.core;


/**
 * Created by snb on 2017/9/6  15:23
 */
public class TimeWaitTank {
    public static void tank(Runnable function, Flag flag,int sleeptime) {
        while (flag.flag) {
            function.run();
            try {
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
