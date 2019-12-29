package com.antzuhl.zeus.controller;

import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import com.antzuhl.zeus.zkutils.ZResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/v1/node")
public class ServerNodeController {

    @Autowired
    private ServiceRegistry serviceRegistry;

    /**
     * Get namespace list .  Controller介从Cache中获取
     * @return namespace list
     */
    @GetMapping(value = "/server")
    public ZResult<List<ServerNode>> getServerNode(@PathParam(value = "namespace") String namespace) {
        ZResult<List<ServerNode>> result = serviceRegistry.getAllServerNode(namespace);
        if (result == null) {
            result.setCode(-1);
            result.setMessage("Namespace not found node.");
        }
        if (CollectionUtils.isEmpty(result.getData())) {
            result.setMessage("Node Empty.");
        }
        return result;
    }

    @GetMapping(value = "/namespaces")
    public ZResult<List<String>> getNamespaces() {
        ZResult<List<String>> result = serviceRegistry.getAllNamespace();
        if (result.getCode() == 200) {
            result.setMessage("OK");
        }
        return result;
    }
}
