package com.piotr.springboot.hotelapp.domain.user;

public interface UserRepository {
    User findByUserName(String userName);

    void save(User user);
}
