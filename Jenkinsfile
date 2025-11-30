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

        stage('Build') {
            steps {
                sh './mvnw clean install -DskipTests'
            }
        }

        stage('Scan') {
            steps {
                withSonarQubeEnv(installationName: 'SonarQb') {
                    sh """
                        ./mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar \
                        -Dsonar.projectKey=Agile_Scrum_App \
                        -Dsonar.projectName='Agile_Scrum_App' \
                    """
                }
            }
        }

     //   stage("Quality Gate") {
       //     steps {
         //       timeout(time: 5, unit: 'MINUTES') {
           //         waitForQualityGate abortPipeline: true
             //   }
            //}
        //}
    }
}
