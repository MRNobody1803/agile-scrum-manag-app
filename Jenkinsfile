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
                        def qg = waitForQualityGate abortPipeline: true
                        if (qg.status == 'OK') {
                            echo "Quality Gate PASSED ✅ - ready to merge to main"
                            // Merge dans main branch
                            sh """
                                git config user.email "jenkins@example.com"
                                git config user.name "Jenkins CI"
                                git checkout main
                                git merge ${env.BRANCH_NAME}
                                git push origin main
                            """
                        } else {
                            error "Quality Gate FAILED ❌ - abort merge"
                        }
                    }
                }
            }
        }
    }
}
