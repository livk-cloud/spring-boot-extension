{
"Statement": [
{
"Action": "s3:GetObject",
"Effect": "Allow",
"Principal": "*",
"Resource": [
"arn:aws:s3:::${bucketName}/common/*"
]
}
],
"Version": "2012-10-17"
}
