package com.tirea.jenkinsLib

class RepositoryCreation {
    def scriptContext // ! pipeline context

    //! Constructor que recibe el contexto del pipeline
    RepositoryCreation(scriptContext) {
        this.scriptContext = scriptContext
    }

    def createRepository() {
        // !Usar el contexto del pipeline para ejecutar el comando sh
        scriptContext.sh '''
            cd resources/
            pwd
            ls -l
            ./create_repo.sh
        '''
    }
}