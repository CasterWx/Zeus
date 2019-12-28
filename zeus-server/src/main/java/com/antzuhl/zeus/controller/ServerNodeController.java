package com.antzuhl.zeus.controller;


import com.antzuhl.zeus.config.ZeusConfig;
import com.antzuhl.zeus.node.ServerNode;
import com.antzuhl.zeus.zkutils.Constant;
import com.antzuhl.zeus.zkutils.ServiceRegistry;
import com.antzuhl.zeus.zkutils.ZResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/v1/zeus/namespaces")
public class ServerNodeController {

    @Autowired
    private ServiceRegistry serviceRegistry;

    /**
     * Get namespace list .  Controller介从Cache中获取
     *
     * @param request  request
     * @param response response
     * @return namespace list
     */
    @GetMapping
    public ZResult<List<ServerNode>> getNamespaces(HttpServletRequest request, HttpServletResponse response) {
        ZResult<List<ServerNode>> rr = new ZResult<List<ServerNode>>();
        rr.setCode(200);
        List <ServerNode> serverNodes = serviceRegistry.getAllServerNode(ZeusConfig.getZkAddr());
        if (!CollectionUtils.isEmpty(serverNodes)){
            rr.setData(serverNodes);
        } else {
            rr.setCode(404);
            rr.setMessage(Constant.NOT_FOUND_NODE);
        }

        return rr;
    }
}
