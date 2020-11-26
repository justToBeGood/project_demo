package org.wengwx.com.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RequestParam;
import org.wengwx.com.dao.MongoTestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wengwx.com.entiry.MongoTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MongoTestController
 * @Author wengweixin
 * @Date 2020/11/23 17:29
 **/
@RestController
@RequestMapping("/mongo")
public class MongoTestController {
    @Autowired
    private MongoTestDao mongoTestDao;

    @GetMapping(value="/test1")
    public void saveTest() throws Exception {
        MongoTest mgtest=new MongoTest();
        mgtest.setId(11);
        mgtest.setAge(33);
        mgtest.setName("ceshi");
        mongoTestDao.saveTest(mgtest);
    }

    @GetMapping(value="/test2")
    public MongoTest findTestByName(){
        MongoTest mgtest= mongoTestDao.findTestByName("ceshi");
        System.out.println("mgtest is "+mgtest);
        return mgtest;
    }

    @GetMapping(value="/test3")
    public void updateTest(){
        MongoTest mgtest=new MongoTest();
        mgtest.setId(11);
        mgtest.setAge(44);
        mgtest.setName("ceshi2");
        mongoTestDao.updateTest(mgtest);
    }

    @GetMapping(value="/test4")
    public void deleteTestById(){
        mongoTestDao.deleteTestById(11);
    }

    @GetMapping("/fileUpload")
    public  void fileUpload()throws Exception{
        File file=new File("D:\\UpupooWallpaper\\1800049059\\previewFix.jpg");
        String name=file.getName();
        String contentType="image/jpp";
        InputStream cotent=new FileInputStream(file);
        //存储文件的额外信息，比如用户ID,后面要查询某个用户的所有文件时就可以直接查询
        Map<String,Object> map=new HashMap<>();
        map.put("用户id","001");
        DBObject metadata=new BasicDBObject(map);
        ObjectId objectId=mongoTestDao.fileStore(cotent,name,contentType,metadata);
        System.out.println(objectId.toString());
    }

    @GetMapping("/getFileById")
    public void getFile(@RequestParam("id") String id){
        GridFSFile gridFSFile=mongoTestDao.getById(id);
        gridFSFile.toString();
    }

    @GetMapping("/getFileByName")
    public void getFileByName(@RequestParam("fileName") String fileName){
        GridFSFile gridFSFile=mongoTestDao.getByFileName(fileName);
        gridFSFile.toString();
    }

    @GetMapping("/getAll")
    public void getAll(){
        GridFSFindIterable gridFSFiles=mongoTestDao.listAll();
    }

    @GetMapping("/deleteById")
    public void deleteById(@RequestParam("id") String id){
        mongoTestDao.removeFileById(id);
    }

    @GetMapping("/deleteByName")
    public void deleteByName(@RequestParam("fileName")String fileName){
        mongoTestDao.removeFileByFileName(fileName);
    }

    @GetMapping("/download")
    public void download(@RequestParam("id")String id){
        InputStream fileInputStream = mongoTestDao.downLoadFile(id);

    }

}
