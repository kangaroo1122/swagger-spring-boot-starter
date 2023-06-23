# swagger-spring-boot-starter

knife4j 2.0.5 封装工具包

使用指南可参考：https://blog.csdn.net/Vampire_1122/article/details/126799595

### maven

[![Maven Central](https://img.shields.io/maven-central/v/com.kangaroohy/swagger-spring-boot-starter.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.kangaroohy%22%20AND%20a%3A%swagger-spring-boot-starter%22)

~~~xml
<dependency>
  <groupId>com.kangaroohy</groupId>
  <artifactId>swagger-spring-boot-starter</artifactId>
  <version>${lastVersion}</version>
</dependency>
~~~

### 使用

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
    basic-auth: true
    username: admin # 默认admin
    password: 123321 # 默认123321
```

生产屏蔽

```yaml
kangaroohy:
  swagger:
    prod: true
```

扫描方式分两种（也可共存）

- 基于 Api.class 注解扫描
- 基于包路径扫描

方式一

默认以接口路径中是否有`/pub`来区分接口是否需要认证访问

此时，系统中默认只有两个分组

- 默认分组-需认证
- 默认分组-无认证

对应正则匹配条件如下：

待认证正则: `^.*/pub/.*$`

无需认证正则: `^((?!/pub/).)*$`

方式二

通过配置的包路径去扫描对应接口

可配置分组信息
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

此时，显示的分组会新增以下两个

- 分组一-需认证
- 分组二-无认证

### 分组校验

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