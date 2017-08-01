
package com.mmall.service.impl;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bu_dong on 2017/7/26.
 */

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoryService iCategoryService;


    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            String[] subImages = product.getSubImages().split(",");
            product.setMainImage(subImages[0]);

            if (product.getId() != null) {//update contact
                int updateCount = productMapper.updateByPrimaryKey(product);
                if (updateCount > 0) {
                    return ServerResponse.createBySuccess("商品更新成功");
                }
                return ServerResponse.createBySuccess("商品更新失败");
            } else { //insert product
                int insertCount = productMapper.insert(product);
                if (insertCount > 0) {
                    return ServerResponse.createBySuccess("商品添加成功");
                }
                return ServerResponse.createBySuccess("商品添加失败");
            }
        }
        return ServerResponse.createByErrorMessage("产品参数传入错误");
    }

    @Override
    public ServerResponse updateSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int updateCount = productMapper.updateByPrimaryKeySelective(product);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("修改商品状态成功");
        }
        return ServerResponse.createBySuccess("修改商品状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已经被删除或下架");
        }

        ProductDetailVo productDetailVo = wrapProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    ProductDetailVo wrapProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setSubTitle(product.getSubtitle());

        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setStatus(product.getStatus());

        Category category = categoryMapper.selectByPrimaryKey(product.getId());

        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;

    }


    @Override
    public ServerResponse<PageInfo> getProducttList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectProducts();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = wrapProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectProductsByNameAndId(productId, productName);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = wrapProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo wrapProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已经被删除或下架");
        }
        if (product.getStatus() != Const.SALE_STATUS.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已经被删除或下架");
        }

        ProductDetailVo productDetailVo = wrapProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> searchProductListByKeywords(String keywords, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {

        if (StringUtils.isBlank(keywords) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<>();//该分类下所有分类id
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keywords)) {
                //没有该分类结果，返回一个空的结果集
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);

            }

            categoryIdList = iCategoryService.selectCategoryAndChildById(categoryId).getData();
        }

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(keywords)) {
            keywords = new StringBuilder("%").append(keywords).append("%").toString();
        }

        //获取请求中的分类
        if (Const.PRODUCT_ORDER_TYPE.PRICE_ORDER.contains(orderBy)) {
            String[] order = orderBy.split("_");
            PageHelper.orderBy(order[0] + " " + order[1]);
        }
        List<Product> productList = productMapper.selectProductsByNameAndCategoryIdList(categoryIdList.size() == 0 ? null : categoryIdList,
                StringUtils.isBlank(keywords) ? null : keywords);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = wrapProductListVo(product);
            productListVoList.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

}
