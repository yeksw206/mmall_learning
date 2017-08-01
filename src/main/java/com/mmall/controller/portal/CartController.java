package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by bu_dong on 2017/7/29.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    ICartService iCartService;

    @ResponseBody
    @RequestMapping("add.do")
    ServerResponse add(HttpSession session, Integer productId, Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @ResponseBody
    @RequestMapping("update.do")
    ServerResponse update(HttpSession session, Integer productId, Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @ResponseBody
    @RequestMapping("delete_product.do")
    ServerResponse delete(HttpSession session, String productIds){//1,7
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.delete(user.getId(), productIds);
    }

    @ResponseBody
    @RequestMapping("list.do")
    ServerResponse list(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @ResponseBody
    @RequestMapping("select_all.do")
    ServerResponse select_all(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(), null, Const.Cart.CHECKED);
    }


    @ResponseBody
    @RequestMapping("un_select_all.do")
    ServerResponse un_select_all(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),null, Const.Cart.UN_CHECKED);
    }

    @ResponseBody
    @RequestMapping("select.do")
    ServerResponse select(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),productId, Const.Cart.CHECKED);
    }

    @ResponseBody
    @RequestMapping("un_select.do")
    ServerResponse un_select(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),productId, Const.Cart.UN_CHECKED);
    }

    @ResponseBody
    @RequestMapping("get_cart_product_count.do")
    ServerResponse<Integer> get_cart_product_count(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
