package inner;

public class Test
{

}

/**
 * This is a sample file.
 */
class Foo
{
    private int field1;
    private int field2;
    private boolean flag;
    {
        field1 = 2;
    }

    public void foo1()
    {
        new Runnable()
        {
            public void run()
            {
                if(flag)
                {

                }else if(field1 > 0)
                {

                }else
                {

                }
            }
        };
    }

    public class InnerClass
    {

    }
}

class AnotherClass
{

}

interface TestInterface
{
    int MAX = 10;
    int MIN = 1;

    void method1();
    void method2();
}