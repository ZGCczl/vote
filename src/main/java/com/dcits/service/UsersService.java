package com.dcits.service;

import com.dcits.pojo.Users;

import java.util.List;

public interface UsersService {

     List<Users> getUsers();

    Users findUserByItcode(String itcode);

    Integer findUserIdByItcode(String itcode);

    Integer addUsers(Users users);

    Integer countUsers();
}
