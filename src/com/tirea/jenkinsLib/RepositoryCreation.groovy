package com.tirea.jenkinsLib

class RepositoryCreation {

    def createRepository() {
        sh '''
            cd resources/
            ./create_repo.sh
        '''
    }
}
