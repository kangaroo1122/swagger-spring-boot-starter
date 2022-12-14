package com.kangaroohy.swagger.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 需要认证分组的路径，不包含 pub 路径<br>
 * 不需要认证分组的路径，包含 pub<br>
 * 可自定义配置，未生效时，可尝试用英文单引号将内容引起来
 * @author kangaroo hy
 * @date 2020/4/25
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties(prefix = "kangaroohy.swagger")
public class SwaggerProperties {

    /**
     * 是否生产环境
     */
    private Boolean prod = false;

    /**
     * 是否添加认证访问
     */
    private Boolean certifiable = false;

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
     * 授权header
     */
    private String header = "token";

    /**
     * 待认证 路径正则<br/>
     * 配置未生效时，可尝试用英文单引号将内容引起来
     */
    private String authc = "^((?!/pub/).)*$";

    /**
     * 无认证 路径正则<br/>
     * 配置未生效时，可尝试用英文单引号将内容引起来
     */
    private String anon = "^.*/pub/.*$";

    /**
     * 分组信息
     */
    private List<Group> group = new ArrayList<>();

    /**
     * 项目信息
     */
    private AppInfo appInfo = new AppInfo();

    @Data
    public static class Group {
        /**
         * 分组名称
         */
        private String groupName;

        /**
         * 扫描路径
         */
        private String apis;

        /**
         * 分组是否需要验证
         */
        private Boolean certifiable = false;
    }

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
        private String termsOfServiceUrl = "";

        /**
         * licence，许可证
         */
        private String license = "";

        /**
         * license url，许可网址
         */
        private String licenseUrl = "";

        /**
         * 接口作者联系方式
         */
        private Contact contact = new Contact();

        @Data
        public static class Contact {
            /**
             * 用户名
             */
            private String name = "";

            /**
             * 联系地址
             */
            private String url = "";

            /**
             * 邮箱
             */
            private String email = "";
        }
    }
}
