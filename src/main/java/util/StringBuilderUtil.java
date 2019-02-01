package util;

public class StringBuilderUtil {
    public static <E> Splitter<E> getSplitter(StringBuilder sb, E split) {
        return new Splitter<>(sb, split);
    }

    public static class Splitter<E> {
        private boolean begin = true;
        private StringBuilder sb;
        private E split;

        private Splitter(StringBuilder sb, E split) {
            this.sb = sb;
            this.split = split;
        }


        public StringBuilder split() {
            if (!begin) sb.append(split);
            else begin = false;
            return sb;
        }

        public boolean isBegin() {
            return begin;
        }

        public StringBuilder getSb() {
            return sb;
        }
    }
}


