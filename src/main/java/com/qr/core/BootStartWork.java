package com.qr.core;

import util.FindClassUtils;

import java.util.Set;

/**
 * @author kelaite
 * 2018/2/8
 */
public class BootStartWork {
    public static void main(String[] args) {
        final Set<Class<?>> classes = FindClassUtils.getClasses("");
        System.out.println(classes);
    }
}
