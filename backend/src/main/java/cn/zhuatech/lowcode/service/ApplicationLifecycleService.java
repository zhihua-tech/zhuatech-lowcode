/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.service;

import cn.zhuatech.lowcode.model.*;
import cn.zhuatech.lowcode.repository.*;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class ApplicationLifecycleService {
    private final ApplicationVersionRepository versions;private final AuditLogRepository audits;
    public ApplicationLifecycleService(ApplicationVersionRepository versions,AuditLogRepository audits){
        this.versions=versions;this.audits=audits;
    }
    public List<ApplicationVersion> list(){return versions.findAllByOrderByUpdatedAtDesc();}

    @Transactional
    public ApplicationVersion create(CreateRequest r){
        if(versions.existsByAppCodeAndVersionNo(r.appCode(),r.versionNo()))throw conflict("应用版本已存在");
        var item=versions.save(new ApplicationVersion(r.appCode(),r.versionNo(),r.name(),r.pageCount(),
            r.workflowCount(),r.validationErrors(),r.unresolvedDependencies(),r.testCoverage(),
            r.criticalSecurityFindings(),r.ownerAssigned(),r.permissionsReviewed(),r.rollbackSnapshotReady()));
        audit("创建应用版本",item,r.name());return item;
    }

    public PublishGate gate(Long id){
        var item=get(id);List<String> blockers=new ArrayList<>();
        if(item.getPageCount()==0)blockers.add("至少需要一个可发布页面");
        if(item.getValidationErrors()>0)blockers.add("存在模型或页面校验错误");
        if(item.getUnresolvedDependencies()>0)blockers.add("存在未解决连接器依赖");
        if(item.getTestCoverage()<70)blockers.add("自动化测试覆盖率低于70%");
        if(item.getCriticalSecurityFindings()>0)blockers.add("存在严重安全问题");
        if(!item.isOwnerAssigned())blockers.add("未指定应用负责人");
        if(!item.isPermissionsReviewed())blockers.add("权限矩阵未复核");
        if(!item.isRollbackSnapshotReady())blockers.add("未生成回滚快照");
        return new PublishGate(blockers.isEmpty()?"READY":"BLOCKED",item.getAppCode(),
            item.getVersionNo(),List.copyOf(blockers));
    }

    @Transactional
    public ApplicationVersion submit(Long id){
        var item=get(id);require(item,"DRAFT","只有草稿版本可以提交");
        if(!"READY".equals(gate(id).decision()))throw conflict("发布门禁未通过");
        item.submit();audit("提交应用验收",item,"发布门禁通过");return item;
    }

    @Transactional
    public ApplicationVersion publish(Long id,String remark){
        var item=get(id);require(item,"PENDING_REVIEW","只有待验收版本可以发布");
        versions.findByAppCodeAndState(item.getAppCode(),"PUBLISHED").forEach(old->{
            old.archive();audit("归档旧版本",old,"由 "+item.getVersionNo()+" 替代");
        });
        item.publish();audit("发布应用版本",item,remark);return item;
    }

    @Transactional
    public ApplicationVersion rollback(Long id,String reason){
        var item=get(id);require(item,"PUBLISHED","只有当前已发布版本允许回退");
        if(!item.isRollbackSnapshotReady())throw conflict("没有可用回滚快照");
        item.rollback();audit("回退应用版本",item,reason);return item;
    }

    public Dashboard dashboard(){return new Dashboard(versions.count(),versions.countByState("DRAFT"),
        versions.countByState("PENDING_REVIEW"),versions.countByState("PUBLISHED"),
        versions.findAll().stream().filter(v->"BLOCKED".equals(gate(v.getId()).decision())).count());}

    private ApplicationVersion get(Long id){return versions.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"应用版本不存在"));}
    private void require(ApplicationVersion item,String state,String message){if(!state.equals(item.getState()))throw conflict(message);}
    private ResponseStatusException conflict(String message){return new ResponseStatusException(HttpStatus.CONFLICT,message);}
    private void audit(String action,ApplicationVersion item,String detail){
        var auth=SecurityContextHolder.getContext().getAuthentication();
        audits.save(new AuditLog("LOWCODE",action,item.getAppCode()+"/"+item.getVersionNo(),
            auth==null?"system":auth.getName(),detail));
    }

    public record CreateRequest(@NotBlank @Size(max=50) String appCode,@NotBlank @Size(max=30) String versionNo,
        @NotBlank @Size(max=120) String name,@Positive int pageCount,@PositiveOrZero int workflowCount,
        @PositiveOrZero int validationErrors,@PositiveOrZero int unresolvedDependencies,
        @DecimalMin("0") @DecimalMax("100") double testCoverage,@PositiveOrZero int criticalSecurityFindings,
        boolean ownerAssigned,boolean permissionsReviewed,boolean rollbackSnapshotReady){}
    public record PublishGate(String decision,String appCode,String versionNo,List<String> blockers){}
    public record Dashboard(long total,long draft,long pendingReview,long published,long blocked){}
}
