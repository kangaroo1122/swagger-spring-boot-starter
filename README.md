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

认证访问（security项目开启此配置会发生一些莫名其妙的问题，建议生产环境关闭swagger访问、shiro项目可正常配置）

```yaml
coctrl:
  swagger:
    groupName: 默认分组
    base-package: com.coctrl.swagger.controller,com.coctrl.admin.controller
    certifiable: true
    username: admin # 默认admin
    password: 123321 # 默认123321
```

生产屏蔽

```yaml
coctrl:
  swagger:
    groupName: 默认分组
    base-package: com.coctrl.swagger.controller,com.coctrl.admin.controller
    prod: true
```

新增或编辑接口，实体分组显示

controller层入参上添加分组信息，如：

新增：ValidGroup.Create.class
```java
public RestResult<String> insertUser(@RequestBody @Validated(ValidGroup.Create.class) UserBO user) {
        return usersService.insertUser(user);
        }
```

编辑：ValidGroup.Update.class
```java
public RestResult<String> insertUser(@RequestBody @Validated(ValidGroup.Update.class) UserBO user) {
        return usersService.insertUser(user);
        }
```

入参实体中：
```java
public class UserBO implements Serializable {
    private static final long serialVersionUID = 5699245096095831445L;

    @ApiModelProperty(value = "ID")
    @Null(groups = ValidGroup.Create.class)
    @NotNull(groups = ValidGroup.Update.class, message = "ID不可为空")
    private Long id;
}
```