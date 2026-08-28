/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import java.util.regex.Pattern;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationLifecycleApiTests {
    @Autowired MockMvc mvc;

    @Test
    void governedApplicationVersionCanBePublishedAndRolledBack() throws Exception {
        long id=create("APP-SALES","2.0.0",0,0,88,0,true,true,true);
        mvc.perform(get("/api/lowcode/applications/{id}/publish-gate",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").value("READY"));
        promote(id);
        mvc.perform(post("/api/lowcode/applications/{id}/submit",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.state").value("PENDING_REVIEW"));
        mvc.perform(post("/api/admin/lowcode/applications/{id}/publish",id).param("remark","越权")
                .with(httpBasic("operator","operator123"))).andExpect(status().isForbidden());
        mvc.perform(post("/api/admin/lowcode/applications/{id}/publish",id).param("remark","验收通过")
                .with(httpBasic("admin","admin123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.state").value("PUBLISHED"));
        mvc.perform(post("/api/admin/lowcode/applications/{id}/rollback",id).param("reason","生产指标异常")
                .with(httpBasic("admin","admin123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.state").value("ROLLED_BACK"));
    }

    @Test
    void publishGateReportsAllCriticalBlockers() throws Exception {
        long id=create("APP-RISK","0.1.0",5,3,20,2,false,false,false);
        mvc.perform(get("/api/lowcode/applications/{id}/publish-gate",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").value("BLOCKED"))
            .andExpect(jsonPath("$.data.blockers.length()").value(7));
        mvc.perform(post("/api/lowcode/applications/{id}/submit",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isConflict());
        mvc.perform(get("/api/lowcode/applications/dashboard").with(httpBasic("operator","operator123")))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.blocked").isNumber());
    }

    @Test
    void publishingNewVersionArchivesPreviousPublishedVersion() throws Exception {
        long first=create("APP-PORTAL","1.0.0",0,0,85,0,true,true,true);
        submitAndPublish(first);
        long second=create("APP-PORTAL","1.1.0",0,0,90,0,true,true,true);
        submitAndPublish(second);
        mvc.perform(get("/api/lowcode/applications").with(httpBasic("operator","operator123")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[?(@.appCode == 'APP-PORTAL' && @.versionNo == '1.0.0')].state").value("ARCHIVED"))
            .andExpect(jsonPath("$.data[?(@.appCode == 'APP-PORTAL' && @.versionNo == '1.1.0')].state").value("PUBLISHED"));
    }

    @Test
    void promotionRejectsArtifactDriftAndSubmitRequiresTestEnvironment() throws Exception {
        long id=create("APP-DIGEST","1.0.0",0,0,90,0,true,true,true);
        mvc.perform(post("/api/lowcode/applications/{id}/submit",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/lowcode/applications/{id}/promote-test",id).with(httpBasic("operator","operator123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"packageDigest\":\"sha256:bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\"}"))
            .andExpect(status().isConflict());
        promote(id);
        mvc.perform(post("/api/lowcode/applications/{id}/submit",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isOk());
    }

    private void submitAndPublish(long id)throws Exception{
        promote(id);
        mvc.perform(post("/api/lowcode/applications/{id}/submit",id).with(httpBasic("operator","operator123")))
            .andExpect(status().isOk());
        mvc.perform(post("/api/admin/lowcode/applications/{id}/publish",id).param("remark","版本验收通过")
                .with(httpBasic("admin","admin123"))).andExpect(status().isOk());
    }

    private void promote(long id)throws Exception{
        mvc.perform(post("/api/lowcode/applications/{id}/promote-test",id).with(httpBasic("operator","operator123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"packageDigest\":\"sha256:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.promotedEnvironment").value("TEST"));
    }

    private long create(String app,String version,int errors,int dependencies,double coverage,int findings,
            boolean owner,boolean permissions,boolean rollback)throws Exception{
        var result=mvc.perform(post("/api/lowcode/applications").with(httpBasic("operator","operator123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"appCode\":\""+app+"\",\"versionNo\":\""+version
                    +"\",\"name\":\"销售运营应用\",\"pageCount\":8,\"workflowCount\":3,"
                    +"\"validationErrors\":"+errors+",\"unresolvedDependencies\":"+dependencies
                    +",\"testCoverage\":"+coverage+",\"criticalSecurityFindings\":"+findings
                    +",\"ownerAssigned\":"+owner+",\"permissionsReviewed\":"+permissions
                    +",\"rollbackSnapshotReady\":"+rollback
                    +",\"packageDigest\":\"sha256:aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"}"))
            .andExpect(status().isOk()).andReturn();
        var matcher=Pattern.compile("\\\"id\\\":(\\d+)").matcher(result.getResponse().getContentAsString());
        Assertions.assertTrue(matcher.find());return Long.parseLong(matcher.group(1));
    }
}
