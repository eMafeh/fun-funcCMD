package test;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class VMtest {
    public static void main(String[] args) {
        init();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String s = sc.nextLine();
            System.out.println(AgentMain.fullSizeOf(s));
        }
    }

    public static void init() {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();

        list.forEach(vm -> {
            try {
                VirtualMachine.attach(vm).loadAgent("D:\\XqlDownload\\test\\out\\artifacts\\vmAgent\\vmAgent.jar");
            } catch (AgentLoadException | AgentInitializationException | AttachNotSupportedException | IOException e) {
                e.printStackTrace();
            }
            System.out.println(vm);
        });
        System.out.println(AgentMain.inst);
    }
}
