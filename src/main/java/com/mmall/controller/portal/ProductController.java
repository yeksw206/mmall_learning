package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by bu_dong on 2017/7/28.
 */


@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    IProductService iProductService;

    @ResponseBody
    @RequestMapping("detail.do")
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @ResponseBody
    @RequestMapping("list.do")
    ServerResponse<PageInfo> list(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                  @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.searchProductListByKeywords(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
