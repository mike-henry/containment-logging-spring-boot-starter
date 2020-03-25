pipeline {
    agent any
    tools {

        maven 'maven_3'

    }
    environment {
        // This can be nexus3 or nexus2
        NEXUS_VERSION = "nexus3"
        // This can be http or https
        NEXUS_PROTOCOL = "http"
        // Where your Nexus is running
        NEXUS_URL = "nexus:8081"
        // Repository where we will upload the artifact
        NEXUS_REPOSITORY = "local-builds"
        // Jenkins credential id to authenticate to Nexus OSS
        NEXUS_CREDENTIAL_ID = "nexus-credentials"

        PROJECT_NAME = "containment-logging-spring-boot-starter"

        SETTINGS_XML = './settings.xml'
        GRADLE_PROPERTIES = './gradle.properties'
    }

    stages {
        stage('Initialise') {
            steps {
              configFileProvider([configFile(fileId: 'e1e9d5d0-3f70-410e-a096-38585ed36d99', variable: 'MAVEN_SETTINGS_FILE'),
                                  configFile(fileId: 'ce4190e5-99fe-411b-82ce-0fb8d9b123a1', variable: 'GRADLE_PROPERTY_FILE')]){
                echo '.Initialising..'

                sh '''
                 echo "PATH = ${PATH}"
                 echo "M2_HOME = ${M2_HOME}"
                 echo "MAVEN_HOME = ${MAVEN_HOME}"
                 echo "JAVA_HOME = ${JAVA_HOME}"
                 cp  ${MAVEN_SETTINGS_FILE} ${SETTINGS_XML}
                 cp  ${GRADLE_PROPERTY_FILE} ${GRADLE_PROPERTIES}
                 '''
              }
            }
        }
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'mvn -s ${SETTINGS_XML} clean compile'
                sh './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh 'mvn  -s ${SETTINGS_XML} test'
                 sh './gradlew test'
            }
        }
        stage('Package') {
            steps {
                echo 'Packaging..'
                sh "mvn -s ${SETTINGS_XML} package -DskipTests=true"
                sh './gradlew assemble'
            }
        }


        stage('Deploy') {
            when {
                branch 'develop'
            }
            steps {
                echo 'Deploying.. library'
                sh "mvn -s ${SETTINGS_XML} deploy -DskipTests=true"
                sh './gradlew publish'
            }
        }
    }
}