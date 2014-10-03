<h1> Pivotal PCFHawq*Web </h1>

<h2>Download</h2>

Download GemFireXD*Web using the link below. 

<a href="https://dl.dropboxusercontent.com/u/15829935/fe-demos/PCFHawqWeb/pcfhawq.war">pcfhawq.war</a>

<h2>Deployment to PCF 1.3 bound to PHD service</h2>

Note: You should deploy PCFHawq*Web bound to a PHD service and it will automatically connect to the HAWQ instance for you.

2. Download WAR via link above

2. Create a manaifest file as shown below

```
applications:
- name: pcfhawq
  memory: 512M
  instances: 1
  host: pcfhawq
  domain: apj1.fe.gopivotal.com
  path: ./pcfhawq.war
  services:
   - phd-dev
```

3. Deploy as shown below.

```
[Fri Oct 03 18:44:11 papicella@:~/cfapps/pcfhawq-web ] $ cf push -f manifest-apj1.yml
Using manifest file manifest-apj1.yml

Creating app pcfhawq in org pas-org / space apple as pas...
OK

Creating route pcfhawq.apj1.fe.gopivotal.com...
OK

Binding pcfhawq.apj1.fe.gopivotal.com to pcfhawq...
OK

Uploading pcfhawq...
Uploading app files from: pcfhawq.war
Uploading 7M, 195 files
OK
Binding service phd-dev to app pcfhawq in org pas-org / space apple as pas...
OK

Starting app pcfhawq in org pas-org / space apple as pas...
OK
-----> Downloaded app package (5.6M)
-----> Java Buildpack Version: 3455de8 (offline) | https://github.com/dtillman/java-buildpack.git#3455de8
-----> Downloading Open Jdk JRE 1.7.0_65 from https://download.run.pivotal.io/openjdk/lucid/x86_64/openjdk-1.7.0_65.tar.gz (found in cache)
       Expanding Open Jdk JRE to .java-buildpack/open_jdk_jre (1.2s)
-----> Downloading Spring Auto Reconfiguration 1.5.0_RELEASE from https://download.run.pivotal.io/auto-reconfiguration/auto-reconfiguration-1.5.0_RELEASE.jar (found in cache)
       Modifying /WEB-INF/web.xml for Auto Reconfiguration
-----> Downloading Tomcat Instance 7.0.55 from https://download.run.pivotal.io/tomcat/tomcat-7.0.55.tar.gz (found in cache)
       Expanding Tomcat to .java-buildpack/tomcat (0.1s)
-----> Downloading Tomcat Lifecycle Support 2.2.0_RELEASE from https://download.run.pivotal.io/tomcat-lifecycle-support/tomcat-lifecycle-support-2.2.0_RELEASE.jar (found in cache)
-----> Downloading Tomcat Logging Support 2.2.0_RELEASE from https://download.run.pivotal.io/tomcat-logging-support/tomcat-logging-support-2.2.0_RELEASE.jar (found in cache)
-----> Downloading Tomcat Access Logging Support 2.2.0_RELEASE from https://download.run.pivotal.io/tomcat-access-logging-support/tomcat-access-logging-support-2.2.0_RELEASE.jar (found in cache)
-----> Uploading droplet (44M)

1 of 1 instances running

App started

Showing health and status for app pcfhawq in org pas-org / space apple as pas...
OK

requested state: started
instances: 1/1
usage: 512M x 1 instances
urls: pcfhawq.apj1.fe.gopivotal.com

     state     since                    cpu    memory           disk
#0   running   2014-10-03 06:49:27 PM   0.0%   317.5M of 512M   111.4M of 1G
```
