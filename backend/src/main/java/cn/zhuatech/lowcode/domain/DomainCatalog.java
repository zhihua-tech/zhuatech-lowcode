/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("SUBMIT", new WorkflowAction("SUBMIT", "提交应用验收", List.of("草稿"), "待验收", "OPERATOR"));
        actions.put("APPROVE", new WorkflowAction("APPROVE", "批准应用上线", List.of("待验收"), "待发布", "ADMIN"));
        actions.put("PUBLISH", new WorkflowAction("PUBLISH", "发布应用版本", List.of("待发布"), "已发布", "ADMIN"));
    }
    public String systemName() { return "知华科技企业低代码应用开发平台"; }
    public String scene() { return "应用空间、数据模型、表单、页面、流程、规则、集成、版本、发布与运行治理"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "应用/业务域"; }
    public String amountLabel() { return "应用价值"; }
    public String quantityLabel() { return "组件数量"; }
    public String dueLabel() { return "上线期限"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("APP_SPACE", "应用空间", "管理应用、业务域、负责人、成员和环境"),
            new ModuleDefinition("DATA_MODEL", "数据模型", "设计实体、字段、校验、索引、关系和数据权限"),
            new ModuleDefinition("FORM", "智能表单", "配置布局、字段、联动、校验、权限和移动端适配"),
            new ModuleDefinition("PAGE", "页面设计", "通过组件、主题和数据源构建管理端与门户页面"),
            new ModuleDefinition("WORKFLOW", "流程编排", "配置节点、条件、审批人、超时和撤回规则"),
            new ModuleDefinition("BUSINESS_RULE", "业务规则", "维护表达式、决策表、触发器和服务端校验"),
            new ModuleDefinition("INTEGRATION", "连接器", "连接API、数据库、消息、文件和企业身份平台"),
            new ModuleDefinition("VERSION", "版本管理", "保存草稿、差异、依赖、快照和回滚点"),
            new ModuleDefinition("GOVERNANCE", "发布治理", "执行安全、权限、性能、命名和上线门禁")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
