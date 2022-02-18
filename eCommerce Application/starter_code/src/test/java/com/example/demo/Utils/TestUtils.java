package com.example.demo.Utils;


import java.lang.reflect.Field;

public class TestUtils {


    public static void injectObjects(Object ObjectTarget, String fieldName , Object ObjectToInject) throws NoSuchFieldException, IllegalAccessException {

        boolean wasPrivate = false;

            Field field = ObjectTarget.getClass().getDeclaredField(fieldName);

            if (!field.canAccess(ObjectTarget)){   // is fieldName an attribute of ObjectTarget ??

                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(ObjectTarget, ObjectToInject);

            if (wasPrivate){
                field.setAccessible(false);
            }

    }



}
