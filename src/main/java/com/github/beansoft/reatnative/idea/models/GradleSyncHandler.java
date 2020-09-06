package com.github.beansoft.reatnative.idea.models;

import com.github.beansoft.reatnative.idea.utils.LogUtil;
import com.intellij.openapi.project.Project;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by pengwei on 2016/11/25.
 */
public class GradleSyncHandler implements InvocationHandler {

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LogUtil.d("method=" + method.toString() + ";proxy=" + proxy);
        if (method.toString().equals("syncSucceeded")) {
            syncSucceeded(null);
        } else if (method.toString().equals("syncFailed")) {
            syncFailed(null, null);
        }
        return null;
    }

    public void syncSucceeded(Project project) {

    }

    public void syncFailed(Project project, String msg) {

    }
}
