package com.dcits.service.Impl;

import com.dcits.dao.UsersMapper;
import com.dcits.pojo.Users;
import com.dcits.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UsersMapper usersMapper;

    /**
     * 获取user
     * @return
     */
    @Override
    public List<Users> getUsers() {
        return this.usersMapper.findUsers();
    }

    /**
     * 根据itcode获取用户
     * @param itcode
     * @return
     */
    @Override
    public Users findUserByItcode(String itcode) {
        return this.usersMapper.findUserByItcode(itcode);
    }

    /**
     * 根据itcode获取用户ID
     * @param itcode
     * @return
     */
    @Override
    public Integer findUserIdByItcode(String itcode) {
        return this.usersMapper.findUserIdByItcode(itcode);
    }

    /**
     * 添加用户
     * @param users
     * @return
     */
    @Override
    public Integer addUsers(Users users) {
        return this.usersMapper.insertSelective(users);
    }

    @Override
    public Integer countUsers() {
        return this.usersMapper.countUsers();
    }

}
