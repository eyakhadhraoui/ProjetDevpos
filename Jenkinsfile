pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {

        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/eyakhadhraoui/test.git',
                    credentialsId: 'pat_jenkins' // Ton ID GitHub PAT
            }
        }

        stage('MVN CLEAN') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('MVN COMPILE') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('MVN PACKAGE') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Docker Cleanup & Build') {
            steps {
                sh '''
                # Supprimer containers existants si ils existent
                docker rm -f student-app student-mysql || true
                
                # Supprimer l'ancienne image si elle existe
                docker rmi -f student-management-app:1.0 || true

                # Build de la nouvelle image Spring Boot
                docker build -t student-management-app:1.0 .
                '''
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh '''
                # Supprimer containers et réseaux orphelins
                docker-compose down --remove-orphans

                # Lancer les services avec build
                docker-compose up -d --build
                '''
            }
        }

    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Pipeline échoué.'
        }
    }
}
