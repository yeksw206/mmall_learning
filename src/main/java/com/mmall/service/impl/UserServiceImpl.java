package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by bu_dong on 2017/7/20.
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        ServerResponse<String> userResponse = checkValid(username, Const.CHECK_TYPE.USERNAME);
        if (userResponse.isSuccessful()) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //MD5加密
        String mPassword = MD5Util.MD5EncodeUtf8(password);

        User resultUser = userMapper.selectLogin(username, mPassword);
        if (resultUser == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        resultUser.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccessMessage("登录成功", resultUser);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> userResponse = checkValid(user.getUsername(), Const.CHECK_TYPE.USERNAME);
        if (!userResponse.isSuccessful()) {
            return userResponse;
        }

        ServerResponse<String> emailResponse = checkValid(user.getEmail(), Const.CHECK_TYPE.EMAIL);
        if (!emailResponse.isSuccessful()) {
            return emailResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOME);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int insertCount = userMapper.insert(user);
        if (insertCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String checkData, String checkType) {
        if (!StringUtils.isNotBlank(checkType)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        if (Const.CHECK_TYPE.EMAIL.equals(checkType)) {
            int emailCount = userMapper.checkEmail(checkData);
            if (emailCount > 0) {
                return ServerResponse.createByErrorMessage("Email已经存在");
            }
        }
        if (Const.CHECK_TYPE.USERNAME.equals(checkType)) {
            int resultCount = userMapper.checkUsername(checkData);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("用户名已经存在");
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> userResponse = checkValid(username, Const.CHECK_TYPE.USERNAME);
        if (userResponse.isSuccessful()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String qustion = userMapper.selectQuestion(username);
        if (StringUtils.isNotBlank(qustion)) {
            return ServerResponse.createBySuccess(qustion);
        }
        return ServerResponse.createByErrorMessage("密码忘记问题没有设置过");
    }

    public ServerResponse<String> checkAnswer(String usrname, String question, String answer) {
        int answerCount = userMapper.checkAnswer(usrname, question, answer);
        if (answerCount > 0) {
            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + usrname, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("忘记密码问题答案错误");
    }

    @Override
    public ServerResponse<String> resetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误 token需要传递");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.equals(token, forgetToken)) {
            //更改密码
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int updateCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (updateCount > 0)
                return ServerResponse.createBySuccessMessage("密码修改成功");
            else
                return ServerResponse.createByErrorMessage("密码修改失败");

        }
        return ServerResponse.createByErrorMessage("token 过期");

    }


    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user) {

        int passwordCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (passwordCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码不存在");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码修改成功");
        }

        return ServerResponse.createByErrorMessage("密码修改失败");
    }


    @Override
    public ServerResponse<User> updateInformation(User newUser) {
        //检查email是否存在
        int emailCount = userMapper.checkEmailByUserId(newUser.getEmail(), newUser.getId());
        if (emailCount > 0) {
            return ServerResponse.createByErrorMessage("Email已经存在 请输入另外一个Email");
        }

        User user = new User();
        user.setId(newUser.getId());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setQuestion(newUser.getQuestion());
        user.setAnswer(newUser.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("个人信息更新失败");
    }

    @Override
    public ServerResponse<User> getInformation(int userID) {
        User user = userMapper.selectByPrimaryKey(userID);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> checkRoleAdmin(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
