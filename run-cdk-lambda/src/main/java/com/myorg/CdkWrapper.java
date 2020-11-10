package com.myorg;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;
import java.util.Map;

public class CdkWrapper implements RequestHandler<Map<String, String>, String> {

    public static final String CDK_OUT_DIR = "/tmp/cdk.out";
    private static final String CDK_COMMAND = "java -cp . ";

    @Override
    public String handleRequest(Map<String, String> input, Context context) {

        runCdk(RunCdkLambdaApp.class);

        return "";
    }

    public static void runCdk(Class<?> cdkClass) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cdk",
                "deploy",
                "--verbose",
                "--output", CDK_OUT_DIR,
                "--app", CDK_COMMAND + cdkClass.getName(),
                "--require-approval", "\"never\""
        );
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException("Cannot start cdk process!", e);
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException("Cdk process interrupted!", e);
        }

        int exitValue = process.exitValue();

        if (exitValue != 0) {
            throw new RuntimeException("Exception while executing CDK!");
        }
    }
}
