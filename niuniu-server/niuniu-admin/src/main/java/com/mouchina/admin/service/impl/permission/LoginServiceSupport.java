package  com.mouchina.admin.service.impl.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.dao.StaticPermissionMapper;
import com.mouchina.admin.dao.StaticRolePermissionMapper;
import com.mouchina.admin.dao.StaticUserRoleMapper;
import com.mouchina.admin.service.api.permission.LoginService;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server.entity.permission.StaticRolePermission;
import com.mouchina.moumou_server.entity.permission.StaticUserRole;

/**
 * Created by douzy on 15/12/8.
 */
@Service
public class LoginServiceSupport implements LoginService {

    @Resource
    StaticUserRoleMapper staticUserRoleMapper;
    @Resource
    StaticPermissionMapper staticPermissionMapper;
    @Resource
    StaticRolePermissionMapper staticRolePermissionMapper;


    @Override
    public List<StaticPermission> getLoginUserPermissionByPhone(Map map) {
        List<StaticPermission> staticPermissionList=new ArrayList<StaticPermission>();
        List<StaticUserRole> staticUserRoles = staticUserRoleMapper.selectList(map);
        if (staticUserRoles!=null) {

            List<String> staticRolePermissions = new ArrayList<String>();

            for (StaticUserRole staticUserRole : staticUserRoles) {
                Map rolePermissionMap = new HashMap();
                rolePermissionMap.put("roleId", staticUserRole.getRoleId());
                List<StaticRolePermission> staticRolePermissionList = staticRolePermissionMapper.selectList(rolePermissionMap);
                if (staticRolePermissionList!=null && staticRolePermissionList.size() > 0) {
                    staticRolePermissions = fullRolePermissionList(staticRolePermissions, staticRolePermissionList);
                }
            }
            staticPermissionList= removeDistinct(staticRolePermissions);
        }
        return  staticPermissionList;
    }
    @Override
    public List<StaticPermission> getAllPermission(){
         return staticPermissionMapper.selectList(null);
    }
    private List<String> fullRolePermissionList(List<String> resultStaticRolePermissions, List<StaticRolePermission> staticRolePermissions) {
        for (StaticRolePermission staticRolePermission : staticRolePermissions) {
            resultStaticRolePermissions.add(staticRolePermission.getPermissionId().toString());
        }
        return resultStaticRolePermissions;
    }
    /**
     * 去除重复权限
     *
     * @param staticRolePermissions
     * @return
     */
    private List<StaticPermission> removeDistinct(List<String> staticRolePermissions) {
        List<StaticPermission> result = new ArrayList<StaticPermission>();
        for (String permissionId : staticRolePermissions) {
            int resu = Collections.frequency(staticRolePermissions, permissionId);
            if (Collections.frequency(staticRolePermissions, permissionId) == 1) {
                StaticPermission staticPermission = staticPermissionMapper.selectByPrimaryKey(Long.valueOf(permissionId));
                if (staticPermission != null) {
                    result.add(staticPermission);
                }
            }
        }
        return result;
    }
}
