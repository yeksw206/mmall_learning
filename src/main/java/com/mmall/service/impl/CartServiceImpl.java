package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.service.ICategoryService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by bu_dong on 2017/7/29.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        //查购物车该用户有没有这个产品 没有则新增
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            Cart insertCart = new Cart();
            insertCart.setUserId(userId);
            insertCart.setProductId(productId);
            insertCart.setChecked(Const.Cart.CHECKED);
            insertCart.setQuantity(count);
            int insertCount = cartMapper.insert(insertCart);
        } else {
            //如果购物车已经有这个商品，数量相加，判断库存
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }

        return this.list(userId);
    }


    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {

        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> delete(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);

       return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    //全选
    //全反选
    //单独选
    //单独反选
    @Override
    public ServerResponse<CartVo> updateCheckedOrUnChecked(Integer userId, Integer productId, Integer checked) {
        cartMapper.updateCheckedOrUnChecked(userId, null, checked);
        return list(userId);
    }

    //查询当前购物车里产品数量
    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        int count = cartMapper.selectCartProductCountByUserId(userId);
        return ServerResponse.createBySuccess(count);
    }




    //返回给前端的数据
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();

        List<CartProductVo> cartProductVoList = Lists.newArrayList();//1
        BigDecimal cartTotalPrice = new BigDecimal("0");//2

        //获取当前用户的所有购物车数据
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        //购物车cart类型zhuanh转化 商品_购物车CartProductVo
        if (!CollectionUtils.isEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setProductChecked(cartItem.getChecked());

                //查商品表 商品信息
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());


                    //计算购物车该商品总价 以及数量， limitQuantity
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()) {
                        //购物车商品数量 符合 该商品库存
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        //购物车数量超过了商品库存
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);

                        //更新购物车 该商品数量
                        Cart correctCountCart = new Cart();
                        correctCountCart.setId(cartItem.getId());
                        correctCountCart.setUserId(cartItem.getUserId());
                        correctCountCart.setProductId(cartItem.getProductId());
                        correctCountCart.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(correctCountCart);
                    }
                    cartProductVo.setQuantity(buyLimitCount);

                    //计算商品总价格cartProductVo
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(cartItem.getQuantity(), product.getPrice().doubleValue()));
                }

                //计算购物车选中的所有商品的总价格
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        //计算购物车所有商品是否全选
        cartVo.setAllChecked(getAllCheckStatus(userId));
        return cartVo;
    }




    //该用户下购物车是否全勾选
    private boolean getAllCheckStatus(Integer userId) {
        if (userId == null)
            return false;
        int unCheckedCount = cartMapper.selectCartProductAllCheckedByUserId(userId);
        if (unCheckedCount == 0) {
            return true;
        }
        return false;
    }
}
