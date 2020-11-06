package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        //调用服务查询所有套餐信息
        List<Setmeal> setmealList = setmealService.findAll();

        // 拼接图片全路径
        setmealList.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });

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
        //调用服务查询套餐详情
        Setmeal setmeal = setmealService.findDetailById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
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
