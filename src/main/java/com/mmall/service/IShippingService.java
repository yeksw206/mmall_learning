package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * Created by bu_dong on 2017/7/29.
 */
public interface IShippingService {
     ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse delete(Integer shippingId, Integer userId);

    ServerResponse update(Shipping shipping, Integer userId);

    ServerResponse select(Integer shippingId, Integer userId);

    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
