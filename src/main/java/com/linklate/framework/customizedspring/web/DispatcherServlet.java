package com.linklate.framework.customizedspring.web;


import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DispatcherServlet
 * @Description
 * @Author Linklate
 * @Date 2024/12/16 23:42
 **/

public class DispatcherServlet extends HttpServlet {
    // 用于储存需要扫描的package列表
    private List<String> packageNames = new ArrayList<>();
    // 用于存储controller的名称与对象的映射关系
    private Map<String,Object> controllerObjs = new HashMap<>();
    // 用于存储controller名称数组列表
    private List<String> controllerNames = new ArrayList<>();
    // 用于存储controller名称与类的映射关系
    private Map<String,Class<?>> controllerClasses = new HashMap<>();
    // 保存自定义的@RequestMapping名称（URL的名称）的列表
    private List<String> urlMappingNames = new ArrayList<>();
    // 保存URL名称与对象的映射关系
    private Map<String,Object> mappingObjs = new HashMap<>();
    // 保存URL与方法的映射关系
    private Map<String,Method> mappingMethods = new HashMap<>();

    private String sContextConfigLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        Refresh();
    }


    //对所有的mappingValues中注册的类进行实例化，默认构造函数
    protected void Refresh() {
        initController();// 初始化 controller
        initMapping();// 初始化 url 映射
    }

    protected void initController() {
        //扫描包，获取所有类名
        this.controllerNames = scanPackages(this.packageNames);

        for (String controllerName : this.controllerNames) {
            Object obj = null;
            Class<?> clz = null;

            try {
                clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName,clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                obj = clz.newInstance();
                this.controllerObjs.put(controllerName, obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        //将以.分隔的包名换成以/分隔的uri
        URI uri = null;
        try {
            uri  =this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.", "/")).toURI();
        } catch (URISyntaxException e) {
        }
        File dir = new File(uri);
        //处理对应文件目录
        for (File file : dir.listFiles()) {//目录下的文件或子目录
            if(file.isDirectory()){
                scanPackage(packageName+"."+file.getName());
            }else{
                String controllerName = packageName +"." +file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    protected void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    //检查所有的方法
                    boolean isRequestMapping =
                            method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping) { //有RequestMapping注解
                        String methodName = method.getName();
                        //建立方法名和URL的映射
                        String urlMapping =
                                method.getAnnotation(RequestMapping.class).value();
                        this.urlMappingNames.add(urlMapping);
                        this.mappingObjs.put(urlMapping, obj);
                        this.mappingMethods.put(urlMapping, method);
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath();
        System.out.println(sPath);
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }

        Object obj = null;
        Object objResult = null;
        try {
            Method method = this.mappingMethods.get(sPath);
            obj = this.mappingObjs.get(sPath);
            objResult = method.invoke(obj);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        response.getWriter().append(objResult.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
