/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.controller;

import cn.zhuatech.lowcode.common.ApiResponse;
import cn.zhuatech.lowcode.model.ApplicationVersion;
import cn.zhuatech.lowcode.service.ApplicationLifecycleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lowcode/applications")
public class ApplicationLifecycleController {
    private final ApplicationLifecycleService service;
    public ApplicationLifecycleController(ApplicationLifecycleService service){this.service=service;}
    @GetMapping ApiResponse<List<ApplicationVersion>> list(){return ApiResponse.ok(service.list());}
    @PostMapping ApiResponse<ApplicationVersion> create(@Valid @RequestBody ApplicationLifecycleService.CreateRequest request){return ApiResponse.ok(service.create(request));}
    @GetMapping("/{id}/publish-gate") ApiResponse<ApplicationLifecycleService.PublishGate> gate(@PathVariable Long id){return ApiResponse.ok(service.gate(id));}
    @PostMapping("/{id}/submit") ApiResponse<ApplicationVersion> submit(@PathVariable Long id){return ApiResponse.ok(service.submit(id));}
    @GetMapping("/dashboard") ApiResponse<ApplicationLifecycleService.Dashboard> dashboard(){return ApiResponse.ok(service.dashboard());}
}
