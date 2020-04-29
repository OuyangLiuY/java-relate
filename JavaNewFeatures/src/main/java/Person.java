import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.*;

public class Person {
    int id;
    String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void create(int id, String name, PersonCallback callback){
        Person person = new Person(id,name);
        callback.callback(person);
    }

    public static void main(String[] args) {
        /*Person.create(1,"李斯", new PersonCallback(){
            @Override
            public void callback(Person person) {
                System.out.println(person.id + " == " + person.name);
            }
        });*/

        Consumer<String> consumer = System.out::println;
        consumer.accept("马谡");
        Function<Long,Long> f = Math::abs;
        Long result = f.apply(-10L);
        System.out.println(result);

        BiPredicate<String,String> b = String::contains;
        System.out.println(b.test("a", "a"));

        StringBuffer sb= new StringBuffer(10);
        System.out.println(sb.length());
        System.out.println(sb.toString());
        Function<String,StringBuffer> ss = StringBuffer::new;
        StringBuffer apply = ss.apply("123");
        System.out.println(apply.length());
        System.out.println(apply.toString());

    }

    public String getName(Person person){

       /* if(person!=null){
            return person.getName();
        }*/

     /*   Optional<Person> optional1 = Optional.ofNullable(person);
        Person person1 = optional1.get();
        if(optional1.isPresent()){
            return null;
        }
        Optional<Person> optional = Optional.of(person);

        return  optional.get().getName();*/

     return Optional.of(person).map(Person::getName).orElse(null);


  /*   return Optional.of(person).filter(person1->{

         return person1.getId() > 10;
     }).orElse(Person::getId);*/
    }



}
