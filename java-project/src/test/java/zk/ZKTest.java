package zk;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.WatchedEvent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wengwx.com.util.ZkUtils;

import java.util.List;

/**
 * @ClassName ZKTest
 * @Author wengweixin
 * @Date 2020/12/1 10:11
 **/
public class ZKTest {
    private  Logger logger = LoggerFactory.getLogger(ZKTest.class);


    public final static String  rootPath="/ZK/test";

    @Test
    public void createNodeTest(){
        try{
            for(int i=0;i<3;i++){
                String parentPath=rootPath+"/test"+String.valueOf(i);
                ZkUtils.createNode(parentPath,parentPath);
                for(int j=0;j<5;j++){
                    String childrenPath=parentPath+"/test"+String.valueOf(i)+String.valueOf(j);
                    ZkUtils.createNode(childrenPath,childrenPath);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void checlExistsTest(){
        String testPath=rootPath+"/test01";

        try {
            if(ZkUtils.checkExists(testPath)){
                System.out.println("节点存在");
            }else {
                System.out.println("节点不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getChildNodesTest(){
        String testPath=rootPath+"/test0";
        try {
            List<String> childrenPath=ZkUtils.getChildNodes(testPath);
            System.out.println(childrenPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateNodeTest(){
        String testPath=rootPath+"/test0";
        try {
            ZkUtils.updateNode(testPath,"这是更新的数据");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readZKNode(){
        String testPath=rootPath+"/test0";
        try {
            String data=ZkUtils.readZKNode(testPath);
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteNodeTest(){
        String testPath=rootPath+"/test2";
        try {
            ZkUtils.deleteNode(testPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
