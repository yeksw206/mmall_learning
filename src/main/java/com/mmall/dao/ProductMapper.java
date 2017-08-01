package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProducts();

    List<Product> selectProductsByNameAndId(@Param("productId") Integer productId, @Param("productName") String productName);

    List<Product> selectProductsByNameAndCategoryIdList(@Param("categoryIdList") List<Integer> categoryIdList, @Param("productName") String productName);
}