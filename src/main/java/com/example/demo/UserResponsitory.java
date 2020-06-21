package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResponsitory extends JpaRepository<User, Long> {

	List<User> findByName(String userName); //这是Spring Data提供的功能，只须定义接口，继承Jpa接口，程序启动时会自动实现这里的UserResponsitory接口及其继承的18个方法，无须用户关心；但是对方法命名有要求，要以(1)、findBy开始；(2)、findBy后面要接实体对象的属性(这里实体对象是User,则Name必须是User中的属性)；否则其它地方有注入该bean的时候，在程序启动时会报错创建bean错误,"No property findUser found for type User!"。参考：https://blog.csdn.net/chiliao6979/article/details/100723894

	List<User> findAll();

}
