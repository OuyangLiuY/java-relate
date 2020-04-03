package java.memory.mode.finall;

public class JavaFinalReference {
    // final 是引用类型
    final int[] intArray;
    static JavaFinalReference obj;

    public JavaFinalReference() {
        intArray = new int[1];
        intArray[0] = 1;
    }

    //线程A写
    static void writerA() {
        obj = new JavaFinalReference();
    }

    //线程B写
    static void writerB() {
        obj.intArray[0] = 2;
    }

    static void readC() {
        if (obj != null) {
            int temp = obj.intArray[0];
        }
    }
}
