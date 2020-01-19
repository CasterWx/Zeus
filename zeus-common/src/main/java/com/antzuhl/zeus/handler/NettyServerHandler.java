package com.antzuhl.zeus.handler;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.cache.PropertiesCache;
import com.antzuhl.zeus.entity.request.RpcRequest;
import com.antzuhl.zeus.entity.response.RpcResponse;
import com.antzuhl.zeus.enums.StatusEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

@ChannelHandler.Sharable
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    @Autowired
    PropertiesCache propertiesCache;

    private final Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public void channelActive(ChannelHandlerContext ctx)   {
        log.info("客户端连接成功: {} !", ctx.channel().remoteAddress());
    }

    public void channelInactive(ChannelHandlerContext ctx)   {
        log.info("客户端断开连接!{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)   {
        RpcRequest request = JSON.parseObject(msg.toString(),RpcRequest.class);

        if ("heartBeat".equals(request.getMethodName())) {
            log.info("客户端心跳信息...{} ", ctx.channel().remoteAddress());
        } else {
            log.info("RPC客户端请求接口: {} 方法名: {}", request.getClassName(), request.getMethodName());
            RpcResponse response = new RpcResponse();
            int msgType = request.getType();
            response.setRequestId(request.getId());
            try {
                if (msgType == StatusEnum.REMOTE_INVOKE.code()) {
                    Object result = this.invokeHandler(request);
                    response.setData(result);
                } else if (msgType == StatusEnum.REMOTE_REGISTRY_MSG.code()) {
                    String result = this.registoryHandler(request);
                    response.setData(result);
                } else if (msgType == StatusEnum.REMOTE_PROPER_MSG.code()) {
                    Object result = PropertiesCache.instance().get(request.getClassName());
                    response.setData(result);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                response.setCode(1);
                response.setError_msg(e.toString());
                log.error("RPC Server handle request error",e);
            }
            ctx.writeAndFlush(response);
        }
    }

    /**
     * 通过反射，执行本地方法
     * @param request
     * @return
     * @throws Throwable
     */
    private Object invokeHandler(RpcRequest request) throws Throwable{
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);
        /*
        * 查看调用的接口是不是已经被RpcService注解过
        * */
        if (serviceBean != null){
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(serviceBean, getParameters(parameterTypes,parameters));
        } else {
            throw new Exception("未找到服务接口,请检查配置!:"+className+"#"+request.getMethodName());
        }
    }

    /**
     * 配置注册
     * @param request
     * */
    public String registoryHandler(RpcRequest request) {
        String registoryName = request.getClassName();
        String registoryValue = request.getMethodName();
        PropertiesCache.instance().put(registoryName, registoryValue);
        log.info("RPC客户端请求注册配置:"+request.getClassName()+"   配置值:"+request.getMethodName());
        return request.getClassName();
    }

    /**
     * 获取参数列表
     * @param parameterTypes
     * @param parameters
     * @return
     */
    private Object[] getParameters(Class<?>[] parameterTypes,Object[] parameters){
        if (parameters==null || parameters.length==0){
            return parameters;
        }else{
            Object[] new_parameters = new Object[parameters.length];
            for(int i=0;i<parameters.length;i++){
                new_parameters[i] = JSON.parseObject(parameters[i].toString(),parameterTypes[i]);
            }
            return new_parameters;
        }
    }


    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.ALL_IDLE){
                log.info("客户端已超过60秒未读写数据,关闭连接.{}",ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)   {
        log.info(cause.getMessage());
        ctx.close();
    }
}