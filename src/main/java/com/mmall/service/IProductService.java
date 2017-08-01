package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by bu_dong on 2017/7/26.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse updateSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProducttList(Integer pageNum, Integer pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> searchProductListByKeywords(String keywords, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
;