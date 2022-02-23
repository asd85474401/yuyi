package com.kcidea.erms.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.Md5Util;
import com.kcidea.erms.common.util.RsaUtil;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.common.util.ValidationUtil;
import com.kcidea.erms.dao.fund.PayRemindRelDao;
import com.kcidea.erms.dao.user.RoleDao;
import com.kcidea.erms.dao.user.UserDao;
import com.kcidea.erms.domain.fund.PayRemindRel;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.user.UserManageModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.ers.SchoolService;
import com.kcidea.erms.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private SchoolService schoolService;

    @Resource
    private PayRemindRelDao payRemindRelDao;

    /**
     * 根据帐号查询用户
     *
     * @param account 帐号
     * @return 用户
     * @author huxubin
     * @date 2021/11/11 13:49
     */
    @Override
    public User findOneByAccount(String account) {
        return userDao.findOneByAccount(account);
    }

    /**
     * 保存登录的信息
     *
     * @param user 用户
     * @param ip   ip地址
     * @return 保存的后的用户对象
     * @author huxubin
     * @date 2021/11/10 17:04
     */
    @Override
    public UserModel saveLoginData(User user, String ip) {

        LocalDateTime now = LocalDateTime.now();

        //创建对象
        UserModel userModel = new UserModel().create(user, null, now, ip);
        userModel.setStartYear(schoolService.findStartYearBySid(user.getSId()));

        //生成uuid
        String uuid = UUID.randomUUID().toString().replace(Constant.SplitChar.LINE_CHAR, "");
        userModel.setUuid(uuid);

        //生成token
        String token = this.createToken(uuid);
        userModel.setToken(token);

        //redisKey
        String redisKey = Constant.RedisKey.TOKEN_REDIS_KEY.concat(uuid);
        //token,存入加密前的内容，admin信息存入redis
        RedisUtil.setValueAndTime(redisKey, JSON.toJSONString(userModel), Constant.RedisTime.ONE_DAY);

        //更新登录次数和最后登录时间
        user.setLoginCount(user.getLoginCount() + 1).setLastLoginTime(now).setUpdatedTime(now);
        userDao.updateById(user);

        return userModel;
    }

    /**
     * 登录后的用户存入redis里面
     *
     * @param tokenKey 用户id
     * @return token信息
     * @author huxubin
     * @date 2021/11/10 17:12
     */
    private String createToken(String tokenKey) {
        //加密后的token
        String encryptToken = "";
        try {
            encryptToken = RsaUtil.encryptDefaultKey(tokenKey);
        } catch (Exception e) {
            log.error("加密失败，加密内容" + tokenKey + "，原因：" + e.getMessage());
        }

        return encryptToken;
    }

    /**
     * 人员管理列表
     *
     * @param nickName    姓名
     * @param roleId      角色id
     * @param disableFlag 禁用标识
     * @param sid         学校id
     * @param pageNum     页码
     * @param pageSize    每页数量
     * @return 人员管理列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public PageResult<UserManageModel> findUserManageList(String nickName, Long roleId, Long disableFlag, Long sid,
                                                          Long userId, Integer pageNum, Integer pageSize) {
        // 校验分页参数
        super.checkPageParam(pageNum, pageNum);

        // 校验参数
        super.checkSid(sid);

        Page<UserManageModel> page = new Page<>(pageNum, pageSize);

        // 查询参数
        List<UserManageModel> list =
                userDao.findListByNickNameRoleIdDisableFlagSidPage(nickName, roleId, disableFlag, sid, page);

        // 循环判断是否显示操作按钮
        for (UserManageModel model : list) {
            if (userId.equals(model.getId())) {
                model.setMyFlag(1);
                continue;
            }
            model.setMyFlag(0);
        }

        PageResult<UserManageModel> result = new PageResult<>();
        result.success(list, page.getTotal());
        return result;
    }


    /**
     * 根据学校id，查询角色下拉
     *
     * @param sid 学校id
     * @return 角色下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public List<IdNameModel> findRoleSelectListBySid(Long sid) {
        // 校验参数
        super.checkSid(sid);
        List<IdNameModel> list = Lists.newArrayList();
        list.add(new IdNameModel().create(Constant.ALL_INT_VALUE.longValue(), "全部"));
        list.addAll(roleDao.findIdNameListBySid(sid));
        return list;
    }

    /**
     * 查询禁用标识下拉
     *
     * @return 禁用标识下拉列表
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public List<IdNameModel> findDisableFlagSelectList() {
        List<IdNameModel> result = Lists.newArrayList();
        result.add(new IdNameModel().create(Constant.ALL_INT_VALUE.longValue(), "全部"));
        result.add(new IdNameModel().create((long) EnumTrueFalse.是.getValue(), "已禁用"));
        result.add(new IdNameModel().create((long) EnumTrueFalse.否.getValue(), "未禁用"));
        return result;
    }

    /**
     * 编辑账户禁用状态
     *
     * @param userId      用户id
     * @param disableFlag 禁用标识
     * @param adminId     操作用户
     * @param sid         学校id
     * @return 更新的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public String updateUserDisableFlagByUserIdAdminSid(Long userId, Integer disableFlag, Long adminId, Long sid) {
        super.checkSid(sid);
        if (userId == null || disableFlag == null || adminId == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        if (disableFlag != 0 && disableFlag != 1) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        User user = userDao.findOneByUserIdSid(sid, userId);
        if (user == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }
        if (userId.equals(adminId)) {
            throw new CustomException(Vm.ERROR_REQUEST);
        }
        user.setDisableFlag(disableFlag);
        user.setUpdatedBy(adminId);
        user.setUpdatedTime(LocalDateTime.now());
        userDao.updateById(user);

        // 同步更新 支付提醒关系表 中该用户的禁用标识
        PayRemindRel payRemindRel = payRemindRelDao.findOneByUserIdSid(sid, userId);
        if (payRemindRel != null) {
            payRemindRel.setDisableFlag(disableFlag);
            payRemindRelDao.updateById(payRemindRel);
        }

        // 如果是禁用此用户，就要清除此用户的登录缓存
        if (EnumTrueFalse.是.getValue() == disableFlag) {
            RedisUtil.deleteByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(Long.toString(userId)));
        }

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 删除账号
     *
     * @param userId  用户id
     * @param adminId 操作人id
     * @param sid     学校id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public String deleteUserByUserId(Long userId, Long adminId, Long sid) {
        super.checkSid(sid);
        if (userId == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 查询 支付提醒关系表 中是否有该用户的支付记录
        if (payRemindRelDao.findCountByUserIdSid(sid, userId) != 0) {
            throw new CustomException("很抱歉，该用户已在付款提醒人员中，请前去移除后再试");
        }

        User user = userDao.selectById(userId);
        if (user == null) {
            throw new CustomException(Vm.DATA_NOT_EXIST);
        }
        if (userId.equals(adminId)) {
            throw new CustomException(Vm.ERROR_REQUEST);
        }

        userDao.deleteById(user);

        // 清除此用户登录缓存
        RedisUtil.deleteByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(Long.toString(userId)));

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 新增用户
     *
     * @param accountName 登录帐号
     * @param password    密码
     * @param nickName    昵称
     * @param roleId      角色id
     * @param email       邮箱地址
     * @param department  部门
     * @param sid         学校id
     * @param adminId     操作人id
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public String addUser(String accountName, String password, String nickName, Long roleId, String email,
                          String department, Long sid, Long adminId) {
        // 校验参数
        super.checkSid(sid);

        // 检查参数是否合法
        int actionType = EnumUserAction.新增.getValue();
        checkParams(accountName, password, nickName, roleId, sid, adminId, email, department, actionType);

        // 检查是否有重复的用户名
        if (userDao.findCountBySidAccountName(sid, accountName) != 0) {
            throw new CustomException(Vm.EXIST_ACCOUNT);
        }

        //密码加密,全部大写
        String passwordStr = Md5Util.toMd5(password).toUpperCase();

        User user = new User();
        user.setAccountName(accountName);
        user.setPassword(passwordStr);
        user.setNickName(nickName);
        user.setRoleId(roleId);
        user.setEmail(email);
        user.setDepartment(department);
        user.setDisableFlag(EnumTrueFalse.否.getValue());
        user.setSId(sid);
        user.setCreatedBy(adminId);
        user.setCreatedTime(LocalDateTime.now());
        userDao.insert(user);

        return Vm.INSERT_SUCCESS;
    }

    /**
     * 校验参数是否合法
     *
     * @param accountName 账号名
     * @param password    密码
     * @param nickName    用户名
     * @param roleId      角色id
     * @param sid         学校id
     * @param admin       操作用户
     * @param actionType  操作类型
     * @author majuehao
     * @date 2021/11/12 15:23
     */
    private void checkParams(String accountName, String password, String nickName, Long roleId, Long sid, Long admin,
                             String email, String department, int actionType) {
        // 校验参数
        super.checkSid(sid);

        // 判断参数是否为空
        if (Strings.isNullOrEmpty(accountName) || Strings.isNullOrEmpty(nickName)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        if (admin == null || admin == 0) {
            throw new CustomException(Vm.NO_LOGIN);
        }
        if (roleId == null) {
            throw new CustomException(Vm.ERROR_DROPDOWN);
        }

        // 检验参数格式是否正确
        // 账号名必填，1-32个字符，仅可输入字母、数字和下划线
        if (!ValidationUtil.engNumCha(accountName) || !ValidationUtil.length(accountName,
                Constant.Admin.MAX_ACCOUNT_LENGTH)) {
            throw new CustomException("账号名必须在1-32个字符内，仅可输入字母、数字和下划线");
        }
        //姓名必填，1-100个字符
        if (!ValidationUtil.length(nickName, Constant.Admin.MAX_NICK_NAME_LENGTH)) {
            throw new CustomException("姓名必须在1-100个字符内");
        }
        // 邮箱不为空，校验邮箱格式
        if (!Strings.isNullOrEmpty(email)) {
            if (!ValidationUtil.email(email) || !ValidationUtil.length(email, Constant.Admin.MAX_NICK_NAME_LENGTH)) {
                throw new CustomException(Vm.EMAIL_FIT_ERROR);
            }
        }
        // 部门不为空，校验部门格式
        if (!Strings.isNullOrEmpty(department)) {
            if (!ValidationUtil.length(department, Constant.Admin.MAX_NICK_NAME_LENGTH)) {
                throw new CustomException(Vm.EMAIL_FIT_ERROR);
            }
        }
        //修改操作时，如果密码为空，表示不修改密码
        EnumUserAction enumUserAction = EnumUserAction.getUserAction(actionType);
        switch (enumUserAction) {
            case 修改:
                //判断密码是否为空，空的话表示不修改密码，不用校验;不为空时才需要校验
                if (!Strings.isNullOrEmpty(password)) {
                    //密码必填，6-20个字符，仅可输入英文和数字和符号
                    if (!ValidationUtil.engNumCha(password)
                            || !ValidationUtil.length(password, Constant.Admin.MIN_PASSWORD_LENGTH,
                            Constant.Admin.MAX_PASSWORD_LENGTH)) {
                        throw new CustomException(Vm.ERROR_PASSWORD);
                    }
                }
                break;
            case 新增:
            default:
                if (Strings.isNullOrEmpty(password)) {
                    throw new CustomException(Vm.ERROR_PASSWORD);
                }
                //密码必填，6-20个字符，仅可输入英文和数字和符号
                if (!ValidationUtil.engNumCha(password)
                        || !ValidationUtil.length(password, Constant.Admin.MIN_PASSWORD_LENGTH,
                        Constant.Admin.MAX_PASSWORD_LENGTH)) {
                    throw new CustomException(Vm.ERROR_PASSWORD);
                }
                break;
        }
    }

    /**
     * 编辑用户信息回显
     *
     * @param userId 用户id
     * @return 用户信息
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public UserManageModel findOneUser(Long userId) {
        if (userId == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        // 查询参数
        User user = userDao.selectById(userId);
        if (user == null) {
            throw new CustomException(Vm.NO_DATA);
        }
        String role = "";
        if (user.getRoleId() != null) {
            role = roleDao.findNameById(user.getRoleId());
        }
        UserManageModel model = new UserManageModel();
        model.setId(user.getId());
        model.setAccountName(user.getAccountName());
        model.setNickName(user.getNickName());
        model.setRoleId(user.getRoleId());
        model.setRole(role);
        model.setEmail(user.getEmail());
        model.setDepartment(user.getDepartment());
        return model;
    }


    /**
     * 编辑用户
     *
     * @param userId      用户id
     * @param accountName 登录帐号
     * @param password    密码
     * @param nickName    昵称
     * @param roleId      角色id
     * @param email       邮箱地址
     * @param department  部门
     * @param sid         学校id
     * @param adminId     操作人id
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/12 11:10
     **/
    @Override
    public String updateUser(Long userId, String accountName, String password, String nickName, Long roleId,
                             String email, String department, Long sid, Long adminId) {
        // 校验参数
        super.checkSid(sid);

        // 检查参数是否合法
        int actionType = EnumUserAction.修改.getValue();
        checkParams(accountName, password, nickName, roleId, sid, adminId, email, department, actionType);

        User user = userDao.selectById(userId);
        if (!Objects.equals(user.getAccountName(), accountName)) {
            // 排除自己，检查是否有重复的用户名
            if (userDao.findCountBySidAccountName(sid, accountName) != 0) {
                throw new CustomException(Vm.EXIST_ACCOUNT);
            }
        }

        // 如果密码为空，说明不需要更新
        if (!Strings.isNullOrEmpty(password)) {
            //密码加密,全部大写
            String passwordStr = Md5Util.toMd5(password).toUpperCase();
            user.setPassword(passwordStr);
        }

        user.setAccountName(accountName);
        user.setNickName(nickName);
        user.setRoleId(roleId);
        user.setEmail(email);
        user.setDepartment(department);
        user.setUpdatedBy(adminId);
        user.setUpdatedTime(LocalDateTime.now());
        userDao.updateById(user);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 查询学校未禁用的用户下拉集合
     *
     * @param sid 学校id
     * @return 用户下拉集合
     * @author yeweiwei
     * @date 2021/11/17 10:18
     */
    @Override
    public List<IdNameModel> findUserSelectList(Long sid) {
        // 校验参数
        super.checkSid(sid);

        List<User> userList = userDao.findListBySidDisableFlag(sid, EnumTrueFalse.否.getValue());
        List<IdNameModel> list = Lists.newArrayList();
        for (User user : userList) {
            Long id = user.getId();
            String name = user.getNickName();
            String email = user.getEmail();
            if (!Strings.isNullOrEmpty(email)) {
                name = name.concat("(").concat(email).concat(")");
            }
            IdNameModel idNameModel = new IdNameModel().create(id, name);
            list.add(idNameModel);
        }
        return list;
    }
}
