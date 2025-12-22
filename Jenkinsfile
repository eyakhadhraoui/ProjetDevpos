pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    stages {

        /* =======================
           GIT CODE
        ======================= */
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/eyakhadhraoui/test.git',
                    credentialsId: 'pat_jenkins'
            }
        }

        /* =======================
           MAVEN BUILD
        ======================= */
        stage('MVN CLEAN') {
            steps { sh 'mvn clean' }
        }

        stage('MVN COMPILE') {
            steps { sh 'mvn compile' }
        }

        stage('MVN PACKAGE') {
            steps { sh 'mvn package -DskipTests' }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        /* =======================
           DOCKER SPRING BOOT
        ======================= */
        stage('Docker Cleanup & Build Spring Boot') {
            steps {
                sh '''
                docker rm -f student-app student-mysql || true
                docker rmi -f student-management-app:1.0 || true
                docker build -t student-management-app:1.0 .
                docker run -d -p 8089:8089 --name student-app student-management-app:1.0
                '''
            }
        }

        /* =======================
           DOCKER COMPOSE SERVICES
        ======================= */
        stage('Docker Compose Up') {
            steps {
                sh '''
                docker-compose down --remove-orphans
                docker-compose up -d --build
                '''
            }
        }

        /* =======================
           DEPLOY NODE EXPORTER
        ======================= */
        stage('Deploy Node Exporter') {
            steps {
                sh '''
                # Arrêter si déjà existant
                docker rm -f node_exporter || true

                # Lancer Node Exporter
                docker run -d --net=host --name node_exporter quay.io/prometheus/node-exporter:latest
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
           API REST TEST
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

        /* =======================
           VERIFIER SERVICES POUR PROMETHEUS
        ======================= */
        stage('Verify Metrics Endpoints') {
            steps {
                sh '''
                echo "===== Vérification Node Exporter ====="
                curl -s http://192.168.33.10:9100/metrics | head -n 5 || echo "Node Exporter inaccessible"
                
                echo "===== Vérification Spring Boot ====="
                curl -s http://192.168.33.10:8089/student/actuator/prometheus | head -n 5 || echo "Spring Boot inaccessible"
                
                echo "===== Vérification Jenkins ====="
                curl -s http://192.168.33.10:8080/prometheus | head -n 5 || echo "Jenkins inaccessible"
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès ! Tous les services pour Prometheus sont prêts.'
        }
        failure {
            echo 'Pipeline échoué. Vérifier les logs et les endpoints.'
        }
    }
}
