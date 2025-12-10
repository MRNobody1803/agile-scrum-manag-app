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
           steps {
               script {
                   // Déterminer la branche source de manière robuste
                   def sourceBranch = sh(
                       script: 'git rev-parse --abbrev-ref HEAD',
                       returnStdout: true
                   ).trim()

                   echo "Current branch: ${sourceBranch}"

                   // Ne pas merger si on est déjà sur main
                   if (sourceBranch == 'main') {
                       echo "Already on main branch, skipping merge"
                       return
                   }

                   echo "🔀 Merging ${sourceBranch} into main..."

                   sh """
                       git config user.email "jenkins@example.com"
                       git config user.name "Jenkins CI"

                       # Fetch toutes les branches
                       git fetch origin

                       # Checkout main et pull
                       git checkout main
                       git pull origin main

                       # Merge la branche source
                       git merge ${sourceBranch} --no-ff -m "Merge ${sourceBranch} into main - Quality Gate passed"

                       # Push
                       git push origin main
                   """

                   echo "✅ Merge completed"
               }
           }
       }
    }
}
