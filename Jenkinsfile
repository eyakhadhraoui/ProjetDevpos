pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {

        stage('GIT CHECKOUT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/eyakhadhraoui/test.git',
                    credentialsId: 'pat_jenkins'
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

        stage('SONARQUBE ANALYSIS') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('DOCKER CLEANUP & BUILD') {
            steps {
                sh '''
                docker rm -f student-app student-mysql || true
                docker rmi -f student-management-app:1.0 || true
                docker build -t student-management-app:1.0 .
                '''
            }
        }

        stage('DOCKER COMPOSE UP') {
            steps {
                sh '''
                docker-compose down --remove-orphans
                docker-compose up -d --build
                '''
            }
        }

        stage('KUBERNETES DEPLOY') {
            steps {
                sh '''
                echo "===== Kubernetes Deployment ====="
                kubectl get nodes
                kubectl apply -f student-man-main/k8s/
                kubectl get pods -n devops
                kubectl get svc -n devops
                '''
            }
        }

        stage('CREATE DEPARTMENT') {
            steps {
                sh '''
                echo "===== Creating Department via REST API ====="
                curl -X POST http://192.168.49.2:32639/department/createDepartment \
                     -H "Content-Type: application/json" \
                     -d '{"name":"Finance","location":"Sfax"}'
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline complet exécuté avec succès'
        }
        failure {
            echo '❌ Échec du pipeline'
        }
    }
}
