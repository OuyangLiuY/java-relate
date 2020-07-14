package inner;

public class InnerClass {
    double two = 0;

    private void method() {
        System.out.println("innerclass");
    }

    private Inner getInstance() {
        return new Inner();
    }

    class Inner {
        private String aa = "10";

        public String getAa() {
            return aa;
        }

        private void method() {
            System.out.println("inner");
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        public Inner() {
        }

        public Inner(String aa) {
            this.aa = aa;
        }
    }

    public static void main(String[] args) {
        InnerClass innerClass = new InnerClass();
        innerClass.method();
        Inner instance = innerClass.getInstance();
        instance.method();
    }
}
