pipeline {
    agent any

    stages {
        stage('Pull Code') {
            steps {
                echo 'Pulling the latest code from GitHub...'
                checkout scm
            }
        }
        stage('Test Connection') {
            steps {
                echo 'Success! The webhook triggered this build automatically!'
                sh 'docker --version'
            }
        }
    }
}