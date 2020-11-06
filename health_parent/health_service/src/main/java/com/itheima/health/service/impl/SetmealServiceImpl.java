package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 分页查询套餐数据
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //调用pageHelper中的startPage方法查询数据库信息
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //判断是否有查询条件,如果有就进行 % 拼接查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        //调用dao进行条件查询
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());

        //将数据存放到PageResult中
        PageResult<Setmeal> pageResult = new PageResult<>(page.getTotal(), page.getResult());

        return pageResult;
    }

    /**
     * 通过id查询套餐信息
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 通过id查询选中的检查组ids
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐信息
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 更新套餐信息
        setmealDao.update(setmeal);

        //删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());

        //判断检查组id
        if (null != checkgroupIds){
            //遍历所有检查组的id
            for (Integer checkgroupId : checkgroupIds) {
                //建立新的套餐与检查组信息
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    /**
     * 删除套餐数据
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //查询数据库,看套餐是否被使用,如果被使用了就不能删除,抛出异常
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0){
            throw new HealthException("该套餐已被使用,无法删除");
        }

        //删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);

        //删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 添加套餐
     *
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐信息
        setmealDao.add(setmeal);
        //获取套餐的id
        Integer setmealId = setmeal.getId();

        //对checkgroupIds进行非空判断
        if (null != checkgroupIds) {
            //遍历检查组ids
            for (Integer checkgroupId : checkgroupIds) {
                //添加套餐与检查组的关系
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
    }

    /**
     * 查询数据库中所有的图片
     *
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    /**
     * 查询所有套餐
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 查询套餐详情
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    /**
     * 获取套餐预约数量
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        //调用dao查询套餐预约的数据
        return setmealDao.findSetmealCount();
    }
}
