package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {


	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {

          String[] names = {"Francisco Javier Vázquez", "Michael Hirsch", "Kim Taekyung", "Pablo López", "Alejandro Carrasco", "Florian Gamillscheg"};
          List<Person> persons = new ArrayList<Person>();

          for(String name : names) {
              String[] fullName = name.split(" ");
              Person person = new Person();
              person.setFirstName(fullName[0]);
              person.setLastName(fullName[1]);
              persons.add(person);
          }

          model.put("persons", persons);
          model.put("title", "Our Projectname");
          model.put("group", "Ling-3");

          return "welcome";
	  }
}
