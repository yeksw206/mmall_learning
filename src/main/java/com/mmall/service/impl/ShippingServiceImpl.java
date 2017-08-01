package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by bu_dong on 2017/7/29.
 */

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int insertCount = shippingMapper.insert(shipping);
        if (insertCount > 0) {
            Map map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccessMessage("新建地址成功", map);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse delete(Integer shippingId, Integer userId) {
        int delCount = shippingMapper.deleteByShippingIdAndUserId(shippingId, userId);
        if (delCount > 0) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse update(Shipping shipping, Integer userId) {
        shipping.setUserId(userId);
        int updateCount = shippingMapper.updateShipping(shipping);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer shippingId, Integer userId) {
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(shippingId, userId);
        if (shipping != null) {
            return ServerResponse.createBySuccess(shipping);
        }
        return ServerResponse.createByErrorMessage("无法查询到该地址");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {

        //1
        PageHelper.startPage(pageNum, pageSize);

        //2query data list
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);

        //3
        PageInfo pageInfo = new PageInfo(shippingList);

        return ServerResponse.createBySuccess(pageInfo);

    }
}
