package org.wengwx.com.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName ZkUtils
 * @Author wengweixin
 * @Description 对于zk的增删改查操作可以作为工具类，对于监听操作不建议方法工具类中
 * @Date 2020/11/30 22:45
 **/
public class ZkUtils {
    private static   Logger logger = LoggerFactory.getLogger(ZkUtils.class);

    private final static String ADDRESS="localhost:2181";
    private final static String CHARSET="UTF-8";
    private static CuratorFramework zkClient=null;
    static {
        zkClient= CuratorFrameworkFactory.newClient(ADDRESS,new ExponentialBackoffRetry(1000,3));
        zkClient.start();
    }

    /**
     * @Author wengweixin
     * @Date  2020/11/30 23:52
     * @Description     判断是否存在。Stat是对zNode所有属性的映射，stat==null表示节点不存在
     * @param path
     * @return boolean
     **/
   public static boolean checkExists(String path)throws Exception{
       Stat stat=getNodeStat(path);
       if(stat==null){
           return false;
       }else {
           return true;
       }
   }


   public static Stat getNodeStat(String path)throws Exception{
       Stat stat=zkClient.checkExists().forPath(path);
       return stat;
   }


   /**
    * @Author wengweixin
    * @Date  2020/11/30 23:53
    * @Description     查询子节点
    * @param path
    * @return java.util.List<java.lang.String>
    **/
   public static List<String> getChildNodes(String path)throws Exception{
       if(!checkExists(path)){
           throw new Exception("节点不存在");
       }
        List<String> childNodes=zkClient.getChildren().forPath(path);
        return childNodes;
   }

   /**
    * @Author wengweixin
    * @Date  2020/11/30 23:53
    * @Description     创建节点,创建已存在的节点会报错
    * @param path
   	 * @param data
    * @return void
    **/
   public static void createNode(String path,String data)throws Exception{
       if(checkExists(path)){
           throw new Exception("节点已存在");
       }
      zkClient.create().creatingParentsIfNeeded()//若创建节点的父节点不存在则先创建父节点再创建子节点
               .withMode(CreateMode.PERSISTENT)//创建的是持久节点
               .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)//默认匿名权限
               .forPath(path, data.getBytes(CHARSET));

   }

   /***
    * @Author wengweixin
    * @Date  2020/11/30 23:56
    * @Description     更新节点数据
    * @param path
   	 * @param data
    * @return void
    **/
   public static void updateNode(String path,String data)throws Exception{
       Stat stat=getNodeStat(path);
      if(stat==null){
          throw new Exception("节点不存在");
      }
        zkClient.setData()
                .withVersion(stat.getVersion())//乐观锁
                .forPath(path,data.getBytes(CHARSET));
   }

   /**
    * @Author wengweixin
    * @Date  2020/11/30 23:57
    * @Description     读取节点数据
    * @param path
    * @return java.lang.String
    **/
   public static String readZKNode(String path)throws Exception{
       if(!checkExists(path)){
           throw new Exception("节点不存在");
       }
        Stat stat=new Stat();
        String data=new String(zkClient.getData()
        //.storingStatIn(stat)//在获取节点内容的同时把状态信息存入stat对象，如果不写的话就只会读取节点数据
        .forPath(path));
        return data;
   }

   /***
    * @Author wengweixin
    * @Date  2020/11/30 23:58
    * @Description     删除节点
    * @param path
    * @return void
    **/
   public static void deleteNode(String path)throws Exception{
       Stat stat=getNodeStat(path);
       if(stat==null){
           throw new Exception("节点不存在");
       }

        zkClient.delete()
                .guaranteed()//保障机制，若为删除成功，只要会话有效就会在后台持续尝试删除
                .deletingChildrenIfNeeded()//若当前节点包含子节点，子节点也删除
                .withVersion(stat.getVersion())//指定版本号
                .forPath(path);
   }



    //======监听器在这里测不了的，只是作为一个例子使用=========
   /**
    * @Author wengweixin
    * @Date  2020/12/1 9:13
    * @Description    watcher事件，使用usingWather的时候，监听只会触发一次，监听完毕后就销毁
    * @param path
   	 * @param curatorWatcher
    * @return void
    **/
   public static void addCuratorWatcher(String path)throws Exception{
       zkClient.getData().usingWatcher(new CuratorWatcher() {
           @Override
           public void process(WatchedEvent event) throws Exception {
               logger.info("触发watcher,path:{}",event.getPath());
           }
       }).forPath(path);
   }

   /**
    * @Author wengweixin
    * @Date  2020/12/1 9:16
    * @Description    watcher事件，NodeCache一次注册n次监听，缺点：没法监听子节点和判断本节点增删改事件，所以引出PathchildrenCache
    * @param path
   	 * @param nodeCacheListener
    * @return void
    **/
   public static void addNodeCacheListener(String path)throws Exception{
       NodeCache nodeCache=new NodeCache(zkClient,path);
       nodeCache.start(true);//当zkServer与客户端链接的时候，NodeCache会把zk数据缓存在本地
       if(nodeCache.getCurrentData()!=null){
           logger.info("节点初始化数据为：{}",new String(nodeCache.getCurrentData().getData()));
       }else {
           logger.info("节点初始化数据为空");
       }
       nodeCache.getListenable().addListener(new NodeCacheListener() {
           @Override
           public void nodeChanged() throws Exception {
               if (nodeCache.getCurrentData() != null) {
                   String re = new String(nodeCache.getCurrentData().getData());
                   logger.info("节点路径:{}, 节点数据:{}", nodeCache.getCurrentData().getPath(), re);
               } else {
                   logger.info("当前节点被删除了");
               }
           }
       });
   }

   /**
    * @Author wengweixin
    * @Date  2020/12/1 9:29
    * @Description  监听父节点以下所有的子节点，当子节点发生变化的时候（增删改）都会监听到
    * 为子节点添加watcher事件
    * PathChildrenCache监听数据结点的增删改
    * @param path
    * @return void
    **/
   public static void addPathChildrenCacheListener(String path)throws Exception{
       PathChildrenCache pathChildrenCache=new PathChildrenCache(zkClient,path,true);
       //NORMAL:异步初始化，BUILD_INITAL_CACHE:同步初始化，POST_INITALIZED_EVENT:异步初始化，初始化之后会触发事件
       pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
       List<ChildData> childDataList=pathChildrenCache.getCurrentData();//当前数据结点的子节点数据列表
       pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
           @Override
           public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
               if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                   logger.info("子节点初始化ok..");
               } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                   logger.info("添加子节点, path:{}, data:{}", event.getData().getPath(), event.getData().getData());
               } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                   logger.info("删除子节点, path:{}", event.getData().getPath());
               } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                   logger.info("修改子节点, path:{}, data:{}", event.getData().getPath(), event.getData().getData());
               }
           }
       });
   }
}
