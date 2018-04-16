package com.qr.core;

import util.Phoenix;

import javax.annotation.Resource;
import java.util.function.Function;

public class RestartCommandImpl implements SystemCmdOutCommand {

    private static RestartCommandImpl restartCommand = new RestartCommandImpl();

    private RestartCommandImpl() {
    }

    public static RestartCommandImpl getRestartCommand() {
        return restartCommand;
    }

    @Resource
    private Function<String, Boolean> caseTrueFalse;

    @Override
    public String getNameSpace() {
        return "restart";
    }

    @Override
    public boolean useCommand(String order) throws Throwable {
        Boolean restart = caseTrueFalse.apply(order);
        if (restart) {
            System.err.println("system will restart in new windows!!!");
            Phoenix.robotRun();
            System.exit(0);
        }
        return restart;
    }
}
