#build springboot项目
FROM openjdk:8
#指定容器工作目录 没有会自动创建
WORKDIR /ems
#将当前的jar包拷贝到容器的ems目录下
ADD demo-0.0.1-SNAPSHOT.jar /ems/app.jar
#暴露镜像的端口
EXPOSE 8080
#不可变的东西放在ENTRYPOINT中
ENTRYPOINT ["nohub","java","jar"]
#可变的东西放在cmd中
CMD ["app.jar"]



######构建镜像
# docker build -t ems(镜像名)
#运行容器
#dokcer run -p 8080:8080 ems
