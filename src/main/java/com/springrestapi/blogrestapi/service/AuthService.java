package com.springrestapi.blogrestapi.service;

import com.springrestapi.blogrestapi.payload.LoginDTO;
import com.springrestapi.blogrestapi.payload.RegisterDTO;

public interface AuthService {

	String login(LoginDTO loginDTO);

	String register(RegisterDTO registerDTO);

}
