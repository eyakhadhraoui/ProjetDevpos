pipeline {
    agent any

    tools {
        maven 'M2_HOME'  // Assure-toi que Maven est installé sur Jenkins avec ce nom
    }

    stages {

        stage('GIT') {
            steps {
                // Cloner le repo avec le token Git
                git branch: 'brahim',
                    url: 'https://github.com/BrahimGarram/devops.git',
                    credentialsId: 'github-token'
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

        stage('MVN SONARQUBE') {
            steps {
                // Utilise le serveur SonarQube configuré dans Jenkins
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar'
                }
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
