/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.controller;

import cn.zhuatech.lowcode.common.ApiResponse;
import cn.zhuatech.lowcode.model.ApplicationVersion;
import cn.zhuatech.lowcode.service.ApplicationLifecycleService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lowcode/applications")
@Validated
public class ApplicationLifecycleAdminController {
    private final ApplicationLifecycleService service;
    public ApplicationLifecycleAdminController(ApplicationLifecycleService service){this.service=service;}
    @PostMapping("/{id}/publish") ApiResponse<ApplicationVersion> publish(@PathVariable Long id,
        @RequestParam @NotBlank String remark){return ApiResponse.ok(service.publish(id,remark));}
    @PostMapping("/{id}/rollback") ApiResponse<ApplicationVersion> rollback(@PathVariable Long id,
        @RequestParam @NotBlank String reason){return ApiResponse.ok(service.rollback(id,reason));}
}
