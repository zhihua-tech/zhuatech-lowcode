/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.controller;

import cn.zhuatech.lowcode.common.ApiResponse;
import cn.zhuatech.lowcode.model.ApplicationVersion;
import cn.zhuatech.lowcode.service.ApplicationLifecycleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/lowcode/applications")
public class ApplicationLifecycleController {
    private final ApplicationLifecycleService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ApplicationLifecycleController(ApplicationLifecycleService service){this.service=service;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping ApiResponse<List<ApplicationVersion>> list(){return ApiResponse.ok(service.list());}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping ApiResponse<ApplicationVersion> create(@Valid @RequestBody ApplicationLifecycleService.CreateRequest request){return ApiResponse.ok(service.create(request));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/{id}/publish-gate") ApiResponse<ApplicationLifecycleService.PublishGate> gate(@PathVariable Long id){return ApiResponse.ok(service.gate(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/promote-test") ApiResponse<ApplicationVersion> promoteTest(@PathVariable Long id,
        @Valid @RequestBody ApplicationLifecycleService.PromotionRequest request){return ApiResponse.ok(service.promoteTest(id,request));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/{id}/submit") ApiResponse<ApplicationVersion> submit(@PathVariable Long id){return ApiResponse.ok(service.submit(id));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/dashboard") ApiResponse<ApplicationLifecycleService.Dashboard> dashboard(){return ApiResponse.ok(service.dashboard());}
}
