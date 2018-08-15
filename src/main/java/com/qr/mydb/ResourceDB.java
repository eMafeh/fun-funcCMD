package com.qr.mydb;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qr.core.CmdOutCommand;
import util.StringValueUtil;

import javax.annotation.Resource;
import java.io.*;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.awt.event.KeyEvent.VK_SPACE;

public enum ResourceDB implements CmdOutCommand {
    /**
     * 全局唯一实例
     */
    INSTANCE;
    @Resource
    private Consumer<Integer> chickOneKey;
    private File json = new File("C:\\Users\\QianRui\\Desktop\\resource.json");
    private TreeMap<String, String> tree;
    private boolean autosave = true;

    @Override
    public String getDescription() {
        return "key value 数据库";
    }

    @Override
    public String getNameSpace() {
        return "db";
    }

    @Override
    public boolean useCommand(final String order) throws Throwable {
        String[] apply = maxSplitWords.get(0).apply(order, 2);
        if (apply[0] == null) return false;
        switch (apply[0]) {
            case "show":
                show();
                break;
            case "add":
                add(apply[1]);
                break;
            case "save":
                save();
                break;
            case "auto":
                autoSave(caseTrueFalse.get(0).apply(apply[1]));
                break;
            case "reload":
                install();
                break;
            case "find":
                find(apply[1]);
                break;
            case "remove":
                remove(apply[1]);
                break;
            default:
                return false;
        }
        return true;
    }

    private void autoSave(final boolean autosave) {
        this.autosave = autosave;
    }

    @Override
    public boolean isStart() {
        return tree != null;
    }

    @Override
    public void install(final Supplier<String> getLine) throws Throwable {
        install();
    }

    private void install() throws IOException {
        try (FileReader fileReader = new FileReader(json); JSONReader js = new JSONReader(fileReader)) {
            tree = js.readObject(TreeMap.class);
            print(() -> "加载成功");
        }
    }

    @Override
    public void shutDown() {
        tree = null;
    }

    private void remove(String key) throws IOException {
        String remove = tree.remove(getOrInput(key, () -> print(() -> "请输入描述")));

        if (remove == null) {
            print(() -> "该记录不存在");
            return;
        }
        if (autosave) {
            save();
        }
    }

    private void add(String key) throws IOException {
        key = getOrInput(key, () -> {
            print(() -> "请输入网址");
            chickOneKey.accept(VK_SPACE);
        }).trim();
        String ov = tree.get(key);
        if (ov != null) {
            print(() -> "该网址已存在,是否覆盖描述");
            if (!caseTrueFalse.get(0).apply(orderLine.get(0).get())) {
                print(() -> "已取消");
                return;
            }
        }
        print(() -> "请输入描述");
        String value = orderLine.get(0).get().replaceAll("\r|\n", "");
        tree.put(key, value);
        if (autosave) {
            save();
        }
    }

    private void find(String key) {
        int l = maxURL();
        String mac = getOrInput(key, () -> print(() -> "请输入描述")).toUpperCase();
        tree.forEach((a, b) -> {
            if (a.toUpperCase().contains(mac) || b.toUpperCase().contains(mac)) {
                print(() -> StringValueUtil.addSpacingToLength(a, l) + b);
            }
        });
    }

    private void save() throws IOException {
        try (JSONWriter writer = new JSONWriter(new FileWriter(json))) {
            writer.config(SerializerFeature.PrettyFormat, true);
            writer.writeObject(tree);
            print(() -> "保存成功");
        }
    }

    private void show() {
        int l = maxURL();
        tree.forEach((a, b) -> print(() -> StringValueUtil.addSpacingToLength(a, l) + b));
        print(() -> "size is " + tree.size());
    }

    private int maxURL() {
        return tree.keySet().stream().max(Comparator.comparingInt(String::length)).get().length() + 10;
    }

    private String getOrInput(String s, Runnable runnable) {
        if (s == null) {
            runnable.run();
            return orderLine.get(0).get();
        }
        return s;
    }
}
