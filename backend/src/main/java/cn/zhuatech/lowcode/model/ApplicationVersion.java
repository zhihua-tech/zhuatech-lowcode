/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity
@Table(name="lowcode_application_versions",uniqueConstraints=@UniqueConstraint(columnNames={"appCode","versionNo"}))
public class ApplicationVersion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=50) private String appCode;
    @Column(nullable=false,length=30) private String versionNo;
    @Column(nullable=false,length=120) private String name;
    private int pageCount;
    private int workflowCount;
    private int validationErrors;
    private int unresolvedDependencies;
    private double testCoverage;
    private int criticalSecurityFindings;
    private boolean ownerAssigned;
    private boolean permissionsReviewed;
    private boolean rollbackSnapshotReady;
    @Column(nullable=false,length=71) private String packageDigest;
    @Column(nullable=false,length=20) private String promotedEnvironment;
    @Column(nullable=false,length=30) private String state;
    @Version private long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected ApplicationVersion(){}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ApplicationVersion(String appCode,String versionNo,String name,int pageCount,int workflowCount,
            int validationErrors,int unresolvedDependencies,double testCoverage,int criticalSecurityFindings,
            boolean ownerAssigned,boolean permissionsReviewed,boolean rollbackSnapshotReady,String packageDigest){
        this.appCode=appCode;this.versionNo=versionNo;this.name=name;this.pageCount=pageCount;
        this.workflowCount=workflowCount;this.validationErrors=validationErrors;
        this.unresolvedDependencies=unresolvedDependencies;this.testCoverage=testCoverage;
        this.criticalSecurityFindings=criticalSecurityFindings;this.ownerAssigned=ownerAssigned;
        this.permissionsReviewed=permissionsReviewed;this.rollbackSnapshotReady=rollbackSnapshotReady;
        this.packageDigest=packageDigest;this.promotedEnvironment="DEV";this.state="DRAFT";
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PrePersist void created(){createdAt=updatedAt=LocalDateTime.now();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PreUpdate void updated(){updatedAt=LocalDateTime.now();}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void promoteTest(){promotedEnvironment="TEST";}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void submit(){state="PENDING_REVIEW";} /**
                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                   */
public void publish(){state="PUBLISHED";promotedEnvironment="PRODUCTION";}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void archive(){state="ARCHIVED";} /**
                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                              */
public void rollback(){state="ROLLED_BACK";}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Long getId(){return id;} /**
                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                     */
public String getAppCode(){return appCode;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getVersionNo(){return versionNo;} /**
                                                     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                     */
public String getName(){return name;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public int getPageCount(){return pageCount;} /**
                                                  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                  */
public int getWorkflowCount(){return workflowCount;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public int getValidationErrors(){return validationErrors;} /**
                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                */
public int getUnresolvedDependencies(){return unresolvedDependencies;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public double getTestCoverage(){return testCoverage;} /**
                                                           * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                           */
public int getCriticalSecurityFindings(){return criticalSecurityFindings;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public boolean isOwnerAssigned(){return ownerAssigned;} /**
                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                             */
public boolean isPermissionsReviewed(){return permissionsReviewed;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public boolean isRollbackSnapshotReady(){return rollbackSnapshotReady;} /**
                                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                             */
public String getState(){return state;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getPackageDigest(){return packageDigest;} /**
                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                             */
public String getPromotedEnvironment(){return promotedEnvironment;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public long getVersion(){return version;} /**
                                               * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                               */
public LocalDateTime getCreatedAt(){return createdAt;}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}
