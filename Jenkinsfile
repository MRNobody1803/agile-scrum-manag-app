pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK17'
        SonarScanner 'SonarScanner'
    }

    stages {

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean install -DskipTests'
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
                            -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
