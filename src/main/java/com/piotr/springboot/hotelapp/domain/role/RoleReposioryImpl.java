package com.piotr.springboot.hotelapp.domain.role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleReposioryImpl implements RoleRepository {
    @Autowired
    private EntityManager entityManager;

    public RoleReposioryImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public Role findRoleByName(String name) {

        // retrieve/read from database using name
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where name=:name", Role.class);
        theQuery.setParameter("name", name);

        Role role = null;

        try {
            role = theQuery.getSingleResult();
        } catch (Exception e) {
            role = null;
        }

        return role;
    }
}
