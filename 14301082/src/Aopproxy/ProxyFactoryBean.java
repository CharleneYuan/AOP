package Aopproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactoryBean implements InvocationHandler {

	//代理接口
    private String proxyInterfaces;
    
    //代理目标
    private Object target;
    
    //拦截器
    private String interceptorNames;
    
    //IOC支持
    private BeanFactory factory;

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

    public BeanFactory getFactory() {
        return factory;
    }

    public String getProxyInterfaces() {
        return proxyInterfaces;
    }

    public void setProxyInterfaces(String proxyInterfaces) {
        this.proxyInterfaces = proxyInterfaces;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getInterceptorNames() {
        return interceptorNames;
    }

    public void setInterceptorNames(String interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    //获取代理对象
    public Object getProxyObject(){
    	Object obj = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                target.getClass().getInterfaces(),
                this);
		return obj;
    }

    //调用代理对象方法
	 @Override
	    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		 	if(interceptorNames!=null) {
	            Object advice = factory.getBean(interceptorNames);
	            if ( advice instanceof MethodBeforeAdvice) {
	                ((MethodBeforeAdvice)advice).before(method,args,target);
	                Object result = method.invoke(target,args);
	                return result;
	            }
	        }
	        Object result = method.invoke(target,args);
	        return result;
	    }
}
