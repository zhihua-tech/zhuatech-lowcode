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

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class ApplicationLifecycleService {
    private final ApplicationVersionRepository versions;private final AuditLogRepository audits;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ApplicationLifecycleService(ApplicationVersionRepository versions,AuditLogRepository audits){
        this.versions=versions;this.audits=audits;
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public List<ApplicationVersion> list(){return versions.findAllByOrderByUpdatedAtDesc();}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public ApplicationVersion create(CreateRequest r){
        if(versions.existsByAppCodeAndVersionNo(r.appCode(),r.versionNo()))throw conflict("应用版本已存在");
        var item=versions.save(new ApplicationVersion(r.appCode(),r.versionNo(),r.name(),r.pageCount(),
            r.workflowCount(),r.validationErrors(),r.unresolvedDependencies(),r.testCoverage(),
            r.criticalSecurityFindings(),r.ownerAssigned(),r.permissionsReviewed(),r.rollbackSnapshotReady(),
            r.packageDigest().toLowerCase(Locale.ROOT)));
        audit("创建应用版本",item,r.name());return item;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
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

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public ApplicationVersion promoteTest(Long id,PromotionRequest request){
        var item=get(id);require(item,"DRAFT","只有草稿版本允许晋级测试环境");
        if(!item.getPackageDigest().equalsIgnoreCase(request.packageDigest()))throw conflict("晋级制品摘要与开发基线不一致");
        if(!"READY".equals(gate(id).decision()))throw conflict("发布门禁未通过，禁止晋级测试环境");
        item.promoteTest();audit("晋级测试环境",item,"制品摘要校验通过");return item;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public ApplicationVersion submit(Long id){
        var item=get(id);require(item,"DRAFT","只有草稿版本可以提交");
        if(!"TEST".equals(item.getPromotedEnvironment()))throw conflict("版本必须先通过测试环境晋级");
        if(!"READY".equals(gate(id).decision()))throw conflict("发布门禁未通过");
        item.submit();audit("提交应用验收",item,"发布门禁通过");return item;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public ApplicationVersion publish(Long id,String remark){
        var item=get(id);require(item,"PENDING_REVIEW","只有待验收版本可以发布");
        versions.findByAppCodeAndState(item.getAppCode(),"PUBLISHED").forEach(old->{
            old.archive();audit("归档旧版本",old,"由 "+item.getVersionNo()+" 替代");
        });
        item.publish();audit("发布应用版本",item,remark);return item;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public ApplicationVersion rollback(Long id,String reason){
        var item=get(id);require(item,"PUBLISHED","只有当前已发布版本允许回退");
        if(!item.isRollbackSnapshotReady())throw conflict("没有可用回滚快照");
        item.rollback();audit("回退应用版本",item,reason);return item;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Dashboard dashboard(){return new Dashboard(versions.count(),versions.countByState("DRAFT"),
        versions.countByState("PENDING_REVIEW"),versions.countByState("PUBLISHED"),
        versions.findAll().stream().filter(v->"BLOCKED".equals(gate(v.getId()).decision())).count());}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ApplicationVersion get(Long id){return versions.findById(id).orElseThrow(()->
        new ResponseStatusException(HttpStatus.NOT_FOUND,"应用版本不存在"));}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private void require(ApplicationVersion item,String state,String message){if(!state.equals(item.getState()))throw conflict(message);}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ResponseStatusException conflict(String message){return new ResponseStatusException(HttpStatus.CONFLICT,message);}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private void audit(String action,ApplicationVersion item,String detail){
        var auth=SecurityContextHolder.getContext().getAuthentication();
        audits.save(new AuditLog("LOWCODE",action,item.getAppCode()+"/"+item.getVersionNo(),
            auth==null?"system":auth.getName(),detail));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record CreateRequest(@NotBlank @Size(max=50) String appCode,@NotBlank @Size(max=30) String versionNo,
        @NotBlank @Size(max=120) String name,@Positive int pageCount,@PositiveOrZero int workflowCount,
        @PositiveOrZero int validationErrors,@PositiveOrZero int unresolvedDependencies,
        @DecimalMin("0") @DecimalMax("100") double testCoverage,@PositiveOrZero int criticalSecurityFindings,
        boolean ownerAssigned,boolean permissionsReviewed,boolean rollbackSnapshotReady,
        @NotBlank @Pattern(regexp="(?i)sha256:[0-9a-f]{64}") String packageDigest){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PromotionRequest(@NotBlank @Pattern(regexp="(?i)sha256:[0-9a-f]{64}") String packageDigest){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PublishGate(String decision,String appCode,String versionNo,List<String> blockers){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Dashboard(long total,long draft,long pendingReview,long published,long blocked){}
}
