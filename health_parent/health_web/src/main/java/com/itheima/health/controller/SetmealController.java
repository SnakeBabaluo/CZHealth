package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //获取图片的原始名字
        String originalFilename = imgFile.getOriginalFilename();

        //对原始图片名进行截取 得到 .jpg
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID工具类生成随机永不重复的文件名,拼接后缀名(.jpg),方便传输到七牛云服务器中
        String filename = UUID.randomUUID() + extension;

        //调用七牛云工具类上传图片
        try {
            //放入图片的字节流对象, 放入生成的文件名
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), filename);

            //创建一个map用来存储响应处理前端图片数据
            Map<String, String> resultMap = new HashMap<String, String>();
            //存入图片的文件名
            resultMap.put("imgName", filename);
            //存入七牛云的域名-以供回显图片时使用拼接的方法
            resultMap.put("domain", QiNiuUtils.DOMAIN);

            //返回响应
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传失败
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        //调用服务进行添加套餐
        setmealService.add(setmeal, checkgroupIds);
        updateRedis();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }
    public void updateRedis(){
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        String key = "health_setmealListInRedis";
        Setmeal setmeal = null;
        //调用服务查询所有套餐列表
        List<Setmeal> setmealList = setmealService.findAll();
        //查询数据库中是否有值
        String health_setmealListInRedis = jedis.get(key);
        if (StringUtils.isEmpty(health_setmealListInRedis)){
            //没有值,存入
            jedis.set(key, JSON.toJSONString(setmealList));
        }else {
            //有值,更新
           jedis.del(key);
           jedis.set(key,JSON.toJSONString(setmealList));
        }
        for (Setmeal s : setmealList) {
            Integer id = s.getId();
            //调用服务查询套餐详情
            setmeal =setmealService.findDetailById(id);
            String Detailkey = "health_setmealDetailListInRedis"+id;
            //查询数据库中是否有值
            String health_setmealDetailListInRedis = jedis.get(Detailkey);
            if (StringUtils.isEmpty(health_setmealDetailListInRedis)){
                //没有值,存入
                jedis.set(Detailkey,JSON.toJSONString(setmeal));
            }else {
                //有值,更新
                jedis.del(Detailkey);
                jedis.set(Detailkey,JSON.toJSONString(setmeal));
            }


        }




    }
    /**
     * 分页查询套餐数据
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务查询套餐数据
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);

        //返回数据
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    /**
     * 通过id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        //调用服务根据套餐id查询套餐信息
        Setmeal setmeal = setmealService.findById(id);

        //创建一个map用来存储图片的显示问题
        Map<String, Object> resultMap = new HashMap<>();
        //将返回的套餐信息存入map中
        resultMap.put("setmeal", setmeal);
        //调用七牛云工具类将图片存储到map中
        resultMap.put("domain", QiNiuUtils.DOMAIN);

        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, resultMap);
    }

    /**
     * 通过id查询选中的检查组ids
     * @param id
     * @return
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        //调用服务查询检查组的ids
        List<Integer> checkGroupIds = setmealService.findCheckgroupIdsBySetmealId(id);

        //返回响应
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupIds);
    }


    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        //调用服务修改套餐
        setmealService.update(setmeal, checkgroupIds);
        updateRedis();
        return new Result(true, "修改套餐成功");
    }

    /**
     * 删除套餐数据
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public Result deleteById(int id){
        //调用服务删除套餐
        setmealService.deleteById(id);
        updateRedis();
        //返回响应
        return new Result(true, "删除套餐成功");
    }
}
