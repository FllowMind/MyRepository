package com.kok.utils;

import com.google.gson.Gson;

public class GsonUtil {
	private static Gson gson = new Gson();

    private  GsonUtil() {

    }


    /**
     * ��json����ת���ɶ���
     * @param jsonstring
     * @param cls
     * @param <T>
     * @return
     */
   public  static <T>T toObject(String jsonstring,Class<T> cls){


       T t = gson.fromJson(jsonstring, cls);
       return t;
   }

    /**
     * ������ת����json����
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
