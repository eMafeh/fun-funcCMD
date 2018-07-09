package com.qr.core;

import cn.qr.instance.InstanceUtil;
import util.AllThreadUtil;
import cn.qr.cryptozoic.clazz.FindClassUtils;

import java.util.function.Supplier;

/**
 * @author kelaite
 * 2018/2/7
 */
public enum MainOutCommandImpl implements SystemCmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    private static final String TITLE = "yes/no";
    private static final String YES = "yes";

    @Override
    public String getNameSpace() {
        return "";
    }

    @Override
    public void install(Supplier<String> getLine) {
        cmdPrintln("\nfind commands");
        //加载指令
        FindClassUtils.SINGLETON.getClasses().stream().filter(CmdOutCommand.class::isAssignableFrom).forEach(a -> {
            @SuppressWarnings({"unchecked"}) final Class<? extends CmdOutCommand> outOrder = (Class<? extends CmdOutCommand>) a;
            addOutOrder(outOrder);
        });
        cmdPrintln("loaded commands over");
        cmdPrintln("\n欢迎使用集成工具cmd");
    }

    void addOutOrder(Class<? extends CmdOutCommand> outOrder) {
        if (outOrder.isInterface() || outOrder.isAnonymousClass() || outOrder.isLocalClass() || outOrder.isMemberClass()) {
            return;
        }
        CmdOutCommand cmdOutCommand;
        if (outOrder.isEnum()) {
            final CmdOutCommand[] invoke = (CmdOutCommand[]) InstanceUtil.getEnumInstance(outOrder);
            if (invoke.length != 1) {
                throw new RuntimeException(outOrder + " enumType must have only one instance");
            }
            cmdOutCommand = invoke[0];
        } else {
            cmdOutCommand = InstanceUtil.getSingLetonInstance(outOrder);
        }
        addOutOrder(cmdOutCommand);
    }

    void addOutOrder(CmdOutCommand outOrder) {
        final String nameSpace = outOrder.getNameSpace();
        final CmdOutCommand cmdOutCommand = NAMESPACE.get(nameSpace);
        if (cmdOutCommand != null) {
            throw new RuntimeException(cmdOutCommand.getClass() + " | " + outOrder.getClass() + " have same namespace : " + nameSpace);
        }
        if (outOrder instanceof SystemCmdOutCommand) {
            outOrder.setLogLevel("ERROR");
        }
        if (outOrder != INSTANCE) {
            cmdPrintln(addSpacingToLength.get(0).apply(nameSpace, 20) + "load success");
        }
        NAMESPACE.put(nameSpace, outOrder);
    }

    @Override
    public boolean useCommand(String line) throws Throwable {
        //            System.out.print("[cmd@root]$ ");
        String[] commands = maxSplitWords.get(0).apply(line, 2);
        if (commands[0] == null) {
            return true;
        }
        CmdOutCommand cmdOutCommand = NAMESPACE.get(commands[0]);
        if (cmdOutCommand == null) {
            cmdPrintln("- command not found : " + commands[0]);
            return true;
        }
        commands[1] = commands[1] == null ? "" : commands[1];
        boolean isStart = cmdOutCommand.isStart();
        if (isStart) {
            boolean success = cmdOutCommand.useCommand(commands[1]);
            if (!success) {
                cmdPrintln("-" + cmdOutCommand.getNameSpace() + ": warn: command not found : " + commands[1]);
            }

        } else {
            cmdPrintln("-" + cmdOutCommand.getNameSpace() + " is not install");
        }
        return true;
    }

    @Override
    public void shutDown() {
        cmdPrintln("system try exit");
        print("error", () -> TITLE);
        if (!YES.equals(orderLine.get(0).get().trim())) {
            print(() -> "system exit is cancel");
            return;
        }
        NAMESPACE.forEach((a, b) -> {
            if (b != null && !(b instanceof SystemCmdOutCommand)) {
                try {
                    b.shutDown();
                } catch (Throwable exit) {
                    exit.printStackTrace();
                }
            }
        });
        AllThreadUtil.exit();
        cmdPrintln("system is exited");
//        final Package[] packages2 = Package.getPackages();
//        System.out.println(packages2.length);
//        Arrays.stream(packages2).filter(a -> !a.getName().startsWith("java") && !a.getName().startsWith("sun")).forEach(System.out::println);
        System.exit(0);
    }


    @Override
    public String getDescription() {
        StringBuilder result = new StringBuilder("QianRui Cmd\nis a test fun work and it support some order\n");
        result.append("\ninput '").append(HelpOutCommandImpl.INSTANCE.getNameSpace()).append(" [order]'\nto show how to use this order\n");
        result.append("\nnow have order list is : \n");
        NAMESPACE.forEach((a, b) -> result.append(addSpacingToLength.get(0).apply(a, 20)).append("by(").append(b.getClass()).append(")\n"));
        result.append("\nif you have some awesome ideas or codes or you just want to join this work\n");
        result.append("please contact me!!!\n>>>\temail 1135901259@qq.com\n>>>\tphoneNumber 18715600499\n");
        return result.toString();
    }
}
