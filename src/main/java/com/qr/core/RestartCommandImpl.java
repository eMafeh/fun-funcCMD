package com.qr.core;

import com.qr.phoenix.Phoenix;

public class RestartCommandImpl implements SystemCmdOutCommand {

    private static RestartCommandImpl restartCommand = new RestartCommandImpl();

    private RestartCommandImpl() {
    }

    public static RestartCommandImpl getRestartCommand() {
        return restartCommand;
    }


    @Override
    public String getNameSpace() {
        return "restart";
    }

    @Override
    public boolean useCommand(String order) {
        System.err.println("close this and open a new windows to restart?");
        Boolean restart = caseTrueFalse.get(0).apply(orderLine.get(0).get());
        if (restart) {
            System.err.println("system will restart in new windows!!! do not touch keyboard any more!!!");
            try {
                Phoenix.robotRun(order, orderLine.get(0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        return restart;
    }
}
