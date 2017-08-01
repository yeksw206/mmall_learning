package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by bu_dong on 2017/7/24.
 */
public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName, Integer parentID);

    ServerResponse updateCategoryName(String categoryName, Integer parentId);

     ServerResponse<List<Category>> getChildCategory(Integer parentId);

     ServerResponse<List<Integer>> selectCategoryAndChildById(Integer categoryId);

}
