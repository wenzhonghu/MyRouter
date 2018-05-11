package com.xiaoniu.finance.processor.consts;

/**
 * Created by wenzhonghu on 2018/5/2.
 */

public final class RouterConst {

    public static final String PACKAGE_NAME = "com.xiaoniu.finance.annotation";
    public static final String FILE_PREFIX = "Router_";
    public static final String ROUTER_MANAGER = "RouterManager";
    public static final String ROUTER_MANAGER_PACKAGE = ".routes";
    public static final String ROUTER_MANAGER_METHOD = "setup";

    // Custom interface
    private static final String FACADE_PACKAGE = "com.xiaoniu.finance.router";
    private static final String TEMPLATE_PACKAGE = ".core";
    public static final String IATRACK = FACADE_PACKAGE + TEMPLATE_PACKAGE + ".XnAbstractTrack";

    public static final String XNROUTER = FACADE_PACKAGE + ".XnRouter";
    public static final String XNACTIONCTRL = FACADE_PACKAGE + TEMPLATE_PACKAGE + ".XnRouteMeta";


    // Annotation type
    public static final String ANNOTATION_TYPE_INTECEPTOR = PACKAGE_NAME + ".router.Interceptor";
    public static final String ANNOTATION_TYPE_ROUTE = PACKAGE_NAME + ".router.Router";
    public static final String ANNOTATION_TYPE_PARAM = PACKAGE_NAME + ".router.Param";
    public static final String TYPE_XN_ACTION = PACKAGE_NAME + ".router.Param";
}
