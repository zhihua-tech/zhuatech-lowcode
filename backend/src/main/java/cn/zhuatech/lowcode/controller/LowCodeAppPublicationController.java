/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.controller;

import cn.zhuatech.lowcode.common.ApiResponse;
import cn.zhuatech.lowcode.service.LowCodeAppPublicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/lowcode")
public class LowCodeAppPublicationController {
    private final LowCodeAppPublicationService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LowCodeAppPublicationController(LowCodeAppPublicationService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/app-publication")
    public ApiResponse<LowCodeAppPublicationService.Assessment> assess(
            @Valid @RequestBody LowCodeAppPublicationService.Request request) {
        return ApiResponse.ok(service.assess(request));
    }
}
