@Library('repo-creation') _

pipeline {
    agent any

    stages {
        stage('Creation') {
            steps {
                script {
                    def repoAgent = new com.tirea.jenkinsLib.RepositoryCreation(this) //! pipeline context
                    repoAgent.createRepository()
                }
            }
        }
    }
}
