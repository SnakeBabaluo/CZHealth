package com.itheima.health.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;
    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //如果jedis中查询到的是空值,说明里面没有存
        String key = "health_setmealListInRedis";
        String health_setmealListInRedis = jedis.get(key);
        List<Setmeal> setmealList = null;
        if (StringUtils.isEmpty(health_setmealListInRedis)) {
            //调用服务查询所有套餐信息
            setmealList = setmealService.findAll();
            // 拼接图片全路径
            setmealList.forEach(s -> {
                s.setImg(QiNiuUtils.DOMAIN + s.getImg());
            });
            //转成json字符串,存入Redis中
            jedis.set(key, JSON.toJSONString(setmealList));
        } else {
            //不为空,将json字符串转换为对象
             setmealList =  JSON.parseObject(health_setmealListInRedis, List.class);
        }
        jedis.close();
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmealList);
    }
    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        //创建jedis对象
        Jedis jedis = jedisPool.getResource();
        //如果jedis中查询到的是空值,说明里面没有存
        String Detailkey = "health_setmealDetailListInRedis"+id;
        String health_setmealDetailListInRedis = jedis.get(Detailkey);
        Setmeal setmeal =null;
        if (StringUtils.isEmpty(health_setmealDetailListInRedis)){
            //调用服务查询套餐详情
            setmeal = setmealService.findDetailById(id);
            // 设置图片的完整路径
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
            //转换成json字符串,存入redis中
            jedis.set(Detailkey,JSON.toJSONString(setmeal));
        }else {
            //如果不为空,则转换为pojo对象
             setmeal = JSON.parseObject(health_setmealDetailListInRedis, Setmeal.class);
        }
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 根据id查询套餐详情
     * @param id
     * @return
     */
    @PostMapping("/findById")
    public Result findById(int id){
        //调用服务查询套餐详情
        Setmeal setmeal = setmealService.findById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        //返回响应
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
