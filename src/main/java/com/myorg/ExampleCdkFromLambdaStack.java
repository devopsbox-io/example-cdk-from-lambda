package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.lambda.Runtime;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class ExampleCdkFromLambdaStack extends Stack {
    public ExampleCdkFromLambdaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public ExampleCdkFromLambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        createRunCdkLambda();
    }

    private void createRunCdkLambda() {
        String tmpBinDir = getTmpBinDir();

        PolicyStatement cloudformationPolicy = new PolicyStatement(PolicyStatementProps.builder()
                .resources(Arrays.asList(
                        "*"
                ))
                .actions(Arrays.asList(
                        "cloudformation:DescribeStacks",
                        "cloudformation:CreateChangeSet",
                        "cloudformation:DescribeChangeSet",
                        "cloudformation:GetTemplate",
                        "cloudformation:GetTemplateSummary",
                        "cloudformation:DescribeStackEvents",
                        "cloudformation:ExecuteChangeSet",
                        "cloudformation:DeleteChangeSet"
                ))
                .build());

        // we can restrict this policy to certain buckets only
        PolicyStatement s3Policy = new PolicyStatement(PolicyStatementProps.builder()
                .resources(Arrays.asList(
                        "*"
                ))
                .actions(Arrays.asList(
                        "s3:*"
                ))
                .build());

        LayerVersion nodeLayer = LayerVersion.Builder.create(this, "node-layer")
                .description("Layer containing node binary")
                .code(
                        Code.fromAsset(tmpBinDir + "/node.zip")
                )
                .build();

        LayerVersion cdkLayer = LayerVersion.Builder.create(this, "aws-cdk-layer")
                .description("Layer containing AWS CDK")
                .code(
                        Code.fromAsset(tmpBinDir + "/aws-cdk.zip")
                )
                .build();

        Function lambda = new Function(this, "RunCdk", FunctionProps.builder()
                .runtime(Runtime.JAVA_11)
                .handler("com.myorg.CdkWrapper")
                .code(Code.fromAsset(tmpBinDir + "/run-cdk-lambda.jar"))
                .layers(Arrays.asList(
                        nodeLayer,
                        cdkLayer
                ))
                .timeout(Duration.seconds(300))
                .memorySize(512)
                .initialPolicy(Arrays.asList(
                        cloudformationPolicy,
                        s3Policy
                ))
                .functionName("run-cdk")
                .build());
    }

    private String getTmpBinDir() {
        Path tmpPath = Paths.get("tmp");
        return tmpPath.toAbsolutePath().toString();
    }
}
