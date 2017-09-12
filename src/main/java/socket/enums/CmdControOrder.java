package socket.enums;


import java.util.function.Function;

/**
 * Created by snb on 2017/9/12  10:24
 */
public interface CmdControOrder {

    Function<String, String> getFunction();
}
