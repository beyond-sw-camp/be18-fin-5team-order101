package com.synerge.order101.user.model.service;

import com.synerge.order101.user.model.dto.UpdateProfileRequestDto;
import com.synerge.order101.user.model.dto.UserProfile;
import com.synerge.order101.user.model.dto.UserRegisterRequestDto;
import com.synerge.order101.user.model.entity.User;

public interface UserService {
    User registerUser(UserRegisterRequestDto userRequestDto);

    User getUserByEmail(String email);

    UserProfile getUserProfileById(Long userId);

    boolean checkEmailExists(String email);
}
