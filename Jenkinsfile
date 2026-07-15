pipeline {
    agent any

    stages {
        stage('Pull Code') {
            steps {
                echo 'Pulling the latest code from GitHub...'
                checkout scm
            }
        }

        stage('Compile & Test') {
            steps {
                echo 'Building Java project and running unit tests...'
                // Using the Maven wrapper (mvnw) that comes with your Spring Boot/Java project
                // On Linux/Docker, we run it using ./mvnw
                sh 'chmod +x mvnw'
                sh './mvnw clean test'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker container image...'
                // This builds the docker image using the Dockerfile in your project root
                sh 'docker build -t pranaya:latest .'
            }
        }
    }
}