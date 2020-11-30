package test;

/**
 * @ClassName StringTest
 * @Author wengweixin
 * @Date 2020/11/27 9:58
 **/
public class StringTest {
    @org.junit.Test
    public void stringTest(){

        //编译期可确定值的常量就是编译期常量，即被final修饰的字符串变量，final String a = "12"，a也是编译器常量。String b = "34"，因为不是final的所以不是编译期常量。
        //字符串属于引用数据类型。对于引用类型，==是地址值的比较

        String s1="1234";
        //对于两个编译期常量字符串相加,编译器会进行优化，直接优化成"1234"，所以不会在字符串常量池中创建"12"，"34"这两个字符串对象，而只会创建一个"1234"字符串对象。
        String s2="12"+"34";
        System.out.println(s1==s2);

        //对象保存在堆内存中，所以肯定不相等
        String s3=new String("1234");
        System.out.println(s1==s3);

        //s4+"34"在运行期，相加的结果是生成一个新的字符串对象放置在堆内存中
        String s4="12";
        String s5=s4+"34";
        System.out.println(s1==s5);

        //引用不变，但是还是指向了在堆内存中的字符串对象
        String s6="12";
        s6+="34";
        System.out.println(s1==s6);

        //substring和replace出来的都是新的字符串，即使常量池中存在截取后的或者替换后的字符串，==也不相等。
        String s7="123456".substring(0,4);
        System.out.println(s1==s7);

        //堆对象+创建的对象依然是堆对象
        String s8="12";
        String s9="34";
        String s10=s8+s9;
        System.out.println(s1==s10);

        //s11被修饰成编译期常量了，相当于s12=“12”+“34”，会被编译器优化
        final String s11="12";
        String s12=s11+"34";
        System.out.println(s1==s12);

        //这种方式还是创建成堆对象
        String s13=new String("12");
        String s14=s13+"34";
        System.out.println(s14==s1);


        //这是intern作用的例子,堆对象调用inter会将自身放到字符常量中
        String b=new String("9")+new String("9");
        b.intern();
        String a="99";
        System.out.println(a==b);

        String c=new String("19")+new String("19");
        String d="99";
        System.out.println(c==d);

    }
}
