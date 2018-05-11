package com.xiaoniu.finance.processor.consts;

/**
 * Some consts used in processors
 */
public class Consts {
    // System interface
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "android.app.Fragment";
    public static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    public static final String SERVICE = "android.app.Service";
    public static final String PARCELABLE = "android.os.Parcelable";

    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";

    // Log
    public static final String PREFIX_OF_LOGGER = "::Compiler ";

    // Options of processor
    public static final String KEY_MODULE_NAME = "moduleName";
    public static final String KEY_PACKAGE_NAME = "libPackageName";

    //common
    public static final String SEPARATOR = "$$";
}