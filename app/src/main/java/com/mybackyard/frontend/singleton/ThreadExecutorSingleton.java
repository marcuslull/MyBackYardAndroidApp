package com.mybackyard.frontend.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExecutorSingleton {
    private static final ExecutorService executorSingle= Executors.newSingleThreadExecutor();

    private ThreadExecutorSingleton() {}

    public static ExecutorService getSingleThread() {
        return executorSingle;
    }
}
