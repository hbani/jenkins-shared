def call(awsCredentials, s3Url, localDir) {
// TODO: Need Jenkins 2.60.3 for https://plugins.jenkins.io/pipeline-aws :(
//         withAws(credentials:'tickdata-s3-readonly') {
//            s3Download(file:'/', bucket:'compass-simulations-config', path:configRoot, force:true)
//         }

    s3Url = s3Url.replaceAll(/^https?:\/\/s3-[^\.\/]+\.amazonaws\.com\/([^\/]+)\//, 's3://$1/')

//   withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'JenkinsBacktestSlave', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
    sh "s3cmd --verbose --preserve --access_key=${awsCredentials.AccessKeyId} --secret_key=\"${awsCredentials.SecretAccessKey}\" --access_token=\"${awsCredentials.Token}\" --force get ${s3Url} ${localDir}"
//   }

   return new File(localDir, s3Url.substring(s3Url.lastIndexOf('/') + 1));
}
