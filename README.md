This guide will show you how to deploy a Spring MVC Hibernate application to Heroku.

Prerequisites
Java, Maven, Git, and the Heroku client.

Clone the Sample App

:::term
$ git clone https://github.com/heroku/devcenter-spring-mvc-hibernate.git 
Cloning into petclinic...
remote: Counting objects: 205, done.
remote: Compressing objects: 100% (100/100), done.
remote: Total 205 (delta 93), reused 205 (delta 93)
Receiving objects: 100% (205/205), 98.55 KiB, done.
Resolving deltas: 100% (93/93), done.
This will check out the completed app. To step back to the starting point, do:

:::term
$ git revert starting-point

Run Your App Locally

Let's run the app locally first to test that it all works. 

mvn install

[INFO] 
[INFO] --------------------< skillbox-java-course:WebSite >--------------------
[INFO] Building WebSite 1.0
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.5.3:build-info (default) @ WebSite ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ WebSite ---

....

[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ WebSite ---
[INFO] Building jar: C:\skillbox\webproject\webProject\WebSite\target\WebSite-1.0.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.787 s
[INFO] Finished at: 2021-08-10T09:11:22+03:00
[INFO] ------------------------------------------------------------------------


Start Your App
Note: you can also start your app using foreman to execute the Procfile.


$ java -jar ./website/target/WebSite-1.0.jar

Test it
Go to http://localhost:8080 and test it out by creating a new record.

Deploy to Heroku

Commit your changes to Git:

:::term
$ git add .
$ git commit -m "Ready to deploy"
Create the app on the Cedar stack:

Now you need to registration on heroku

https://signup.heroku.com/

And create new app

In the top right menu, click New → Create new app

the application will be further available at 

https://your_app_name.com/

Distributions for installation, as well as a brief instruction for each operating system is located at [https://devcenter.heroku.com/articles/heroku-cli ]

Once the installation is complete, start your favorite terminal emulator, on Windows it is better to use Git Bash.

Execute the command

heroku  --version

Authorization heroku cli

It's time to log into [heroku.com] (http://heroku.com) through the console application.

in your terminal type the command

heroku login

Now create deploy on heroky

git remote add heroku https://git.heroku.com/your_app_name.git

git push heroku


