/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { int score=100;List<String> actions=new ArrayList<>();if(request.validationErrors()>0){score-=Math.min(35,request.validationErrors()*5);actions.add("修复模型和页面校验错误");}if(request.unresolvedDependencies()>0){score-=Math.min(30,request.unresolvedDependencies()*8);actions.add("解决应用依赖和连接器问题");}if(request.testCoverage()<70){score-=25;actions.add("补齐关键业务自动化测试");}if(request.criticalSecurityFindings()>0){score-=60;actions.add("阻断严重安全问题应用发布");}if(!request.ownerAssigned()){score-=20;actions.add("指定应用负责人");}if(!request.permissionsReviewed()){score-=35;actions.add("复核角色和数据权限矩阵");}if(!request.rollbackSnapshotReady()){score-=25;actions.add("生成可用回滚快照");}return result(score,actions,"PUBLISH","REMEDIATE","BLOCKED",Map.of("testCoverage",request.testCoverage(),"validationErrors",request.validationErrors(),"unresolvedDependencies",request.unresolvedDependencies(),"securityFindings",request.criticalSecurityFindings())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String appCode,
        @PositiveOrZero int validationErrors,
        @PositiveOrZero int unresolvedDependencies,
        @DecimalMin("0") @DecimalMax("100") double testCoverage,
        @PositiveOrZero int criticalSecurityFindings,
        boolean ownerAssigned,
        boolean permissionsReviewed,
        boolean rollbackSnapshotReady) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
