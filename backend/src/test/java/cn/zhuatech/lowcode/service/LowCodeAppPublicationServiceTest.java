/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class LowCodeAppPublicationServiceTest {
    private final LowCodeAppPublicationService service = new LowCodeAppPublicationService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void publishesGovernedApplication() {
        var result = service.assess(request(true, true, true));
        assertThat(result.decision()).isEqualTo(LowCodeAppPublicationService.Decision.PUBLISH);
        assertThat(result.blockers()).isEmpty();
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void reviewsApplicationWithImprovementActions() {
        var result = service.assess(request(false, false, false));
        assertThat(result.decision()).isEqualTo(LowCodeAppPublicationService.Decision.REVIEW);
        assertThat(result.actions()).hasSize(3);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksUnsafeApplicationPublication() {
        var result = service.assess(new LowCodeAppPublicationService.Request("APP-003", true, false,
                false, false, false, false, true, true, false, false, false, false));
        assertThat(result.decision()).isEqualTo(LowCodeAppPublicationService.Decision.BLOCKED);
        assertThat(result.blockers()).hasSize(9);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private LowCodeAppPublicationService.Request request(boolean owner, boolean performance, boolean accessibility) {
        return new LowCodeAppPublicationService.Request("APP-001", owner, true, true, true, true, true,
                performance, accessibility, true, true, true, true);
    }
}
