package com.hexing.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hexing.common.annotation.DataScope;
import com.hexing.common.constant.UserConstants;
import com.hexing.common.core.domain.entity.SysDept;
import com.hexing.common.core.domain.entity.SysRole;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.common.exception.ServiceException;
import com.hexing.common.utils.MessageUtils;
import com.hexing.common.utils.SecurityUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.spring.SpringUtils;
import com.hexing.system.domain.OdoCodeDTO;
import com.hexing.system.domain.SysPost;
import com.hexing.system.domain.SysUserPost;
import com.hexing.system.domain.SysUserRole;
import com.hexing.system.mapper.*;
import com.hexing.system.service.ISysConfigService;
import com.hexing.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SysDeptMapper deptMapper;

    @Value("${uip.uipTransfer}")
    private String uipTransfer;

    private static final String initPassword = "hx.123";

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    @Override
    public List<String> selectUserByDeptId(List<String> deptId)
    {
        return userMapper.selectUserByDeptId(deptId);
    }
    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName)
    {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException(MessageUtils.message("permission.update.admin"));
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException(MessageUtils.message("permission.user.null"));
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     *
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotNull(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        // 删除用户与角色关联
        userRoleMapper.deleteUserRole(userIds);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPost(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<SysUser> getUserListByRoleCodes(String[] roleCodes) {
        List<SysUser> userList = userMapper.getUserListByRoleCodes(roleCodes);
        return userList;
    }

    @Override
    public Map<Long, SysUser> getUserMapByIds(Long[] userIds) {
        if (ArrayUtils.isNotEmpty(userIds)) {
            List<SysUser> userList = userMapper.getUserListByIds(userIds);
            return userList.stream().collect(Collectors.toMap(SysUser::getUserId, Function.identity(), (n1, n2) -> n1));
        }
        return Collections.emptyMap();
    }

    @Override
    public List<SysUser> getUserByNickName(String nickName) {
        if (StringUtils.isNotBlank(nickName)) {
            List<SysUser> userList = userMapper.getUserByNickName(nickName);
            return userList;
        }
        return Collections.emptyList();
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u", dataType = "4")
    public List<SysUser> selectChildUserList(SysUser sysUser)
    {
        return userMapper.selectUserList(sysUser);
    }


    @Override
    @Transactional
    public void syncDepartmentUserList() {
        String delete = "2";
        String unDelete = "0";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("interfaceCode", "get_department_employee");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uipTransfer, param, String.class);
        String body = responseEntity.getBody();
        JSONObject result = JSON.parseObject(body);
        if (result.getIntValue("code") != 0) {
            throw new ServiceException("获取odoo组织架构信息异常");
        }
        JSONObject dataObj = JSON.parseObject(result.getString("data"));
        List<OdoCodeDTO> odoDeptList = JSON.parseArray(dataObj.getString("dept_data"), OdoCodeDTO.class);
        //获取所有组织
        List<SysDept> currentDeptList = deptMapper.getAllDepartment();
        currentDeptList.forEach(d -> d.setDelFlag(delete));
        for (OdoCodeDTO odoCodeDTO : odoDeptList) {
            long deptCode = Long.parseLong(odoCodeDTO.getCode());
            SysDept deptDO = currentDeptList.stream().filter(d -> d.getDeptId().equals(deptCode)).findFirst().orElse(null);
            if (Objects.nonNull(deptDO)) {
                deptDO.setDelFlag(unDelete);
                deptDO.setDeptName(odoCodeDTO.getName());
                if (StringUtils.isNotBlank(odoCodeDTO.getParent_code())) {
                    deptDO.setParentId(Long.parseLong(odoCodeDTO.getParent_code()));
                }
            } else {
                SysDept sysDept = new SysDept();
                sysDept.setDeptId(deptCode);
                if (StringUtils.isNotBlank(odoCodeDTO.getParent_code())) {
                    sysDept.setParentId(Long.parseLong(odoCodeDTO.getParent_code()));
                }
                sysDept.setDeptName(odoCodeDTO.getName());
                deptMapper.insertDept(sysDept);
            }
        }
        if (!CollectionUtils.isEmpty(currentDeptList)) {
            for (SysDept sysDept : currentDeptList) {
                deptMapper.updateDept(sysDept);
            }
        }
        List<OdoCodeDTO> odoUserList = JSON.parseArray(dataObj.getString("user_data"), OdoCodeDTO.class);
        List<SysUser> currentUserList = userMapper.getAllUserList();
        //只增不减
        for (OdoCodeDTO odoUser : odoUserList) {
            SysUser userDO = currentUserList.stream().filter(u -> u.getUserName().equals(odoUser.getCode())).findFirst().orElse(null);
            if (Objects.isNull(userDO)) {
                SysUser sysUser = new SysUser();
                sysUser.setUserName(odoUser.getCode());
                sysUser.setNickName(odoUser.getName());
                sysUser.setPassword(SecurityUtils.encryptPassword(initPassword));
                if (StringUtils.isNotBlank(odoUser.getDept_code())) {
                    sysUser.setDeptId(Long.parseLong(odoUser.getDept_code()));
                }
                sysUser.setUserType("1");
                sysUser.setStatus("1");
                userMapper.insertUser(sysUser);
            }
        }
    }


    @Override
    public Map<String, SysUser> getUserByUserNames(Set<String> userNames) {
        if (CollectionUtils.isEmpty(userNames)) {
            return Collections.emptyMap();
        }
        String[] userNameArray = userNames.stream().toArray(String[]::new);
        List<SysUser> userList = userMapper.getUserListByUserNames(userNameArray);
        return userList.stream().collect(Collectors.toMap(SysUser::getUserName, Function.identity(), (n1, n2) -> n2));
    }

    @Override
    public SysUser getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }


    @Override
    public List<SysUser> getOnOfficeUserList(String parameter) {
        return userMapper.getOnOfficeUserList(parameter);
    }

    @Override
    public List<SysUser> selectUserAll() {
        List<SysUser> userList = userMapper.selectUserAll();
        userList.forEach(user -> user.setShowName(user.getNickName() + "（" + user.getUserName() + "）"));
        return userList;
    }

    @Override
    public Map<String, SysUser> getUsernameUserObjMap() {
        List<SysUser> allUserList = userMapper.getAllUserList();
        Map<String,SysUser> usernameNicknameMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(allUserList)) {
            for (SysUser sysUser : allUserList) {
                usernameNicknameMap.put(sysUser.getUserName(), sysUser);
            }
        }
        return usernameNicknameMap;
    }

}
