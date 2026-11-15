// Pipeline CI/CD DevOpsQuiz : CODE -> BUILD -> TEST -> RELEASE -> DEPLOY
// Sans Maven : javac + JUnit Platform Console Standalone.
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    // Déclenchement automatique à chaque push (webhook GitHub).
    // Alternative sans webhook : remplacer par  pollSCM('H/2 * * * *')
    triggers {
        githubPush()
    }

    environment {
        IMAGE_NAME     = 'devopsquiz'
        CONTAINER_NAME = 'devopsquiz-app'
        VOLUME_NAME    = 'devopsquiz-data'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'chmod +x scripts/*.sh'
            }
        }

        stage('Build (javac)') {
            steps {
                sh './scripts/build.sh'
            }
        }

        // Si un test échoue, ce stage échoue : les stages suivants ne s'exécutent pas.
        stage('Tests (JUnit)') {
            steps {
                sh './scripts/test.sh'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-reports/TEST-*.xml'
                }
            }
        }

        stage('Release') {
            steps {
                script {
                    env.APP_VERSION = sh(script: 'git describe --tags --always', returnStdout: true).trim()
                }
                echo "Version : ${env.APP_VERSION} (build #${env.BUILD_NUMBER})"
            }
        }

        stage('Docker build + vérification') {
            steps {
                sh './scripts/docker-build.sh'
            }
        }

        stage('Deploy') {
            steps {
                sh './scripts/docker-deploy.sh'
            }
        }
    }

    post {
        success { echo "Pipeline réussi : ${env.IMAGE_NAME}:${env.APP_VERSION} déployée." }
        failure { echo 'Pipeline en échec : aucun déploiement effectué.' }
    }
}
