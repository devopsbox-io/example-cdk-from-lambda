# Prerequisites

**The currently used version of CDK in this branch is 1.71.0. However, V1 is in maintenance mode since June 1, 2022. See
[aws/aws-cdk#19836](https://github.com/aws/aws-cdk/issues/19836).**

**Please use the [cdk-v2](https://github.com/devopsbox-io/example-cdk-from-lambda/tree/cdk-v2) branch for the AWS CDK v2 version.**

- AWS CDK (tested with 1.71.0)
- Java JDK (tested with openjdk 11.0.9)
- NodeJS (required by AWS CDK, tested with v12.18.3)
- Docker (tested with 18.09.5)
- Maven (tested with 3.6.3)
- AWS CLI (only for testing, tested with 1.18.57)

Should work with different versions of some prerequisites.

# Running

```
cdk bootstrap
cdk deploy --require-approval never
```

# Testing

```
aws s3 ls
```

should not return any bucket with prefix "runcdklambdastack-createdbycdkfromlambda"

Run:

```
aws lambda invoke --function-name run-cdk tmp/lambda-out
```

to start CDK from lambda and create the bucket. The first run can take about 1 minute to complete. After that

```
aws s3 ls
```

should return a bucket created by CDK (with "runcdklambdastack-createdbycdkfromlambda" prefix)
