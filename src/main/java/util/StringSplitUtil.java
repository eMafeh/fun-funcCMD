package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kelaite
 * 2018/2/7
 */
public class StringSplitUtil {
    private static final int NOT_WORD = -1;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(split("123456", 6)));
    }

    /**
     * 将字符串切割为两份，整体不返回null，元素也不会为null
     *
     * @param line  被切割的字符串
     * @param index 第二个字符串的首字符下标
     * @return 两个元素的数组
     */
    public static String[] split(String line, int index) {
        String[] strings = new String[2];
        if (line == null) {
            return strings;
        }
        if (index <= 0) {
            strings[0] = "";
            strings[1] = line;
            return strings;
        }
        char[] chars = line.toCharArray();
        int length = chars.length;
        if (index >= length - 1) {
            strings[0] = line;
            strings[1] = "";
            return strings;
        }
        strings[0] = new String(chars, 0, index);
        strings[1] = new String(chars, index, length - index);
        return strings;
    }

    public static String nextWord(String line, int begin) {
        char[] chars = line.toCharArray();
        int length = chars.length;
        if (begin > length) {
            return "";
        }
        int start = NOT_WORD;
        for (int i = begin < 0 ? 0 : begin; i < length; i++) {
            char c = chars[i];
            if (c == ' ') {
                if (start == NOT_WORD) {
                    continue;
                } else {
                    return new String(chars, start, i - start);
                }
            }
            if (start == NOT_WORD) {
                start = i;
            }
        }
        if (start == NOT_WORD) {
            return "";
        }
        return new String(chars, start, length - start);
    }

    public static List<String> allWords(String line) {
        List<String> words = new ArrayList<>();
        char[] chars = line.toCharArray();
        int length = chars.length;
        //-1 indicate now is not word
        int wordBegin = NOT_WORD;
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            if (c == ' ') {
                if (wordBegin != NOT_WORD) {
                    //word is end ,cache word,really to find next word
                    words.add(new String(chars, wordBegin, i - wordBegin));
                    wordBegin = -1;
                }
                //next char
                continue;
            }
            //not read word,word begin
            if (wordBegin == NOT_WORD) {
                wordBegin = i;
            }
        }
        if (wordBegin != NOT_WORD) {
            words.add(new String(chars, wordBegin, length - wordBegin));
        }
        return words;
    }

    public static String[] maxSplitWords(String line, int maxSplit) {
        if (maxSplit < 0) {
            throw new IllegalArgumentException("maxSplit can not be negative but is :" + maxSplit);
        }
        if (maxSplit == 0) {
            return new String[0];
        }
        String[] strings = new String[maxSplit];
        char[] chars = line.toCharArray();
        int length = chars.length;
        //-1 indicate now is not word
        int wordBegin = NOT_WORD;
        int wordNum = 0;
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            if (c == ' ') {
                if (wordBegin != NOT_WORD) {
                    //words num is add 1
                    strings[wordNum++] = new String(chars, wordBegin, i - wordBegin);
                    //word is end ,cache word,really to find next word
                    wordBegin = NOT_WORD;
                }
                //next char
                continue;
            }
            //not read word,word begin
            if (wordBegin == NOT_WORD) {
                wordBegin = i;
                //split is max,less char to one word,return
                if (wordNum == maxSplit - 1) {
                    strings[wordNum] = new String(chars, wordBegin, length - wordBegin);
                    return strings;
                }
            }
        }
        if (wordBegin != NOT_WORD) {
            strings[wordNum] = new String(chars, wordBegin, length - wordBegin);
        }
        return strings;
    }
}
