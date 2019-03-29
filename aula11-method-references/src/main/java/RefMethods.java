import java.sql.Ref;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class RefMethods {

    static class MyType {
        public static int m0() { return 0; }
        public int m1() { return 0; }
        public static int m2(MyType mt) { return 1; }
        public static int m3(MyType mt, int i) { return 2; }
    }

    private static int DEFAULT=123;

    private int val;
    private String[] names;

    public RefMethods(int v) {
        val = v;
    }

    public RefMethods() {
        val = DEFAULT;
    }

    public RefMethods(String[] names) {
        this.names = names;
    }

    private int mapper(String name) {

        return name.length();
    }

    public void m() {

        // métodos estáticos

        Supplier<Integer> f1 = () -> MyType.m0();
        Supplier<Integer> f1a = MyType::m0; //() -> MyType.m0();

        Function<MyType, Integer> f2=
                (MyType mt) -> MyType.m2(mt);
        Function<MyType, Integer> f2b = MyType::m2;
        f2b.apply(new MyType());
        //ToIntFunction<MyType> f2a= (MyType mt) -> MyType.m2(mt);


        BiFunction<MyType,Integer,Integer> f3=
                (MyType mt, Integer i) -> MyType.m3(mt,i);
        BiFunction<MyType,Integer,Integer> f3a= MyType::m3;


        // metodos de instância


        Function<MyType, Integer> f4 =
                (MyType mt) -> mt.m1();

        //Quando o this é passado como parâmetro
        Function<MyType, Integer> f4a = MyType::m1;
        f4a.apply(new MyType());

        Function<String, Integer> f5 =
                (String n) -> this.mapper(n);

        RefMethods ref1 = new RefMethods();
        // quando o this é especificado no method reference
        Function<String, Integer> f5a = ref1::mapper;


        // construtores
        Supplier<RefMethods> f6 =() -> new RefMethods();
        Supplier<RefMethods> f6a = RefMethods::new;

        Function<Integer, RefMethods> f7=
                (Integer i) -> new RefMethods(i);
        Function<Integer, RefMethods> f7a= RefMethods::new;
        Function<String[],RefMethods> f8 =
                (String[] n) -> new RefMethods(n);
        Function<String[],RefMethods> f8a = RefMethods::new;

        Function<Integer, RefMethods[]> f9 =
                (Integer n) -> new RefMethods[n];
        Function<Integer, RefMethods[]> f9a =
                RefMethods[]::new;


        // ignoring parameter
        //Function<MyType, Integer> ai4 = (__) -> 0;

    }
}
