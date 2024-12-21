package com.join.lesson.feature.user;

import com.join.lesson.core.common.ApiResponse;
import com.join.lesson.core.common.util.SecurityUtils;
import com.join.lesson.feature.user.dto.UpdateUserRequset;
import com.join.lesson.feature.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserResource {

    private final UserUseCase userUseCase;

    @GetMapping
    public ApiResponse<UserResponse> getUserInfo() {

        return ApiResponse.ok(userUseCase.getUserInfo(SecurityUtils.getUserId()));

    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getProProfile(@PathVariable("uesrId") Long userId) {

        return ApiResponse.ok(userUseCase.getUserInfo(userId));

    }

    @PatchMapping
    public ApiResponse<Long> updateUserInfo(@RequestBody UpdateUserRequset updateUserRequset) {

        return ApiResponse.ok(userUseCase.updateUserInfo(updateUserRequset, SecurityUtils.getUserId()));

    }

}
