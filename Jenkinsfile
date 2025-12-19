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
                docker rm -f student-app student-mysql || true
                docker rmi -f student-management-app:1.0 || true
                docker build -t student-management-app:1.0 .
                '''
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh '''
                docker-compose down --remove-orphans
                docker-compose up -d --build
                '''
            }
        }

        /* =======================
           GIT KUBERNETES MANIFESTS
        ======================= */
        stage('GIT KUBERNETES MANIFESTS') {
            steps {
                dir('k8s-repo') {
                    git branch: 'master',
                        url: 'https://github.com/NadineMili/student-management-devops.git'
                }
            }
        }

        /* =======================
           KUBERNETES DEPLOY
        ======================= */
        stage('KUBERNETES DEPLOY') {
            steps {
                sh '''
                echo "===== Kubernetes Deployment ====="
                kubectl get nodes
                kubectl apply -f k8s-repo/student-man-main/k8s/
                kubectl get pods -n devops
                kubectl get svc -n devops
                '''
            }
        }

        /* =======================
           API REST
        ======================= */
        stage('CREATE DEPARTMENT') {
            steps {
                sh '''
                echo "===== Creating Department via REST API ====="
                sleep 20
                curl -X POST http://192.168.49.2:32639/department/createDepartment \
                     -H "Content-Type: application/json" \
                     -d '{"name": "Finance", "location": "Sfax"}'
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
