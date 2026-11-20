package com.zode.web.login.service.impl;

import com.common.base.BaseService;
import com.zo.platform.sdk.common.Result;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.zo.platform.sdk.common.kit.StrKit;
import com.zo.platform.starter.session.ISessionHolder;
import com.zo.platform.starter.session.UserInfoSession;
import com.zode.web.login.dto.LoginParam;
import com.zode.web.login.dto.LoginResult;
import com.zode.web.login.dto.User;
import com.zode.web.login.dto.UserDetail;
import com.zode.web.login.enums.ErrorCode;
import com.zode.web.login.enums.LoginTypeEnum;
import com.zode.web.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author zhoump
 * @date 2026/10/31
 * @purpose
 */
@Service
public class LoginServiceImpl extends BaseService implements LoginService {

    private final static String SQL_KEY = "user.login.sql.";

    private ISessionHolder sessionHolder;
    private UserInfoSession userInfoSession;

    @Autowired
    public void setSessionHolder(ISessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Autowired
    public void setUserInfoSession(UserInfoSession userInfoSession) {
        this.userInfoSession = userInfoSession;
    }

    @Override
    public Result saveUserInfo(UserDetail userDetail) {
        if (StrKit.isBlank(userDetail.getUserId())){
            // 新增用户
            Record save = new Record();
            userDetail.setUserId(StrKit.uuid());
            Map<String, Object> column = userDetail.parseMap();
            save.setColumns(column);
            save.set("created_time",new Date());
            save.set("updated_time",new Date());
            save.set("deleted","0");
            Db.save("zo_platform_user",save);
        }else{
            // 更新用户
            Record update = new Record();
            Map<String, Object> column = userDetail.parseMap();
            update.setColumns(column);
            update.set("created_time",new Date());
            update.set("updated_time",new Date());
            update.set("deleted","0");
            Db.update("zo_platform_user","zo_platform_user_id",update);
        }
        return success().setData((User)userDetail);
    }

    /**
     * 用户登录
     *
     * @param loginParam
     * @return
     */
    @Override
    public Result userLogin(LoginParam loginParam) {
        if (!loginParam.verifyParam()){
            return error().setMessage("缺少登录参数");
        }
        String loginType = loginParam.getLoginType();
        User user;
        if (LoginTypeEnum.ACCOUNT_LOGIN.getCode().equals(loginType)){
            String loginName = loginParam.getLoginName();
            String password = loginParam.getPassword();
            user = getLoginUserInfo(loginName,password);
        } else if (LoginTypeEnum.WECHAT_LOGIN.getCode().equals(loginType)) {
            String openId = loginParam.getOpenId();
            user = getLoginUserInfo(openId);
        }else if (LoginTypeEnum.SMS.getCode().equals(loginType)) {
            // 手机短信验证方式...暂不支持
            user = null;
        }else{
            return error().setMessage("不明确的登录方式");
        }
        if (user == null){
            // 用户名错误
            ErrorCode.USER_LOGIN_MISS.doThrow();
        }
        LoginResult loginResult = this.loginHandler(loginParam, user);
        return success().setData(loginResult);
    }

    private LoginResult loginHandler(LoginParam loginParam, User user) {
        String token = loginHandler(user, loginParam.getAppKey());
        return LoginResult.build(user, token);
    }


    /**
     * 登录处理
     *
     * @param user   用户信息
     * @param appKey appKey
     * @return userToken
     */
    private String loginHandler(User user, String appKey) {
        //构建用户session信息。
        String token = sessionHolder.newToken(user.getUserId());
        userInfoSession.setUser(user);
        userInfoSession.setUserId(user.getUserId());
        userInfoSession.setUserName(user.getName());
        return token;
    }

    private User getLoginUserInfo(String loginName, String password) {
        Record userRecord = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryUserByLoginName", Kv.by("loginName", loginName).set("password", password)));
        if (userRecord == null){
            return null;
        }
        User user = User.fromRecord(userRecord);
        return user;
    }

    private User getLoginUserInfo(String openId) {
        Record userRecord = Db.findFirst(Db.getSqlPara(SQL_KEY + "queryUserByOpenId", Kv.by("openId", openId)));
        if (userRecord == null){
            return null;
        }
        User user = User.fromRecord(userRecord);
        return user;
    }

    /**
     * 用户登出
     *
     * @return
     */
    @Override
    public Result userLogout() {
        sessionHolder.clear();
        return success();
    }
}
