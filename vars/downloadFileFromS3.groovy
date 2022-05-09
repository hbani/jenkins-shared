def call(awsCredentials, s3Url, localDir) {
    s3Url = s3Url.replaceAll(/^https?:\/\/s3-[^\.\/]+\.amazonaws\.com\/([^\/]+)\//, 's3://$1/')
    sh "s3cmd --verbose --preserve --access_key=${awsCredentials.AccessKeyId} --secret_key=\"${awsCredentials.SecretAccessKey}\" --access_token=\"${awsCredentials.Token}\" --force get ${s3Url} ${localDir}"
   return new File(localDir, s3Url.substring(s3Url.lastIndexOf('/') + 1));
}
