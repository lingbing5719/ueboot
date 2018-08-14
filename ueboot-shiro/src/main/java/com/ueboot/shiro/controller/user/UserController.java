/*
* Copyright (c)  2018
* All rights reserved.
* 2018-08-14 10:47:55
*/
package com.ueboot.shiro.controller.user;

import com.ueboot.core.http.response.Response;
import com.ueboot.shiro.controller.user.vo.*;
import com.ueboot.shiro.entity.User;
import com.ueboot.shiro.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * Created on 2018-08-14 10:47:55
 * @author yangkui
 * @since 2.1.0 by ueboot-generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/platform/user")
public class UserController {

    @Resource
    private UserService userService;


    @RequiresPermissions("user:read")
    @PostMapping(value = "/page")
    public Response<Page<UserResp>> page(@PageableDefault(value = 15, sort = { "id" }, direction = Sort.Direction.DESC)
                                                     Pageable pageable, @RequestBody(required = false) UserFindReq req){
        Page<User> entities = userService.findBy(pageable);
        Page<UserResp> body = entities.map(entity -> {
            UserResp resp = new UserResp();
            BeanUtils.copyProperties(entity, resp);
            return resp;
        });

        return new Response<>(body);
    }


    @RequiresPermissions("user:save")
    @PostMapping(value = "/save")
    public Response<Void> save(@RequestBody UserReq req) {
        User entity = null;
        if (req.getId() == null) {
            entity = new User();
        } else {
            entity = userService.get(req.getId());
        }
        BeanUtils.copyProperties(req, entity);
        userService.save(entity);
        return new Response<>();
    }

    @RequiresPermissions("user:delete")
    @PostMapping(value = "/delete")
    public Response<Void> delete(Long[] id) {
        userService.delete(id);
        return new Response<>();
    }

    @RequiresPermissions("user:read")
    @GetMapping(value = "/{id}")
    public Response<UserResp> get(@PathVariable Long id) {
        User entity = userService.get(id);
        UserResp resp = new UserResp();
        BeanUtils.copyProperties(entity, resp);
        return new Response<>(resp);
    }

}
