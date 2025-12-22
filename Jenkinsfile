pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    environment {
        SPRING_APP_NAME = "student-management-app"
        SPRING_PORT = "8089"
        NODE_EXPORTER_PORT = "9100"
        VM_IP = "192.168.33.10"   // IP de la VM Vagrant
    }

    stages {

        /* =======================
           1. CLONE GITHUB REPO
        ======================= */
        stage('GIT') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/eyakhadhraoui/test.git',
                    credentialsId: 'pat_jenkins'
            }
        }

        /* =======================
           2. BUILD MAVEN
        ======================= */
        stage('MVN CLEAN & PACKAGE') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        /* =======================
           3. SONARQUBE ANALYSIS
        ======================= */
        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('Sonarqube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        /* =======================
           4. DEPLOY SPRING BOOT APP (DOCKER)
        ======================= */
        stage('Docker Build & Run Spring Boot') {
            steps {
                sh '''
                docker rm -f ${SPRING_APP_NAME} || true
                docker rmi -f ${SPRING_APP_NAME}:1.0 || true
                docker build -t ${SPRING_APP_NAME}:1.0 .
                docker run -d -p ${SPRING_PORT}:${SPRING_PORT} --name ${SPRING_APP_NAME} ${SPRING_APP_NAME}:1.0
                '''
            }
        }

        /* =======================
           5. DEPLOY NODE EXPORTER
        ======================= */
        stage('Deploy Node Exporter') {
            steps {
                sh '''
                docker rm -f node_exporter || true
                docker run -d --net=host --name node_exporter quay.io/prometheus/node-exporter:latest
                '''
            }
        }

        /* =======================
           6. CHECK SERVICES
        ======================= */
        stage('Check Services') {
            steps {
                sh '''
                echo "=== Vérification Node Exporter ==="
                curl -s http://${VM_IP}:${NODE_EXPORTER_PORT}/metrics | head -n 5

                echo "=== Vérification Spring Boot ==="
                curl -s http://${VM_IP}:${SPRING_PORT}/student/actuator/prometheus | head -n 5

                echo "=== Vérification Jenkins (Prometheus Plugin) ==="
                curl -s http://localhost:8080/prometheus | head -n 5
                '''
            }
        }

        /* =======================
           7. KUBERNETES DEPLOY
        ======================= */
        stage('GIT KUBERNETES MANIFESTS') {
            steps {
                dir('k8s-repo') {
                    git branch: 'master',
                        url: 'https://github.com/NadineMili/student-management-devops.git'
                }
            }
        }

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
           8. CREATE SAMPLE DEPARTMENT
        ======================= */
        stage('CREATE DEPARTMENT') {
            steps {
                sh '''
                echo "===== Creating Department via REST API ====="
                sleep 20
                curl -X POST http://${VM_IP}:32639/department/createDepartment \
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
