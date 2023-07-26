package com.piotr.springboot.hotelapp.domain.role;

public interface RoleRepository {
    public Role findRoleByName(String name);
}
