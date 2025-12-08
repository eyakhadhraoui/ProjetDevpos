pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/eyakhadhraoui/test.git'
                    ]]
                ])
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean install -DskipTests=false'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
    }

    post {
        success {
            echo '🚀 Build Maven réussi !'
        }
        failure {
            echo '❌ Build Maven échoué...'
        }
    }
}
