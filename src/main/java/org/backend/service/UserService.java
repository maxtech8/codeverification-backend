package org.backend.service;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;

import java.util.List;
import org.backend.model.User;

public interface UserService {
    JSONObject getUsers(int page_num);
    List<User> getUsers();
    User getUserById(Long user_id, int page_num);
    void deleteUser(int id);
	void addUser(String email, String password, String firstName, String lastName);
	User getUserByEmailAndPassword(String email, String password);
	User getUserByEmail(String email);
}
