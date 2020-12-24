package com.iels.auth.service;

import com.iels.auth.client.UserClient;
import com.iels.framework.domain.ucenter.IelsMenu;
import com.iels.framework.domain.ucenter.ext.IelsUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: snypxk
 * @return: loadUserByUsername()
 *          返回 Null 或 UserDetails - Spring Security会根据这个返回值进行下一步操作
 *          i: 返回Null Spring Security会抛出用户不存在异常,
 *          ii: 根据UserDetails来进行密码匹配,匹配正确Spring Security则发放令牌,否则不发放.
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用用户中心: 根据账号查询用户信息
        IelsUserExt userext = userClient.getUserext(username);
        if (userext == null) {
            //返回空给 spring security表示用户不存在
            return null;
        }
        //取出正确密码（hash值）
        String password = userext.getPassword();

        //从数据库获取权限
        List<IelsMenu> permissions = userext.getPermissions();
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        List<String> user_permission = new ArrayList<>();
        permissions.forEach(item -> user_permission.add(item.getCode()));
        //user_permission.add("course_get_baseinfo");
        //user_permission.add("course_pic_list");
        String user_permission_string = StringUtils.join(user_permission.toArray(), ",");

        UserJwt userDetails = new UserJwt(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        userDetails.setId(userext.getId());
        userDetails.setUtype(userext.getUtype());           //用户类型
        userDetails.setCompanyId(userext.getCompanyId());   //所属企业
        userDetails.setName(userext.getUsername());         //用户名称
        userDetails.setUserpic(userext.getUserpic());       //用户头像
        return userDetails;
    }
}
