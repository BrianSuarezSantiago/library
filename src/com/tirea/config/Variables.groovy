package com.tirea.config

// Define here all the global variables
class Variables {
    // General configurations
    static def String BACKEND_REPOSITORY_URL = 'https://github.com/BrianSuarezSantiago/java-app.git'
    static def String FRONT_FOLDER_NAME = 'java-app' //! '' Placeholder, will be completed dynamically
    static def String BACK_FOLDER_NAME = 'simple-nodejs-app'

    // Maven specific configurations
    static def String MVN_DOCKER_IMAGE_NAME = 'spring'

    // NPM specific configurations
    static def String NPM_DOCKER_IMAGE_NAME = 'node'

    // Docker specific configurations
    static def String DOCKER_IMAGE_TAG = 'latest'

    // AWS specific configurations
    static def String BUCKET_NAME = 'bucket-for-cicd-pipeline'
    //static def String AWS_ACCESS_KEY_ID = credentials('aws-credentials')  // Name of the global credentials created on Jenkins
    //static def String AWS_SECRET_ACCESS_KEY = credentials('aws-credentials')  // Name of the global credentials created on Jenkins
}
