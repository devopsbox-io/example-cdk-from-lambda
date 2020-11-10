package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketProps;

public class RunCdkLambdaStack extends Stack {
    public RunCdkLambdaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public RunCdkLambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        new Bucket(this, "created-by-cdk-from-lambda", BucketProps.builder()
                .removalPolicy(RemovalPolicy.DESTROY)
                .build());
    }
}
