package com.luoyang.hanfu.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyang.hanfu.common.R;
import com.luoyang.hanfu.entity.User;
import com.luoyang.hanfu.service.UserService;
import com.luoyang.hanfu.vo.UserAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员端 - 用户管理控制器
 */
@Tag(name = "管理员模块 - 用户管理")
@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public R<Page<UserAdminVO>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String userName,
                                     @RequestParam(required = false) String yonghuPhone) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .like(StrUtil.isNotBlank(userName), User::getUsername, userName)
                .or()
                .like(StrUtil.isNotBlank(userName), User::getNickname, userName)
                .like(StrUtil.isNotBlank(yonghuPhone), User::getPhone, yonghuPhone)
                .orderByDesc(User::getCreateTime);

        Page<User> userPage = userService.page(page, queryWrapper);
        
        // 转换为 VO 适配前端
        Page<UserAdminVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserAdminVO> voList = userPage.getRecords().stream().map(user -> {
            UserAdminVO vo = new UserAdminVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setYonghuName(user.getNickname() != null ? user.getNickname() : user.getUsername());
            vo.setYonghuPhoto(user.getAvatar());
            vo.setYonghuPhone(user.getPhone());
            vo.setNewMoney(user.getBalance());
            vo.setStatus(user.getStatus());
            if (user.getCreateTime() != null) {
                vo.setCreateTime(user.getCreateTime().format(DATE_FORMATTER));
            }
            return vo;
        }).collect(Collectors.toList());
        
        voPage.setRecords(voList);
        return R.ok(voPage);
    }

    @Operation(summary = "用户手动充值")
    @PostMapping("/recharge")
    public R<Void> recharge(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        User user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }
        BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
        user.setBalance(currentBalance.add(amount));
        userService.updateById(user);
        return R.ok();
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/status")
    public R<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userService.updateById(user);
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok();
    }
}
