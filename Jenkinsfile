node {
    withCredentials([[$class: 'UsernamePasswordMultiBinding',
                      credentialsId: 'docker-hub',
                      usernameVariable: 'DOCKER_USER_ID',
                      passwordVariable: 'DOCKER_USER_PASSWORD']]) {
        
        stage('Pull') {
            try {
                git branch: 'main', credentialsId: 'githubserver3', url: 'https://github.com/YeonjiKim0316/fisa03-springboot.git'
                echo "Stage Pull success"
            } catch (Exception e) {
                echo "Stage Pull failed"
                throw e
            }
        }

        stage('Build') {
            sh(script: '''yes | sudo docker image prune -a''')
            sh(script: '''sudo docker build -t fisa-app .''')
            echo "Stage Build success"
        }

        stage('Tag') {
            sh(script: '''sudo docker tag fisa-app ${DOCKER_USER_ID}/fisa-app:${BUILD_NUMBER}''')
            echo "Stage Tag success"
        }

        stage('Push') {
            sh(script: 'sudo docker login -u ${DOCKER_USER_ID} -p ${DOCKER_USER_PASSWORD}')
            sh(script: 'sudo docker push ${DOCKER_USER_ID}/fisa-app:${BUILD_NUMBER}')
            echo "Stage Push success"
        }

        stage('Deploy') {
            sshagent(credentials: ['ec2-springboot-server']) {
                sh(script: 'ssh -o StrictHostKeyChecking=no ubuntu@3.35.238.184 "sudo docker rm -f docker-sb"')
                sh(script: 'ssh ubuntu@3.35.238.184 "sudo docker run --name docker-sb --env-file .env -e TZ=Asia/Seoul -p 8080:8080 -d -t ${DOCKER_USER_ID}/fisa-app:${BUILD_NUMBER}"')
            }
            echo "Stage Deploy success"
        }

        stage('Cleaning up') {
            sh "sudo docker rmi ${DOCKER_USER_ID}/fisa-app:${BUILD_NUMBER}"
            echo "Stage Cleaning up success"
        }
    }
}
