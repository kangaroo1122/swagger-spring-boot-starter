package com.coctrl.document.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 需要认证分组的路径，不包含 pub 路径 - 正则：^((?!pub).)*$ <br>
 * 不需要认证分组的路径，包含 pub - 正则：^.*pub.*$
 *
 * @author kangaroo hy
 * @date 2020/4/25
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties(prefix = "coctrl.swagger")
public class SwaggerProperties {

    /**
     * 是否允许访问
     */
    private Boolean enable = false;

    /**
     * 访问用户名
     */
    private String username = "admin";

    /**
     * 访问密码
     */
    private String password = "123321";

    /**
     * 分组名
     */
    private String groupName = "默认分组";

    /**
     * 扫描包路径，多个用英文逗号（ , ）隔开
     */
    private String basePackage;

    /**
     * 授权header
     */
    private String header = "token";

    /**
     * 项目信息
     */
    private AppInfo appInfo = new AppInfo();

    @Data
    public static class AppInfo {
        /**
         * 接口大标题
         */
        private String title = "后端API";

        /**
         * 具体的描述
         */
        private String description = "后端API文档，version 1.0.0";

        /**
         * 接口版本号
         */
        private String version = "1.0.0";

        /**
         * 服务说明url，服务条款
         */
        private String termsOfServiceUrl = "http//www.coctrl.com";

        /**
         * licence，许可证
         */
        private String license = "coctrl";

        /**
         * license url，许可网址
         */
        private String licenseUrl = "www.coctrl.com";

        /**
         * 接口作者联系方式
         */
        private Contact contact = new Contact();

        @Data
        public static class Contact {
            /**
             * 用户名
             */
            private String name = "重庆格工自动化控制设备有限公司";

            /**
             * 联系地址
             */
            private String url = "www.coctrl.com";

            /**
             * 邮箱
             */
            private String email = "server@coctrl.com";
        }
    }
}
