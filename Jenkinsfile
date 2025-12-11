pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://192.168.33.10:9000'
        SONAR_AUTH_TOKEN = credentials('test') // ID de ton token dans Jenkins
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Cloning repository from GitHub...'
                git branch: 'main',
                    url: 'https://github.com/eyakhadhraoui/test.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling the code...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                // Avec timeout pour éviter blocage
                timeout(time: 5, unit: 'MINUTES') {
                    withSonarQubeEnv('SonarQube') {
                        sh """
                            mvn sonar:sonar \
                            -Dsonar.projectKey=sample_project \
                            -Dsonar.host.url=$SONAR_HOST_URL \
                            -Dsonar.login=$SONAR_AUTH_TOKEN
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate result...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! 🎉'
        }
        failure {
            echo 'Pipeline failed. ❌'
        }
    }
}
