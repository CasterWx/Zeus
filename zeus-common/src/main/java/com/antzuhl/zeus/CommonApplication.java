package com.antzuhl.zeus;

import com.antzuhl.zeus.dao.NamespaceManager;
import com.antzuhl.zeus.dao.ServiceManager;
import com.antzuhl.zeus.dao.utils.ZeusBaseDal;
import com.antzuhl.zeus.entity.beans.Namespace;
import com.antzuhl.zeus.entity.beans.Service;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.List;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
public class CommonApplication implements ApplicationRunner {


	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		new ZeusBaseDal().createTable();

		NamespaceManager.getInstance().addNamespace(new Namespace(1, "namespace-1", "info-1"));
		ServiceManager.getInstance().addService(new Service(1, 1, "service-1", "DOWN", "info-service"));

		List<Namespace> list = NamespaceManager.getInstance().getAll();
		list.stream().forEach(e-> System.out.println(e.toString()));
		List<Service> list1 = ServiceManager.getInstance().getAll();
		list1.stream().forEach(e-> System.out.println(e.toString()));

		System.out.println("1");
	}
}

