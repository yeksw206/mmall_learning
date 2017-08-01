package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by bu_dong on 2017/7/29.
 */

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    IShippingService iShippingService;

    @ResponseBody
    @RequestMapping("add.do")
    ServerResponse add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return iShippingService.add(user.getId(),shipping);
    }

    @ResponseBody
    @RequestMapping("del.do")
    ServerResponse delete(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return iShippingService.delete(shippingId, user.getId());
    }

    @ResponseBody
    @RequestMapping("update.do")
    ServerResponse delete(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return iShippingService.update(shipping, user.getId());
    }

    @ResponseBody
    @RequestMapping("select.do")
    ServerResponse<Shipping> select(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return iShippingService.select(shippingId, user.getId());
    }

    @ResponseBody
    @RequestMapping("list.do")
    ServerResponse<PageInfo> list(HttpSession session,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("请登录");
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
