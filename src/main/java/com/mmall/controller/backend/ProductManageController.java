package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by bu_dong on 2017/7/26.
 */

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {
    private Logger logger = LoggerFactory.getLogger(ProductManageController.class);

    @Autowired
    IUserService iUserService;

    @Autowired
    IProductService iProductService;

    @Autowired
    IFileService iFileService;

    @ResponseBody
    @RequestMapping("save.do")
    ServerResponse saveProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            return iProductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }

    @ResponseBody
    @RequestMapping("set_sale_status.do")
    ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            return iProductService.updateSaleStatus(productId, status);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }

    @ResponseBody
    @RequestMapping("detail.do")
    ServerResponse<ProductDetailVo> getProductDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            return iProductService.manageProductDetail(productId);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }

    @ResponseBody
    @RequestMapping("list.do")
    ServerResponse getProducttList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            return iProductService.getProducttList(pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }


    @ResponseBody
    @RequestMapping("search.do")
    ServerResponse searchProduct(HttpSession session, Integer productId, String productName, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }

    @ResponseBody
    @RequestMapping("upload.do")
    ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session, HttpServletRequest request) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请用管理员登录");
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            //上传web-INF/upload --> 上传到ftp -->删除upload
            String path = request.getSession().getServletContext().getRealPath("upload");

            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            Map<String, String> fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByErrorMessage("你不是管理员，没有权限操作");
    }


    /*
    * 富文本上传 使用simditor
    * {
    * "success": true/false,
    * "msg": "error message", # optional
    * " file_path": "[real file path]"
    *}
     */
    @ResponseBody
    @RequestMapping("richtext_img_upload.do")
    Map uploadRichText(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpSession session, HttpServletRequest request,
                       HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录，请用管理员登录");
            return resultMap;
        }
        if (iUserService.checkRoleAdmin(user).isSuccessful()) {
            //上传web-INF/upload --> 上传到ftp -->删除upload
            String path = request.getSession().getServletContext().getRealPath("upload");

            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                response.addHeader("Access-Control-Allow-Headers","X-File-Name");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            resultMap.put("success", false);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            return resultMap;
        }

        resultMap.put("success", false);
        resultMap.put("msg", "你不是管理员，没有权限操作");
        return resultMap;
    }

}
