<p align="center">
<img src="/images/logo.png" alt="Zeus">
</p>

![](https://img.shields.io/badge/zeus-1.0-orange)       ![](https://img.shields.io/badge/license-apache-brightgreen)

### 介绍

Zeus可以用于服务发现，服务治理，负载均衡，服务容错，服务调用，API网关，配置中心。

理念是用最简单的方式使用。

#### 服务注册

![](https://img.shields.io/badge/%40-ZeusRegistry-red)

引入zeus-client，在启动类添加`@ZeusRegistry`标签即可。

* registryName ：命名空间(集群管理)
* zkAddr ：zookeeper地址
* serverName ：服务名称
* serverAddr ：服务注册地址

```java
@SpringBootApplication
@ZeusRegistry(registryName = "user-center", zkAddr = "192.168.124.16:2181",
        serverName = "server-1", serverAddr = "48.89.13.53:8080")
public class ZeusDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeusDemoApplication.class, args);
    }
}
```

#### 服务发现

![](https://img.shields.io/badge/Get-%2Fv1%2Fnode%2Fnamespaces-red)

* http://localhost:5454/v1/node/namespaces

> requestMethod : `Get`

获取所有集群名。

```json
{
  "code":200,
  "message":"OK",
  "data":[
    "user-center2","user-center1","user-center3"
  ]
}
```

![](https://img.shields.io/badge/Get-server-red)

* http://localhost:5454/v1/node/server?namespace=param

> requestMethod : `Get`

获取指定集群下的所有服务节点信息。

> response

```json
{
  "code":200,
  "message":"OK",
  "data":[{
    "nameSpace":"user-center1",
    "serverName":"server-1",
    "data":"126",
    "path":"/registry/user-center1/server-1"
    }]
  }
```


#### 接口监控

![](https://img.shields.io/badge/%40-FlowMonitor-red)

为你的方法或是API添加`@FlowMonitor(name)`注释，你的该方法访问将会得到记录。

```java
@RequestMapping(value = "/hello")
@FlowMonitor(name = "hello")
public String hello() {
    return "Hello! " ;
}
```

