package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by bu_dong on 2017/7/20.
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse register(User user);

    ServerResponse<String> checkValid(String checkData, String checkType);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String usrname, String question, String answer);

    ServerResponse<String> resetPassword(String username, String newPassword, String forgetToken);

    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

    ServerResponse<User> updateInformation(User newUser);

    ServerResponse<User> getInformation(int userID);

    public ServerResponse<String> checkRoleAdmin(User user);

 }
