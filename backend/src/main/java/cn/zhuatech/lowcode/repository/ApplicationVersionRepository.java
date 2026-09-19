/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.lowcode.repository;

import cn.zhuatech.lowcode.model.ApplicationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public interface ApplicationVersionRepository extends JpaRepository<ApplicationVersion,Long> {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    boolean existsByAppCodeAndVersionNo(String appCode,String versionNo);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    List<ApplicationVersion> findAllByOrderByUpdatedAtDesc();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    List<ApplicationVersion> findByAppCodeAndState(String appCode,String state);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    long countByState(String state);
}
