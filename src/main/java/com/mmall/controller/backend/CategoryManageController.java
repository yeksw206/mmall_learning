package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by bu_dong on 2017/7/24.
 */

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add_category(String categoryName,
                                               @RequestParam(value = "parentId", defaultValue = "0") int parentId,
                                               HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录 请先登录");
      //  ServerResponse response = iUserService.checkRoleAdmin(user);
       // if (response.isSuccessful()) {
            return iCategoryService.addCategory(categoryName, parentId);
       // }
       // return ServerResponse.createByErrorMessage("你不是管理员，你没有权利添加分类");
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse set_category_name(String categoryName, int categoryId, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录 请先登录");
        ServerResponse response = iUserService.checkRoleAdmin(user);
      //  if (response.isSuccessful()) {
            return iCategoryService.updateCategoryName(categoryName, categoryId);
       // }
      //  return ServerResponse.createByErrorMessage("你不是管理员，你没有权利更新分类名字");
    }


    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getChildCategory(@RequestParam(value = "categoryId", defaultValue = "0") int categoryId, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录 请先登录");
       // ServerResponse response = iUserService.checkRoleAdmin(user);
       // if (response.isSuccessful()) {S
            return iCategoryService.getChildCategory(categoryId);
     //   }
       // return ServerResponse.createByErrorMessage("你不是管理员，你没有权利获取子分类信息");
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndALLChildCategoryList(int categoryId, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录 请先登录");
       // ServerResponse response = iUserService.checkRoleAdmin(user);
       // if (response.isSuccessful()) {
            return iCategoryService.selectCategoryAndChildById(categoryId);
       // }
       // return ServerResponse.createByErrorMessage("你不是管理员，你没有权利获取当前分类id及递归子节点categoryId");
    }
}
