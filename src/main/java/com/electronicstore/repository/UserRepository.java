package com.electronicstore.repository;

import com.electronicstore.model.user.User;
import java.util.List;

public interface UserRepository extends Repository<User> {
    User findByUsername(String username);
}
