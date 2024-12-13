package com.tirea.jenkinsLib

import com.tirea.config.Variables

class PipelineUtils {
    // Clone and configure step
    def prepareStage() {
        //cleanWs()
        sh '''
            git clone ${Variables.FRONTEND_REPOSITORY_URL}
            git clone ${Variables.BACKEND_REPOSITORY_URL}
            echo "Repositories have been successfully cloned"
        '''
    }

    /*
    // Checks if the specify S3 bucket exists or not
    def checkS3BucketExists(bucketName) {
        try {
            def result = sh(script: "aws s3api head-bucket --bucket ${bucketName} 2>/dev/null", returnStatus: true)

            if (result == 0) {
                echo "El bucket '${bucketName}' existe."
                return true
            } else {
                echo "El bucket '${bucketName}' no existe."
                return false
            }
        } catch (Exception exception) {
            echo "Error al verificar el bucket: ${exception.message}"
            return false
        }
    }

    // Build, testing, code quality and containerization for Maven projects
    def mavenBuildStage() {
        //! mvn package -DskipTests -DoutputDirectory=$(pwd)
        sh '''
            mvn clean install
            echo "Build completed on $(date)"
        '''

        //! Integración con herramientas SonarQube, Fortify, IQServer

        writeFile file: 'Dockerfile', text: '''
            FROM openjdk:11-jre-slim
            WORKDIR /app
            COPY target/*.jar app.jar
            ENTRYPOINT ["java", "-jar", "app.jar"]
        '''
        sh '''
            echo "Dockerfile created successfully on $(date)"

            
            echo "Docker image ${MVN_DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} built successfully"
        '''
        //docker build -t ${MVN_DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .

        // ! Pushear imagen

        // ! Subida de la imagen a Nexus
        //cleanWs()
    }

    // Configure deployment destination and save workspace configuration
    def mavenPackageStage() {
        // Iniciar sesión en el clúster OpenShift (usando el token de acceso o kubeconfig)
        //sh "oc login --token=${OPENSHIFT_TOKEN} --server=${OPENSHIFT_SERVER}"

        // Desplegar la imagen en el clúster de OpenShift
        //sh "oc new-app ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} --name=${IMAGE_NAME}"

        // Exponer el servicio si es necesario
        //sh "oc expose svc/${IMAGE_NAME}"

        //print("Zipping Kubernetes configuration files...")
        //sh "zip -r ${zipFileName} ${configDir}/*"
        //echo "Kubernetes configuration files have been successfully zipped into ${zipFileName}"

        // Prepare output to upload to ROSA
        sh '''
            mkdir -p maven_output
            cp target/*.jar Dockerfile maven_output
        '''
    }

    // Upload backend to ROSA cluster
    def mavenDeployStage() {
        //! check --delete flag option for aws sync command
        sh '''
            aws s3 sync maven_output/ s3://${Variables.BUCKET_NAME}/
            echo "Successfully loaded into ${Variables.BUCKET_NAME} on $(date)"
        '''
        cleanWs()
    }

    // Build, testing, code quality, and containerization for NPM projects
    def npmBuildStage() {
        sh '''
            npm install
            
            echo "Build completed on $(date)"
        '''
        //npm run build
        //! Integración con herramientas SonarQube, Fortify, IQServer
        // sh 'sonar-scanner -Dsonar.projectKey=my_project -Dsonar.sources=src -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_TOKEN}'

        writeFile file: 'Dockerfile', text: '''
            FROM node:18-alpine
            WORKDIR /app
            COPY . .
            RUN npm install --production
            CMD ["npm", "start"]
        '''

        sh '''
            echo "Dockerfile created successfully on $(date)"

            
            echo "Docker image ${Variables.NPM_DOCKER_IMAGE_NAME}:${Variables.DOCKER_IMAGE_TAG} built successfully"
        '''
        //docker build -t ${NPM_DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .

        // ! Pushear imagen
        // sh 'docker push ${NPM_DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}'

        // ! Subida de la imagen a Nexus
        //cleanWs()
    }

    // Configure deployment destination and save workspace configuration
    def npmPackageStage() {
        sh '''
            mkdir -p npm_output
            cp -r dist/ package.json Dockerfile npm_output  
        '''
        //echo "NPM output prepared on $(date)"

        checkS3BucketExists("${Variables.BUCKET_NAME}")

        // Opcional: Realizar acciones adicionales (como zipear configuraciones o preparar datos para Kubernetes)
        // sh "zip -r ${zipFileName} ${configDir}/*"
    }

    // Upload frontend to S3 bucket
    def npmDeployStage() {
        sh '''
            aws s3 sync npm_output/ s3://${Variables.BUCKET_NAME}/
            echo "Successfully loaded into ${Variables.BUCKET_NAME} on $(date)"
        '''
        cleanWs()
    }*/
}
