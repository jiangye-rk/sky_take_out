package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
      Dish dish = new Dish();
      BeanUtils.copyProperties(dishDTO,dish);
      dishMapper.insert(dish);
      Long dishId=dish.getId();
      List<DishFlavor> flavors=dishDTO.getFlavors();
      if(flavors!=null && flavors.size()>0){
          flavors.forEach(dishFlavor->{
              dishFlavor.setDishId(dishId);
          });
      }
      dishFlavorMapper.insertBatch(flavors);
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        //检查被删除的菜品是否起售
        for (Long id:ids){
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //检查被删除的菜品是否关联套餐
    List<Long> setMealIds=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setMealIds!=null&&setMealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
    for(Long id:ids){
        dishMapper.deleteById(id);
        dishFlavorMapper.deleteByDishId(id);
    }
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish=dishMapper.getById(id);
        List<DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //向口味表插入n条数据
        List<DishFlavor> dishFlavors=dishDTO.getFlavors();
        if(dishFlavors!=null&&dishFlavors.size()>0){
            dishFlavors.forEach(dishFlavor->{
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(dishFlavors);
        }

    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish=Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish=Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        List<Dish> list=dishMapper.list(dish);
        return list;
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

}
