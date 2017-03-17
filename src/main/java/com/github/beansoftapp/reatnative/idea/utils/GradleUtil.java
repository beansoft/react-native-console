package com.github.beansoftapp.reatnative.idea.utils;

//import com.android.tools.idea.gradle.task.AndroidGradleTaskManager;
import com.github.beansoftapp.reatnative.idea.models.GradleSyncHandler;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.project.Project;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by pengwei on 16/9/13.
 */
public final class GradleUtil {

    /**
     * gradle sync
     * 暂时不推荐使用
     *
     * @param project
     * @param handler
     */
    @Deprecated
    private static void startSync(Project project, GradleSyncHandler handler) {
        GradleSyncUtil.startSync(project, handler);
    }

    /**
     * 执行task
     *
     * @param project
     * @param taskName
     * @param args
     * @param listener
     */
    public static void executeTask(Project project, String taskName, String args, ExternalSystemTaskNotificationListener listener) {
//        AndroidGradleTaskManager manager = new AndroidGradleTaskManager();
//        List<String> taskNames = new ArrayList<String>();
//        if (taskName != null) {
//            taskNames.add(taskName);
//        }
//        List<String> vmOptions = new ArrayList<String>();
//        List<String> params = new ArrayList<String>();
//        if (args != null) {
//            params.add(args);
//        }
//        if (listener == null) {
//            listener = new ExternalSystemTaskNotificationListenerAdapter() {};
//        }
//        manager.executeTasks(
//                ExternalSystemTaskId.create(GradleConstants.SYSTEM_ID, ExternalSystemTaskType.EXECUTE_TASK, project),
//                taskNames, project.getBasePath(), null, vmOptions, params, null, listener);
    }

    /**
     * 查找所有的build.gradle文件
     *
     * @param project
     * @return
     */
//    public static Collection<VirtualFile> getAllGradleFile(Project project) {
//        Collection<VirtualFile> collection = FilenameIndex.getVirtualFilesByName(project,
//                GradleConstants.DEFAULT_SCRIPT_NAME, GlobalSearchScope.allScope(project));
//        return collection == null ? Collections.EMPTY_LIST : collection;
//    }





    /**
     * 是否在同步过程中
     *
     * @return
     */
    public static boolean isSyncInProgress(Project project) {
        Class<?> gradleSyncClass = null;
        try {
            gradleSyncClass = Class.forName("com.android.tools.idea.gradle.project.sync.GradleSyncState");
        } catch (ClassNotFoundException e) {
            try {
                gradleSyncClass = Class.forName("com.android.tools.idea.gradle.GradleSyncState");
            } catch (ClassNotFoundException e1) {

            }
        }
        if (gradleSyncClass != null) {
            try {
                Method instance = gradleSyncClass.getMethod("getInstance", Project.class);
                Object value = instance.invoke(null, project);
                Method inProgress = gradleSyncClass.getMethod("isSyncInProgress");
                Object b = inProgress.invoke(value);
                if (b != null && b instanceof Boolean) {
                    return Boolean.valueOf(b.toString());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

//    /**
//     * 项目是否为library库
//     * @param file
//     * @return
//     */
//    public static boolean isLibrary(GradleBuildFile file) {
//        if (file != null) {
//            List plugins = file.getPlugins();
//            if (plugins != null && plugins.size() > 0) {
//                return plugins.contains("com.android.library") || plugins.contains("android-library");
//            }
//        }
//        return true;
//    }
}
