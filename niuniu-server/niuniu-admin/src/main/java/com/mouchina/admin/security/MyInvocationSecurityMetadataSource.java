package com.mouchina.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.mouchina.admin.service.api.permission.LoginService;
import com.mouchina.moumou_server.entity.permission.StaticPermission;

/**
 * Created by douzy on 15/11/10.
 */
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
    //tomcat启动时实例化一次 由spring调用
    public MyInvocationSecurityMetadataSource(LoginService loginService){
        this.loginService = loginService;
        loadResourceDefine();
    }
    private LoginService loginService;
    //tomcat开启时加载一次，加载所有url和权限（或角色）的对应关系
    private void loadResourceDefine() {
        if(resourceMap==null) {

            resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
            List<StaticPermission> staticPermissions = this.loginService.getAllPermission();
            for (StaticPermission staticPermission : staticPermissions) {
                Collection<ConfigAttribute> configAttributes =
                        new ArrayList<ConfigAttribute>();
                ConfigAttribute configAttribute =
                        new SecurityConfig(staticPermission.getPermissionCode());
                configAttributes.add(configAttribute);
                resourceMap.put(staticPermission.getPermissionAction()+".html", configAttributes);
            }
        }
    }
    //参数是要访问的url，返回这个url对于的所有权限（或角色）
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 将参数转为url
        FilterInvocation filterInvocation = (FilterInvocation) o;
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            RequestMatcher urlMatcher =new AntPathRequestMatcher(resURL);
            if (urlMatcher.matches(filterInvocation.getHttpRequest())) {
                return resourceMap.get(resURL);
            }
        }
        return null;
    }
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
