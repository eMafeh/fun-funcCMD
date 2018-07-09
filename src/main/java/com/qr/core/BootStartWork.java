package com.qr.core;

import cn.qr.cryptozoic.clazz.FindClassUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @author kelaite
 * 2018/2/8
 */
public class BootStartWork {
    public static void main(String[] args) throws IOException {
        final Set<Class<?>> classes = FindClassUtils.SINGLETON.getClasses();
        System.out.println(classes);
    }
}
