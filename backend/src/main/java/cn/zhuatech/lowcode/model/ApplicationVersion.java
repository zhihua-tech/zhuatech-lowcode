/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    protected ApplicationVersion(){}
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
    @PrePersist void created(){createdAt=updatedAt=LocalDateTime.now();}
    @PreUpdate void updated(){updatedAt=LocalDateTime.now();}
    public void promoteTest(){promotedEnvironment="TEST";}
    public void submit(){state="PENDING_REVIEW";} public void publish(){state="PUBLISHED";promotedEnvironment="PRODUCTION";}
    public void archive(){state="ARCHIVED";} public void rollback(){state="ROLLED_BACK";}

    public Long getId(){return id;} public String getAppCode(){return appCode;}
    public String getVersionNo(){return versionNo;} public String getName(){return name;}
    public int getPageCount(){return pageCount;} public int getWorkflowCount(){return workflowCount;}
    public int getValidationErrors(){return validationErrors;} public int getUnresolvedDependencies(){return unresolvedDependencies;}
    public double getTestCoverage(){return testCoverage;} public int getCriticalSecurityFindings(){return criticalSecurityFindings;}
    public boolean isOwnerAssigned(){return ownerAssigned;} public boolean isPermissionsReviewed(){return permissionsReviewed;}
    public boolean isRollbackSnapshotReady(){return rollbackSnapshotReady;} public String getState(){return state;}
    public String getPackageDigest(){return packageDigest;} public String getPromotedEnvironment(){return promotedEnvironment;}
    public long getVersion(){return version;} public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
}
