/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.repository;

import cn.zhuatech.lowcode.model.ApplicationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ApplicationVersionRepository extends JpaRepository<ApplicationVersion,Long> {
    boolean existsByAppCodeAndVersionNo(String appCode,String versionNo);
    List<ApplicationVersion> findAllByOrderByUpdatedAtDesc();
    List<ApplicationVersion> findByAppCodeAndState(String appCode,String state);
    long countByState(String state);
}
