pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK17'
    }

    stages {

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw clean test '
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQb') { // inject SONAR_HOST_URL + token
                    script {
                        def scannerHome = tool 'SonarScanner'
                        sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=Agile_Scrum_App \
                            -Dsonar.projectName=Agile_Scrum_App \
                            -Dsonar.sources=src/main/java \
                        """
                    }
                }
            }
        }

       stage('Quality Gate') {
           steps {
               script {
                   timeout(time: 5, unit: 'MINUTES') {
                       def qg = waitForQualityGate()
                       if (qg.status != 'OK') {
                           error "Quality Gate FAILED ❌ - Pipeline stopped"
                       }
                       echo "Quality Gate PASSED ✅"
                   }
               }
           }
       }

       // Stage 2 : Merge vers main (exécutée seulement si Quality Gate passe)
       stage('Merge to Main') {
           when {
               expression {
                   return env.BRANCH_NAME != 'main' // Ne merge que si on n'est pas déjà sur main
               }
           }
           steps {
               script {
                   echo "Merging ${env.BRANCH_NAME} into main branch..."
                   sh """
                       git config user.email "jenkins@example.com"
                       git config user.name "Jenkins CI"
                       git checkout main
                       git merge ${env.BRANCH_NAME} --no-ff -m "Merge ${env.BRANCH_NAME} into main - Quality Gate passed"
                       git push origin main
                   """
                   echo "Successfully merged ${env.BRANCH_NAME} into main ✅"
               }
           }
       }
    }
}
