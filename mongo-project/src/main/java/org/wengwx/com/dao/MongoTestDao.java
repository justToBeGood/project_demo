package org.wengwx.com.dao;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.wengwx.com.entiry.MongoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @ClassName MongoTestDao
 * @Author wengweixin
 * @Date 2020/11/23 17:21
 **/
@Component
public class MongoTestDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 创建对象
     */
    public void saveTest(MongoTest test) {
        mongoTemplate.save(test);
    }

    /**
     * 根据用户名查询对象
     * @return
     */
    public MongoTest findTestByName(String name) {
        Query query=new Query(Criteria.where("name").is(name));
        MongoTest mgt =  mongoTemplate.findOne(query , MongoTest.class);
        //List<MongoTest> list=mongoTemplate.find(query,MongoTest.class);
        return mgt;
    }

    /**
     * 更新对象
     */
    public void updateTest(MongoTest test) {
        Query query=new Query(Criteria.where("id").is(test.getId()));
        Update update= new Update().set("age", test.getAge()).set("name", test.getName());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query,update,MongoTest.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    public void deleteTestById(Integer id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,MongoTest.class);
    }

    //文件上传
    public ObjectId fileStore(InputStream inputStream, String fileName, String contentType, DBObject metadata){
        return gridFsTemplate.store(inputStream,fileName,contentType,metadata);

    }

    public GridFSFile getById(String id){
        Query query=new Query(Criteria.where("_id").is(id));
        return gridFsTemplate.findOne(query);
    }

    public GridFSFile getByFileName(String filename){
        Query query=new Query(Criteria.where("filename").is(filename));
        return gridFsTemplate.findOne(query);
    }


    public GridFSFindIterable listAll(){
        Query query=new Query();
        return gridFsTemplate.find(query);
    }

    public void removeFileById(String id){
        Query query=new Query(Criteria.where("_id").is(id));
        gridFsTemplate.delete(query);
    }

    public void removeFileByFileName(String fileName){
        Query query=new Query(Criteria.where("filename").is(fileName));
        gridFsTemplate.delete(query);
    }

    public InputStream downLoadFile(String id){
        GridFSFile gridFSFile= getById(id);
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        InputStream inputStream=null;
        try {
            inputStream = gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return inputStream;
        }


    }
}
