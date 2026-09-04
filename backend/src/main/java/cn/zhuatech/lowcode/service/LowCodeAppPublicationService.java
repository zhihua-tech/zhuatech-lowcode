/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LowCodeAppPublicationService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.versionFrozen()) blockers.add("待发布版本尚未冻结");
        if (!request.permissionsReviewed()) blockers.add("角色权限矩阵未复核");
        if (!request.connectorSecretsExternalized()) blockers.add("连接器密钥未从应用包外置");
        if (!request.tenantIsolationVerified()) blockers.add("租户数据隔离未验证");
        if (!request.dataMigrationTested()) blockers.add("数据迁移脚本未完成演练");
        if (!request.securityScanPassed()) blockers.add("应用安全扫描未通过");
        if (!request.rollbackReady()) blockers.add("发布回退快照不可用");
        if (!request.publisherSeparated()) blockers.add("开发人与发布人未职责分离");
        if (!request.auditReady()) blockers.add("发布审计资料不完整");
        if (!request.ownerAssigned()) actions.add("指定应用业务负责人");
        if (!request.performanceBudgetMet()) actions.add("补齐页面性能预算与基准结果");
        if (!request.accessibilityReviewed()) actions.add("完成关键页面无障碍检查");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED : !actions.isEmpty() ? Decision.REVIEW : Decision.PUBLISH;
        return new Assessment(request.appId(), decision, List.copyOf(blockers), List.copyOf(actions));
    }

    public record Request(@NotBlank String appId, boolean ownerAssigned, boolean versionFrozen,
                          boolean permissionsReviewed, boolean connectorSecretsExternalized,
                          boolean tenantIsolationVerified, boolean dataMigrationTested,
                          boolean performanceBudgetMet, boolean accessibilityReviewed,
                          boolean securityScanPassed, boolean rollbackReady,
                          boolean publisherSeparated, boolean auditReady) {}
    public record Assessment(String appId, Decision decision, List<String> blockers, List<String> actions) {}
    public enum Decision { PUBLISH, REVIEW, BLOCKED }
}
