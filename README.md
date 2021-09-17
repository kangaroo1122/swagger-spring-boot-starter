#swagger-spring-boot-starter

knife4j 2.0.5 封装工具包，只需配置扫描包即可访问，更新配置信息，查看yml提示

访问地址：http://localhost:{port}/doc.html

```yaml
coctrl:
  swagger:
    groupName: 默认分组
    base-package: com.coctrl.swagger.controller
```

多包扫描，英文逗号（ , ）隔开

```yaml
coctrl:
  swagger:
    groupName: 默认分组
    base-package: com.coctrl.swagger.controller,com.coctrl.admin.controller
```

认证访问

```yaml
coctrl:
  swagger:
    groupName: 默认分组
    base-package: com.coctrl.swagger.controller,com.coctrl.admin.controller
    certifiable: true
    username: admin # 默认admin
    password: 123321 # 默认123321
```