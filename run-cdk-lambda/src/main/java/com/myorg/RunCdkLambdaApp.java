package com.myorg;

import software.amazon.awscdk.core.App;

public class RunCdkLambdaApp {
    public static void main(final String[] args) {
        App app = new App();

        new RunCdkLambdaStack(app, "RunCdkLambdaStack");

        app.synth();
    }
}
