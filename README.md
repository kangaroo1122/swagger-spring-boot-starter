#swagger-spring-boot-starter

knife4j 2.0.5 封装工具包

默认情况下，只需要controller层添加注解 @Api 即可自动扫描接口

若要更新配置信息，查看yml提示

访问地址：http://localhost:{port}/doc.html

```yaml
kangaroohy:
  swagger:
    ......
```

认证访问

```yaml
kangaroohy:
  swagger:
    certifiable: true
    username: admin # 默认admin
    password: 123321 # 默认123321
```

生产屏蔽

```yaml
kangaroohy:
  swagger:
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
public RestResult<String> updateUser(@RequestBody @Validated(ValidGroup.Update.class) UserBO user) {
        return usersService.updateUser(user);
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