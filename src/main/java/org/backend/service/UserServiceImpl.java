package org.backend.service;

import org.backend.model.User;
import org.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.json.JSONObject;
import org.json.JSONException;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	// Constructor injection of ParentRepository
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> getUsers() {
		List<User> users = userRepository.findAll();
		return users;
	}

	@Override
	public JSONObject getUsers(int page_num) {
		try {
			Pageable pageable = PageRequest.of(page_num, 2, Sort.by("id").ascending());

			int total_num = userRepository.findAll(pageable).getTotalPages();

			// Retrieve a page of parents from the repository, ordered by creation date in
			// descending order
			List<User> pageResult = userRepository.findAll(pageable).getContent();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("total_num", total_num);
			jsonObject.put("users", pageResult);

			return jsonObject;
		} catch (JSONException e) {
			return null;
		}

	}

	@Override
	public User getUserById(Long user_id, int page_num) {
		User user = userRepository.findById(user_id);
		return user;
	}

	@Override
	public User getUserByEmailAndPassword(String email, String Password) {
		List<User> users = userRepository.findByEmail(email);

		// I should add hash compare logic
		if (users.isEmpty())
			return null;
		return users.get(0);
	}

	@Override
	public User getUserByEmail(String email) {
		List<User> users = userRepository.findByEmail(email);

		// I should add hash compare logic
		if (users.isEmpty())
			return null;
		return users.get(0);
	}

	@Override
	public void deleteUser(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUser(String email, String password, String firstName, String lastName) {
		var user = new User(email, password, firstName, lastName);
		userRepository.save(user);
	}
}
