## 1.0.4

- 新增`@ApiModelEnum`类注解，用于枚举展示

## 1.0.3

！！！字段调整，与前边的版本不兼容

- 是否开启文档登录访问字段调整：`certifiable` --> `basicAuth`，默认 `false`
- 接口授权header配置字段调整：`header` --> `authHeader`，默认 `token`
- 支持配置全局包路径，不配置则默认扫描 `@Api` 注解
- 分组授权请求头字段调整：`certifiable` --> `auth`，默认 `false`
- api 路径过滤支持 ant 格式配置和正则配置，默认任意路径（any）

```yaml
kangaroohy:
  swagger:
    group:
      - group-name: 分组一 # 分组名称
        apis: com.kangaroohy.controller # 扫描包路径，不配置则会扫描 @Api 注解
        path-selectors: ant # 默认any，任意路径，还支持 ant、regex、none
        paths: /api/**  # 配合 path-selectors 使用
        auth: true  # 此分组是否需要设置授权头
      - group-name: 分组二
        apis: com.kangaroohy.controller
        path-selectors: regex
        paths: '^((?!/pub/).)*$'
        auth: false
```

## 1.0.2

- 默认分组支持关闭配置

## 1.0.1

- 支持自定义分组配置

## 1.0.0

- 支持认证访问、生产屏蔽配置
- 支持实体分组校验显示
- 默认提供两个分组（需认证、无认证）
