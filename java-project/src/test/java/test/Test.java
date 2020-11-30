package test;



import com.google.gson.Gson;
import org.wengwx.com.entiry.User;
import org.wengwx.com.entiry.UserVoProtos;
import org.wengwx.com.exception.MyselfException;
import org.wengwx.com.service.ServiceFirst;
import org.wengwx.com.util.ClassLoaderUtils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Test
 * @Author wengweixin
 * @Date 2020/11/24 11:31
 **/
public class Test {
    String name="sdfandsfnaondss深V案件女就按覅是几百遍第八十九点半是局in爱睡觉独女UI爱是本地是酒吧街办VB是吧v111111开始实施所所所所所所所所所扩扩扩扩扩扩扩扩扩木木木木木木多多多度uuuu1萨顶顶那你就能促进菜鸟仓家具板均具备VB不VBuvkznsjkxnjcknzkjnjnv111堆内存中农家菜你净资产v" +
            "深刻的哪里看大V领就拿了几女理解能力几女老姐这次女零结案短时间内看来这内存V领叫女婿陆家嘴哪里吃是机虚拟质量检测哪些了1" +
            "可执行老年村是几楼哪里仅支持女婿节几点见覅就师弟of极爱哦圣诞节覅偶氨基丁酸覅OA搜城门口科技兴农虚假处女" +
            "净资产那可就尊小客车VN证件查询你";
    String phone="13128778123";
    int age=987654321;

    @org.junit.Test
    public void test1() {
        Map<String,Object> testData=new HashMap<>();
        List<Map> whiteList=new ArrayList<>();
        Map<String,String> exceptionWhiteItem1=new HashMap<>();
        exceptionWhiteItem1.put("type","java.lang.ClassCastException,java.sql.SQLException");
        exceptionWhiteItem1.put("msg","");
        whiteList.add(exceptionWhiteItem1);
        Map<String,String> exceptionWhiteItem2=new HashMap<>();
        exceptionWhiteItem2.put("type","java.lang.NullPointerException");
        exceptionWhiteItem2.put("msg","白名单");
        whiteList.add(exceptionWhiteItem2);
        testData.put("*",whiteList);
        System.out.println(new Gson().toJson(testData));
    }

    @org.junit.Test
    public void test2(){
        try{
            testGson();
            testSerial();
            testProtoBuf();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void testGson() {
        long t=System.currentTimeMillis();
        Gson gson = new Gson();
        String json = gson.toJson(getUser());
        System.out.println("Gson消耗的毫秒数："+(System.currentTimeMillis()-t));
        System.out.println("Gson字节数：" + json.getBytes().length);
        System.out.println("=====================");
    }
    public  void testSerial() throws Exception {
        long t=System.currentTimeMillis();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(getUser());
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("java序列化消耗的毫秒数："+(System.currentTimeMillis()-t));
        System.out.println("java序列化后的字节数：" + b.length);
        bos.close();
        System.out.println("=====================");
    }
    public  void testProtoBuf() throws Exception {
        long t=System.currentTimeMillis();
        UserVoProtos.User.Builder builder = UserVoProtos.User.newBuilder();
        builder.setName(name);
        builder.setAge(age);
        builder.setPhone(phone);
        UserVoProtos.User vo = builder.build();
        byte[] v = vo.toByteArray();
        System.out.println("rotobuf消耗的毫秒数："+(System.currentTimeMillis()-t));
        System.out.println("Protobuf字节数：" + v.length);
        System.out.println("=====================");

    }
    public User getUser() {
        User user = new User();
        user.setAge(1034223323);
        user.setName(name);
        user.setPhone(phone);
        return user;
    }




}
