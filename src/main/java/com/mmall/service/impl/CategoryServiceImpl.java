package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by bu_dong on 2017/7/24.
 */

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentID) {
        if (parentID == null || StringUtils.isBlank(categoryName))
            ServerResponse.createByErrorMessage("添加商品分类参数错误");
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentID);
        category.setStatus(true);

        int insertCount = categoryMapper.insert(category);
        if (insertCount >= 0) {
            return ServerResponse.createBySuccessMessage("添加商品分类成功");
        }

        return ServerResponse.createByErrorMessage("添加商品分类失败");
    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName))
            ServerResponse.createByErrorMessage("更新分类名称s参数错误");
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int insertCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (insertCount >= 0) {
            return ServerResponse.createBySuccessMessage("更新分类名称成功");
        }

        return ServerResponse.createByErrorMessage("修改分类名称失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildCategory(Integer parentId) {

        List<Category> categoryList = categoryMapper.selectChildCategoryByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("没有查找到该分类的子分类信息");
        }
        return ServerResponse.createBySuccess(categoryList);
    }




    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildById(Integer categoryId) {

        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categorySet != null) {
            for (Category category : categorySet) {
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectChildCategoryByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
