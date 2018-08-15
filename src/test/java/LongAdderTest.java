import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {
    public static void main(String[] args) throws FileNotFoundException {
        LongAdder adder = new LongAdder();
        adder.add(10);
        adder.add(5);
        new FileInputStream(new File(""));
    }
}
