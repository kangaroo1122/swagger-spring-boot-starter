package com.kangaroohy.swagger.configuration;

import com.kangaroohy.enums.PathSelectorsType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
@ConfigurationProperties(prefix = "kangaroohy.swagger")
public class SwaggerProperties {

    /**
     * 是否生产环境
     */
    private Boolean prod = false;

    /**
     * 是否添加认证访问
     */
    private Boolean basicAuth = false;

    /**
     * 访问用户名
     */
    private String username = "admin";

    /**
     * 访问密码
     */
    private String password = "123321";

    /**
     * 授权header
     */
    private String authHeader = "token";

    /**
     * 是否开启默认分组
     */
    private Boolean enableDefault = true;

    /**
     * 扫描路径，全局配置，分组不配置时，使用此配置
     */
    private String apis;

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
        private String groupName = "默认分组";

        /**
         * 扫描路径，与paths配合，会同时生效，也可单独配置
         */
        private String apis;

        /**
         * 路径选择器，默认为包下任意路径
         */
        private PathSelectorsType pathSelectors = PathSelectorsType.ANY;

        /**
         * 扫描路径配置，正则 或者 ant 格式路径都可以，配合 pathSelectors 使用
         */
        private String paths;

        /**
         * 分组是否需要验证请求头
         */
        private Boolean auth = false;

        /**
         * 生效环境，比如生产环境不希望某个分组展示
         */
        private String[] profilesActive = new String[0];
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
